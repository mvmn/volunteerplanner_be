package com.volunteer.api.controller;

import com.volunteer.api.data.mapping.GenericPageDtoMapper;
import com.volunteer.api.data.mapping.TaskDtoV1Mapper;
import com.volunteer.api.data.mapping.TaskViewDtoV1Mapper;
import com.volunteer.api.data.model.TaskStatus;
import com.volunteer.api.data.model.api.BatchTaskVerificationDtoV1;
import com.volunteer.api.data.model.api.GenericCollectionDtoV1;
import com.volunteer.api.data.model.api.GenericPageDtoV1;
import com.volunteer.api.data.model.api.TaskDtoV1;
import com.volunteer.api.data.model.api.TaskSearchDtoV1;
import com.volunteer.api.data.model.api.TaskVerificationDtoV1;
import com.volunteer.api.data.model.api.TaskViewDtoV1;
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

@Validated
@RestController
@RequestMapping(path = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TaskControllerV1 {

  private final TaskService taskService;

  private final TaskDtoV1Mapper taskMapper;
  private final TaskViewDtoV1Mapper taskViewMapper;

  @PreAuthorize("hasAuthority('TASKS_MODIFY')")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TaskViewDtoV1 create(@RequestBody @Valid final TaskDtoV1 source) {
    return taskViewMapper.map(taskService.create(taskMapper.map(source)));
  }

  @PreAuthorize("hasAuthority('TASKS_MODIFY')")
  @PutMapping("/{task-id}")
  @ResponseStatus(HttpStatus.OK)
  public TaskViewDtoV1 update(@PathVariable("task-id") final Integer taskId,
      @RequestBody @Valid final TaskDtoV1 source) {
    final Task task = taskMapper.map(source);
    task.setId(taskId);

    return taskViewMapper.map(taskService.update(task));
  }

  @PreAuthorize("hasAuthority('TASKS_MODIFY')")
  @DeleteMapping("/{task-id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("task-id") final Integer taskId) {
    taskService.delete(taskId);
  }

  @PostMapping("/batch")
  @PreAuthorize("hasAuthority('TASKS_MODIFY')")
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
      @RequestBody(required = false) @Valid TaskVerificationDtoV1 verificationDto) {
    taskService.changeStatus(taskId, TaskStatus.VERIFIED,
        verificationDto != null ? verificationDto.getVerificationComment() : null);
  }

  @PreAuthorize("hasAuthority('TASKS_COMPLETE')")
  @PostMapping("/{taskId}/complete")
  public void complete(@PathVariable("taskId") Integer taskId,
      @RequestBody(required = false) @Valid TaskVerificationDtoV1 verificationDto) {
    taskService.changeStatus(taskId, TaskStatus.COMPLETED,
        verificationDto != null ? verificationDto.getVerificationComment() : null);
  }

  @PreAuthorize("hasAuthority('TASKS_REJECT')")
  @PostMapping("/{taskId}/reject")
  public void reject(@PathVariable("taskId") Integer taskId,
      @RequestBody(required = false) @Valid TaskVerificationDtoV1 verificationDto) {
    taskService.changeStatus(taskId, TaskStatus.REJECTED,
        verificationDto != null ? verificationDto.getVerificationComment() : null);
  }

  @PreAuthorize("hasAuthority('TASKS_VERIFY')")
  @PostMapping("/batch/verify")
  public void batchVerify(@RequestBody @Valid BatchTaskVerificationDtoV1 dto) {
    taskService.changeStatus(dto.getItems(), TaskStatus.VERIFIED, dto.getVerificationComment());
  }

  @PreAuthorize("hasAuthority('TASKS_COMPLETE')")
  @PostMapping("/batch/complete")
  public void batchComplete(@RequestBody @Valid BatchTaskVerificationDtoV1 dto) {
    taskService.changeStatus(dto.getItems(), TaskStatus.COMPLETED, dto.getVerificationComment());
  }

  @PreAuthorize("hasAuthority('TASKS_REJECT')")
  @PostMapping("/batch/reject")
  public void batchReject(@RequestBody @Valid BatchTaskVerificationDtoV1 dto) {
    taskService.changeStatus(dto.getItems(), TaskStatus.REJECTED, dto.getVerificationComment());
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
        searchResult, task -> taskViewMapper.map(task, authentication));
  }
}
