package com.volunteer.api.service;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import com.volunteer.api.data.model.api.TaskViewDtoV1;


public interface TaskExportService {

  void exportTasks(Page<TaskViewDtoV1> tasks, HttpServletResponse outputResponse)
      throws IOException;

}
