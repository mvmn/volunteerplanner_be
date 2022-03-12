package com.volunteer.api.data.user;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.volunteer.api.data.user.mapping.TaskV1Mapper;
import com.volunteer.api.data.user.model.api.TaskBatchDtoV1;
import com.volunteer.api.data.user.model.api.TaskDtoV1;
import com.volunteer.api.data.user.model.domain.TaskDetalization;
import com.volunteer.api.data.user.model.persistence.Task;
import com.volunteer.api.data.user.service.TaskService;
import com.volunteer.api.error.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TaskControllerV1 {

  private final TaskService taskService;
  private final TaskV1Mapper taskMapper;

  @PostMapping
  @PreAuthorize("hasAuthority('operator')")
  public TaskDtoV1 createTask(@RequestBody @Valid TaskDtoV1 dto) {
    return taskMapper.map(taskService.createTask(taskMapper.map(dto)));
  }

  @PostMapping("/batch")
  @PreAuthorize("hasAuthority('operator')")
  public Collection<TaskDtoV1> createTasks(@RequestBody @Valid TaskBatchDtoV1 dto) {
    Task blueprint = taskMapper.map(dto.getBlueprint());
    List<TaskDetalization> details =
        dto.getDetails().stream().map(taskMapper::map).collect(Collectors.toList());
    return taskService.batchCreate(blueprint, details).stream().map(taskMapper::map)
        .collect(Collectors.toList());
  }

  @GetMapping("/batch/{taskIds}")
  public List<TaskDtoV1> getTasksByIds(@PathVariable("taskIds") List<Integer> taskIds) {
    return taskService.getTasksByIds(taskIds).stream().map(taskMapper::map)
        .collect(Collectors.toList());
  }

  @GetMapping("{taskId}")
  public TaskDtoV1 getTaskById(@PathVariable("taskId") Integer taskId) {
    return taskService.getTaskById(taskId).map(taskMapper::map)
        .orElseThrow(() -> new ObjectNotFoundException("Task not found by ID " + taskId));
  }

  @PreAuthorize("hasAuthority('operator')")
  @PostMapping("{taskId}/verify")
  public void verify(@PathVariable("taskId") Integer taskId) {
    taskService.verify(taskId);
  }

  @PreAuthorize("hasAuthority('operator')")
  @PostMapping("{taskId}/complete")
  public void complete(@PathVariable("taskId") Integer taskId) {
    taskService.complete(taskId);
  }

  @PreAuthorize("hasAuthority('operator')")
  @PostMapping("{taskId}/reject")
  public void reject(@PathVariable("taskId") Integer taskId) {
    taskService.reject(taskId);
  }
}
