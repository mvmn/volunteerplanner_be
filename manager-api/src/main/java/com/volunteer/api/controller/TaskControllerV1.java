package com.volunteer.api.controller;

import com.volunteer.api.data.mapping.GenericPageDtoMapper;
import com.volunteer.api.data.mapping.TaskV1Mapper;
import com.volunteer.api.data.model.TaskStatus;
import com.volunteer.api.data.model.api.GenericCollectionDtoV1;
import com.volunteer.api.data.model.api.GenericPageDtoV1;
import com.volunteer.api.data.model.api.TaskDtoV1;
import com.volunteer.api.data.model.api.TaskSearchDtoV1;
import com.volunteer.api.data.model.persistence.Task;
import com.volunteer.api.service.TaskService;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(path = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TaskControllerV1 {

  private final TaskService taskService;
  private final TaskV1Mapper taskMapper;

  @PostMapping
  @PreAuthorize("hasAuthority('TASKS_MODIFY')")
  @ResponseStatus(HttpStatus.CREATED)
  public TaskDtoV1 create(@RequestBody @Valid final TaskDtoV1 source) {
    return taskMapper.map(taskService.create(taskMapper.map(source)));
  }

  @PostMapping("/batch")
  @PreAuthorize("hasAuthority('TASKS_MODIFY')")
  @ResponseStatus(HttpStatus.CREATED)
  public GenericCollectionDtoV1<TaskDtoV1> create(
      @RequestBody @Valid final GenericCollectionDtoV1<TaskDtoV1> source) {

    final Collection<TaskDtoV1> items = taskMapper.map(taskService.create(
        source.getItems().stream()
            .map(taskMapper::map)
            .collect(Collectors.toList()))
    );

    return GenericCollectionDtoV1.<TaskDtoV1>builder()
        .items(items)
        .build();
  }

  @PreAuthorize("hasAuthority('TASKS_VIEW')")
  @GetMapping("/{taskId}")
  public TaskDtoV1 getById(@PathVariable("taskId") Integer taskId) {
    return taskMapper.map(taskService.get(taskId));
  }

  @PreAuthorize("hasAuthority('TASKS_VERIFY')")
  @PostMapping("/{taskId}/verify")
  public void verify(@PathVariable("taskId") Integer taskId) {
    taskService.changeStatus(taskId, TaskStatus.VERIFIED);
  }

  @PreAuthorize("hasAuthority('TASKS_COMPLETE')")
  @PostMapping("/{taskId}/complete")
  public void complete(@PathVariable("taskId") Integer taskId) {
    taskService.changeStatus(taskId, TaskStatus.COMPLETED);
  }

  @PreAuthorize("hasAuthority('TASKS_REJECT')")
  @PostMapping("/{taskId}/reject")
  public void reject(@PathVariable("taskId") Integer taskId) {
    taskService.changeStatus(taskId, TaskStatus.REJECTED);
  }

  @PreAuthorize("hasAuthority('TASKS_VERIFY')")
  @PostMapping("/batch/verify")
  public void batchVerify(@RequestBody GenericCollectionDtoV1<Integer> taskIds) {
    taskService.changeStatus(taskIds.getItems(), TaskStatus.VERIFIED);
  }

  @PreAuthorize("hasAuthority('TASKS_COMPLETE')")
  @PostMapping("/batch/complete")
  public void batchComplete(@RequestBody GenericCollectionDtoV1<Integer> taskIds) {
    taskService.changeStatus(taskIds.getItems(), TaskStatus.COMPLETED);
  }

  @PreAuthorize("hasAuthority('TASKS_REJECT')")
  @PostMapping("/batch/reject")
  public void batchReject(@RequestBody GenericCollectionDtoV1<Integer> taskIds) {
    taskService.changeStatus(taskIds.getItems(), TaskStatus.REJECTED);
  }

  @PreAuthorize("hasAuthority('TASKS_VIEW')")
  @GetMapping("/search")
  public GenericCollectionDtoV1<TaskDtoV1> getByIds(
      @RequestParam("ids") @NotEmpty final List<Integer> ids) {
    final Collection<TaskDtoV1> tasks = taskMapper.map(taskService.get(ids));
    return GenericCollectionDtoV1.<TaskDtoV1>builder().items(tasks).build();
  }

  @PreAuthorize("hasAuthority('TASKS_VIEW')")
  @PostMapping("search")
  public GenericPageDtoV1<TaskDtoV1> search(@RequestBody @Valid TaskSearchDtoV1 searchRequest) {
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
        searchRequest.getCreatedByUserId(), searchRequest.getVerifiedByUserId(),
        searchRequest.getClosedByUserId(), PageRequest.of(pageNumber != null ? pageNumber : 0,
            pageSize != null ? pageSize : 10, order));

    return GenericPageDtoMapper.map(searchRequest.getPageNumber(), searchRequest.getPageSize(),
        searchResult, taskMapper::map);
  }
}
