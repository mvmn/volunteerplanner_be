package com.volunteer.api.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.volunteer.api.data.model.TaskPriority;
import com.volunteer.api.data.model.TaskStatus;
import com.volunteer.api.data.model.api.StoreDtoV1;
import com.volunteer.api.data.model.api.TaskViewDtoV1;
import com.volunteer.api.data.model.api.UserDtoV1;
import com.volunteer.api.service.TaskExportService;
import com.volunteer.api.utils.lang.Pair;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskExportServiceImpl implements TaskExportService {

  private static Map<TaskPriority, String> PRIORITY_DISPLAY_NAMES =
      Map.of(TaskPriority.CRITICAL, "Критичний", TaskPriority.HIGH, "Високий", TaskPriority.NORMAL,
          "Середній", TaskPriority.LOW, "Низький");

  private static Map<TaskStatus, String> STATUS_DISPLAY_NAMES =
      Map.of(TaskStatus.COMPLETED, "Завершений", TaskStatus.REJECTED, "Відхилений",
          TaskStatus.VERIFIED, "Підтверджений", TaskStatus.NEW, "Новий");
  
  private static final List<Pair<String, Function<TaskViewDtoV1, String>>> FIELD_DEFINITIONS =
      List.of(fieldDef("Ідентифікатор", t -> t.getId().toString()),
          fieldDef("Волонтерський склад", t -> describeStore(t.getVolunteerStore())),
          fieldDef("Кінцевий склад", t -> describeStore(t.getCustomerStore())),
          fieldDef("Продукт", t -> t.getProduct().getName()),
          fieldDef("Кількість", t -> t.getQuantity().toPlainString()),
          fieldDef("Залишок кількості", t -> t.getQuantityLeft().toPlainString()),
          fieldDef("Одиниці виміру", t -> t.getProductMeasure()),
          fieldDef("Приорітет", t -> PRIORITY_DISPLAY_NAMES.getOrDefault(t.getPriority(), "-")),
          fieldDef("Дедлайн", t -> formatDate(t.getDeadlineDate())),
          fieldDef("Примітки", t -> t.getNote()), //
          fieldDef("Статус", t -> STATUS_DISPLAY_NAMES.getOrDefault(t.getStatus(), "-")),
          fieldDef("Кількість підзадач", t -> t.getSubtaskCount().toString()),
          fieldDef("Хто створив", t -> formatUser(t.getCreatedBy())),
          fieldDef("Дата створення", t -> formatDate(t.getCreatedAt())),
          fieldDef("Хто перевірив", t -> formatUser(t.getVerifiedBy())),
          fieldDef("Дата перевірки", t -> formatDate(t.getVerifiedAt())),
          fieldDef("Коментар перевірки", t -> t.getVerificationComment()),
          fieldDef("Хто закрив", t -> formatUser(t.getClosedBy())),
          fieldDef("Дата закриття", t -> formatDate(t.getClosedAt())),
          fieldDef("Комнетар закриття", t -> t.getCloseComment()));
  
  private static Pair<String, Function<TaskViewDtoV1, String>> fieldDef(String name,
      Function<TaskViewDtoV1, String> valueMapper) {
    return Pair.of(name, val -> {
      String result = valueMapper.apply(val);
      return result != null ? result : "";
    });
  }

  @Override
  public void exportTasks(Page<TaskViewDtoV1> tasks, HttpServletResponse outputResponse)
      throws IOException {
    try (XSSFWorkbook workbook = new XSSFWorkbook()) {
      XSSFSheet sheet = workbook.createSheet("Java Books");

      AtomicInteger rowIdx = new AtomicInteger(0);
      XSSFRow headingRow = sheet.createRow(0);
      int headingCellIdx = 0;
      for (String heading : FIELD_DEFINITIONS.stream().map(Pair::getA)
          .collect(Collectors.toList())) {
        XSSFCell cell = headingRow.createCell(headingCellIdx++);
        cell.setCellValue(heading);
      }
      tasks.forEach(taskView -> {
        XSSFRow row = sheet.createRow(rowIdx.incrementAndGet());

        mapRow(row, taskView);
      });

      String filename =
          String.format("tasks_page_%d_of_%d.xlsx", tasks.getNumber() + 1, tasks.getTotalPages());
      outputResponse.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
      try (OutputStream out = outputResponse.getOutputStream()) {
        workbook.write(out);
        out.flush();
      }
    }
  }

  private void mapRow(XSSFRow row, TaskViewDtoV1 taskView) {
    int cellIdx = 0;
    for (Pair<String, Function<TaskViewDtoV1, String>> fieldDef : FIELD_DEFINITIONS) {
      XSSFCell cell = row.createCell(cellIdx++);
      cell.setCellValue(fieldDef.getB().apply(taskView));
    }
  }

  private static String describeStore(StoreDtoV1 store) {
    if (store == null) {
      return "";
    }
    return store.getName() + ", " + store.getAddress() + ". "
        + (store.getConfidential() != null && store.getConfidential().booleanValue()
            ? "Конфіденційний"
            : "");
  }

  private static String formatDate(Long date) {
    return date != null
        ? ZonedDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneOffset.ofHours(3)).toString()
        : "";
  }

  private static String formatUser(UserDtoV1 user) {
    return user != null ? user.getDisplayName() : "";
  }
}
