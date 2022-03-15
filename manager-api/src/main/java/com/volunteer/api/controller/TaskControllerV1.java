package com.volunteer.api.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.volunteer.api.data.mapping.TaskV1Mapper;
import com.volunteer.api.data.model.TaskStatus;
import com.volunteer.api.data.model.api.GenericCollectionDtoV1;
import com.volunteer.api.data.model.api.TaskBatchDtoV1;
import com.volunteer.api.data.model.api.TaskDtoV1;
import com.volunteer.api.data.model.api.TaskSearchDtoV1;
import com.volunteer.api.data.model.domain.TaskDetalization;
import com.volunteer.api.data.model.persistence.Task;
import com.volunteer.api.error.ObjectNotFoundException;
import com.volunteer.api.service.TaskService;
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

  @PostMapping("batch")
  @PreAuthorize("hasAuthority('operator')")
  public GenericCollectionDtoV1<TaskDtoV1> createTasks(@RequestBody @Valid TaskBatchDtoV1 dto) {
    Task blueprint = taskMapper.map(dto.getBlueprint());
    List<TaskDetalization> details =
        dto.getDetails().stream().map(taskMapper::map).collect(Collectors.toList());
    List<TaskDtoV1> createdTasks = taskService.batchCreate(blueprint, details).stream()
        .map(taskMapper::map).collect(Collectors.toList());
    return GenericCollectionDtoV1.<TaskDtoV1>builder().items(createdTasks).build();
  }

  @GetMapping("batch/{taskIds}")
  public GenericCollectionDtoV1<TaskDtoV1> getTasksByIds(
      @PathVariable("taskIds") List<Integer> taskIds) {
    List<TaskDtoV1> tasks = taskService.getTasksByIds(taskIds).stream().map(taskMapper::map)
        .collect(Collectors.toList());
    return GenericCollectionDtoV1.<TaskDtoV1>builder().items(tasks).build();
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

  @PreAuthorize("hasAuthority('operator')")
  @PostMapping("batch/verify")
  public void batchVerify(@RequestBody GenericCollectionDtoV1<Integer> taskIds) {
    taskService.batchStatusChange(taskIds.getItems(), TaskStatus.VERIFIED);
  }

  @PreAuthorize("hasAuthority('operator')")
  @PostMapping("batch/complete")
  public void batchComplete(@RequestBody GenericCollectionDtoV1<Integer> taskIds) {
    taskService.batchStatusChange(taskIds.getItems(), TaskStatus.COMPLETED);
  }

  @PreAuthorize("hasAuthority('operator')")
  @PostMapping("batch/reject")
  public void batchReject(@RequestBody GenericCollectionDtoV1<Integer> taskIds) {
    taskService.batchStatusChange(taskIds.getItems(), TaskStatus.REJECTED);
  }

  @PreAuthorize("hasAuthority('operator') or hasAuthority('volunteer')")
  @PostMapping("search")
  public Page<TaskDtoV1> search(@RequestBody @Valid TaskSearchDtoV1 searchRequest) {
    Integer pageSize = searchRequest.getPageSize();
    Integer pageNumber = searchRequest.getPageNumber();

    Sort order = Sort.by("deadlineDate").ascending();
    if (searchRequest.getSortOrder() == TaskSearchDtoV1.SortOrder.PRIORITY) {
      order = Sort.by("priority").descending();
    }

    Page<Task> searchResult = taskService.search(searchRequest.getCustomer(),
        searchRequest.getProductId(), searchRequest.getVolunteerStoreId(),
        searchRequest.getCustomerStoreId(), searchRequest.getStatuses(),
        searchRequest.getCategoryIds(), searchRequest.getRemainingQuantityMoreThan(),
        searchRequest.getZeroQuantity() != null ? searchRequest.getZeroQuantity().booleanValue()
            : false,
        searchRequest.getExcludeExpired() != null ? searchRequest.getExcludeExpired().booleanValue()
            : false,
        PageRequest.of(pageSize != null ? pageSize : 10, pageNumber != null ? pageNumber : 0,
            order));
    return searchResult.map(taskMapper::map);
  }
}
