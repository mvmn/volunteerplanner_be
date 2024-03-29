package com.volunteer.api.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.volunteer.api.data.mapping.GenericPageDtoMapper;
import com.volunteer.api.data.mapping.TaskDtoV1Mapper;
import com.volunteer.api.data.mapping.TaskViewDtoV1Mapper;
import com.volunteer.api.data.model.TaskStatus;
import com.volunteer.api.data.model.UserAuthority;
import com.volunteer.api.data.model.api.BatchTaskStatusChangeDtoV1;
import com.volunteer.api.data.model.api.GenericCollectionDtoV1;
import com.volunteer.api.data.model.api.GenericPageDtoV1;
import com.volunteer.api.data.model.api.TaskDtoV1;
import com.volunteer.api.data.model.api.TaskSearchDtoV1;
import com.volunteer.api.data.model.api.TaskStatusChangeDtoV1;
import com.volunteer.api.data.model.api.TaskViewDtoV1;
import com.volunteer.api.data.model.persistence.Task;
import com.volunteer.api.service.TaskExportService;
import com.volunteer.api.service.TaskService;
import com.volunteer.api.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping(path = "/api/v1/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TaskControllerV1 {

  private final TaskService taskService;
  private final TaskExportService taskExportService;
  
  private final TaskDtoV1Mapper taskMapper;
  private final TaskViewDtoV1Mapper taskViewMapper;

  @PreAuthorize("hasAuthority('TASKS_MODIFY') or hasAuthority('TASKS_MODIFY_MINE')")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TaskViewDtoV1 create(@RequestBody @Valid final TaskDtoV1 source) {
    return taskViewMapper.map(taskService.create(taskMapper.map(source)));
  }

  @PreAuthorize("hasAuthority('TASKS_MODIFY') or hasAuthority('TASKS_MODIFY_MINE')")
  @PutMapping("/{task-id}")
  @ResponseStatus(HttpStatus.OK)
  public TaskViewDtoV1 update(@PathVariable("task-id") final Integer taskId,
      @RequestBody @Valid final TaskDtoV1 source, final Authentication authentication) {
    final boolean updateAny = AuthenticationUtils.hasAuthority(
        UserAuthority.TASKS_MODIFY, authentication);

    final Task task = taskMapper.map(source);
    task.setId(taskId);

    return taskViewMapper.map(taskService.update(task, !updateAny));
  }

  @PreAuthorize("hasAuthority('TASKS_MODIFY') or hasAuthority('TASKS_MODIFY_MINE')")
  @DeleteMapping("/{task-id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("task-id") final Integer taskId,
      final Authentication authentication) {
    final boolean deleteAny = AuthenticationUtils.hasAuthority(
        UserAuthority.TASKS_MODIFY, authentication);

    taskService.delete(taskId, !deleteAny);
  }

  @PostMapping("/batch")
  @PreAuthorize("hasAuthority('TASKS_MODIFY') or hasAuthority('TASKS_MODIFY_MINE')")
  @ResponseStatus(HttpStatus.CREATED)
  public GenericCollectionDtoV1<TaskViewDtoV1> create(
      @RequestBody @Valid final GenericCollectionDtoV1<TaskDtoV1> source) {

    final Collection<TaskViewDtoV1> items = taskViewMapper.map(
        taskService.create(source.getItems().stream()
            .map(taskMapper::map)
            .collect(Collectors.toList()))
    );

    return GenericCollectionDtoV1.<TaskViewDtoV1>builder()
        .items(items)
        .build();
  }

  @PreAuthorize("hasAuthority('TASKS_VIEW')")
  @GetMapping("/{taskId}")
  public TaskViewDtoV1 getById(@PathVariable("taskId") final Integer taskId,
      final Authentication authentication) {
    return taskViewMapper.map(taskService.get(taskId), authentication);
  }

  @PreAuthorize("hasAuthority('TASKS_VERIFY')")
  @PostMapping("/{taskId}/verify")
  public void verify(@PathVariable("taskId") Integer taskId,
      @RequestBody(required = false) @Valid TaskStatusChangeDtoV1 dto) {
    taskService.changeStatus(taskId, TaskStatus.VERIFIED,
        Objects.isNull(dto) ? null : dto.getComment(), false);
  }

  @PreAuthorize("hasAuthority('TASKS_COMPLETE')")
  @PostMapping("/{taskId}/complete")
  public void complete(@PathVariable("taskId") Integer taskId,
      @RequestBody(required = false) @Valid TaskStatusChangeDtoV1 dto) {
    taskService.changeStatus(taskId, TaskStatus.COMPLETED,
        Objects.isNull(dto) ? null : dto.getComment(), false);
  }

  @PreAuthorize("hasAuthority('TASKS_REJECT') or hasAuthority('TASKS_REJECT_MINE')")
  @PostMapping("/{taskId}/reject")
  public void reject(@PathVariable("taskId") Integer taskId,
      @RequestBody(required = false) @Valid TaskStatusChangeDtoV1 dto,
      final Authentication authentication) {
    final boolean rejectAny = AuthenticationUtils.hasAuthority(
        UserAuthority.TASKS_REJECT, authentication);

    taskService.changeStatus(taskId, TaskStatus.REJECTED,
        Objects.isNull(dto) ? null : dto.getComment(), !rejectAny);
  }

  @PreAuthorize("hasAuthority('TASKS_VERIFY')")
  @PostMapping("/batch/verify")
  public void batchVerify(@RequestBody @Valid BatchTaskStatusChangeDtoV1 dto) {
    taskService.changeStatus(dto.getItems(), TaskStatus.VERIFIED, dto.getComment(), false);
  }

  @PreAuthorize("hasAuthority('TASKS_COMPLETE')")
  @PostMapping("/batch/complete")
  public void batchComplete(@RequestBody @Valid BatchTaskStatusChangeDtoV1 dto) {
    taskService.changeStatus(dto.getItems(), TaskStatus.COMPLETED, dto.getComment(), false);
  }

  @PreAuthorize("hasAuthority('TASKS_REJECT') or hasAuthority('TASKS_REJECT_MINE')")
  @PostMapping("/batch/reject")
  public void batchReject(@RequestBody @Valid BatchTaskStatusChangeDtoV1 dto,
      final Authentication authentication) {
    final boolean rejectAny = AuthenticationUtils.hasAuthority(
        UserAuthority.TASKS_REJECT, authentication);

    taskService.changeStatus(dto.getItems(), TaskStatus.REJECTED, dto.getComment(), !rejectAny);
  }

  @PreAuthorize("hasAuthority('TASKS_VIEW')")
  @GetMapping("/search")
  public GenericCollectionDtoV1<TaskViewDtoV1> search(
      @RequestParam("ids") @NotEmpty final List<Integer> ids, final Authentication authentication) {
    final Collection<TaskViewDtoV1> tasks = taskViewMapper.map(
        taskService.get(ids), authentication);

    return GenericCollectionDtoV1.<TaskViewDtoV1>builder().items(tasks).build();
  }

  @PreAuthorize("hasAuthority('TASKS_VIEW')")
  @PostMapping("search")
  public GenericPageDtoV1<TaskViewDtoV1> search(@RequestBody @Valid TaskSearchDtoV1 searchRequest,
      final Authentication authentication) {
    Page<Task> searchResult = searchTasks(searchRequest);

    return GenericPageDtoMapper.map(searchRequest.getPageNumber(), searchRequest.getPageSize(),
        searchResult, task -> taskViewMapper.map(task, authentication));
  }

  @PreAuthorize("hasAuthority('TASKS_VIEW')")
  @PostMapping(value = "export/xls",
      produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
  public void exportXls(@RequestBody @Valid TaskSearchDtoV1 searchRequest,
      final Authentication authentication, final HttpServletResponse response) throws IOException {
    Page<Task> searchResult = searchTasks(searchRequest);
    taskExportService
        .exportTasks(searchResult.map(task -> taskViewMapper.map(task, authentication)), response);
  }

  private Page<Task> searchTasks(TaskSearchDtoV1 searchRequest) {
    Integer pageSize = searchRequest.getPageSize();
    Integer pageNumber = searchRequest.getPageNumber();

    Sort order = Sort.by("deadlineDate").ascending();
    if (searchRequest.getSortOrder() != null) {
      switch (searchRequest.getSortOrder()) {
        case DUEDATE:
          order = Sort.by("deadlineDate").ascending();
          break;
        case PRIORITY:
          order = Sort.by("priority").ascending();
          break;
        case QUANTITY:
          order = Sort.by("quantity").ascending();
          break;
        case QUANTITY_LEFT:
          order = Sort.by("quantityLeft").ascending();
          break;
        case PRODUCT_NAME:
          order = Sort.by("product.name").ascending();
          break;
        case STATUS:
          order = Sort.by("status").ascending();
          break;
        case ID:
          order = Sort.by("id").ascending();
          break;
      }
    }
    if (searchRequest.getSortDirection() != null) {
      switch (searchRequest.getSortDirection()) {
        case ASC:
          order = order.ascending();
          break;
        case DESC:
          order = order.descending();
          break;
      }
    }

    Page<Task> searchResult = taskService.search(searchRequest.getCustomer(),
        searchRequest.getSearchText(), searchRequest.getProductId(),
        searchRequest.getVolunteerStoreId(), searchRequest.getCustomerStoreId(),
        searchRequest.getStatuses(), searchRequest.getCategoryIds(),
        searchRequest.getCategoryPath(), searchRequest.getRemainingQuantityMoreThan(),
        searchRequest.getZeroQuantity() != null ? searchRequest.getZeroQuantity().booleanValue()
            : false,
        searchRequest.getExcludeExpired() != null ? searchRequest.getExcludeExpired().booleanValue()
            : false,
        searchRequest.getCreatedByUserId(), searchRequest.getVerifiedByUserId(),
        searchRequest.getClosedByUserId(), PageRequest.of(pageNumber != null ? pageNumber : 0,
            pageSize != null ? pageSize : 10, order));
    return searchResult;
  }
}
