package com.volunteer.api.data.user.service.impl;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.volunteer.api.data.user.mapping.TaskV1Mapper;
import com.volunteer.api.data.user.model.TaskStatus;
import com.volunteer.api.data.user.model.domain.TaskDetalization;
import com.volunteer.api.data.user.model.persistence.Task;
import com.volunteer.api.data.user.repository.ProductRepository;
import com.volunteer.api.data.user.repository.TaskRepository;
import com.volunteer.api.data.user.service.AuthService;
import com.volunteer.api.data.user.service.TaskService;
import com.volunteer.api.error.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;
  private final ProductRepository productRepository;
  private final TaskV1Mapper mapper; // TODO: consider having separate mapper for domain objects to
                                     // keep web layer separated
  private final AuthService authService; // TODO: consider moving this to controller to keep web
                                         // layer separated from business layer

  @Override
  public Task createTask(Task task) {
    return taskRepository.save(prepare(task));
  }

  @Override
  @Transactional
  public List<Task> batchCreate(Task blueprint, List<TaskDetalization> details) {
    return taskRepository.saveAll(details.stream().map(detail -> {
      Task taskBlueprint = mapper.clone(blueprint);

      taskBlueprint.setProduct(productRepository.getById(detail.getProductId()));
      taskBlueprint.setQuantity(detail.getQuantity());
      taskBlueprint.setProductMeasure(detail.getUnitOfMeasure());
      return taskBlueprint;
    }).map(this::prepare).collect(Collectors.toList()));
  }

  protected Task prepare(Task task) {
    if (task.getDeadlineDate().isBefore(ZonedDateTime.now())) {
      throw new IllegalArgumentException("Deadline date cannot be in the past");
    }
    task.setId(null);
    task.setStatus(TaskStatus.NEW);
    task.setQuantityLeft(task.getQuantity());
    task.setCreatedAt(ZonedDateTime.now());
    task.setCreatedBy(authService.getCurrentUser());
    return task;
  }

  @Override
  public Optional<Task> getTaskById(int taskId) {
    return taskRepository.findById(taskId);
  }

  @Override
  public void verify(int taskId) {
    Task task = getById(taskId);
    if (task.getStatus() == TaskStatus.NEW) {
      task.setStatus(TaskStatus.VERIFIED);
      task.setVerifiedBy(authService.getCurrentUser());
      task.setVerifiedAt(ZonedDateTime.now());
      taskRepository.save(task);
    } else if (task.getStatus() == TaskStatus.VERIFIED) {
      // Do nothing in case already verified - idempotent operation
    } else {
      throw new IllegalStateException("Cannot verify task in status " + task.getStatus());
    }
  }

  @Override
  public void reject(int taskId) {
    Task task = getById(taskId);
    if (task.getStatus() == TaskStatus.REJECTED) {
      // Do nothing in case already rejected - idempotent operation
    } else if (task.getStatus() == TaskStatus.COMPLETED) { // Disallow rejecting completed
      throw new IllegalStateException("Cannot reject task in status " + task.getStatus());
    } else {
      task.setStatus(TaskStatus.REJECTED);
      task.setClosedBy(authService.getCurrentUser());
      task.setClosedAt(ZonedDateTime.now());
      taskRepository.save(task);
    }
  }

  @Override
  public void complete(int taskId) {
    Task task = getById(taskId);
    if (task.getStatus() == TaskStatus.COMPLETED) {
      // Do nothing in case already completed - idempotent operation
    } else if (task.getStatus() == TaskStatus.VERIFIED) {
      task.setStatus(TaskStatus.COMPLETED);
      task.setClosedBy(authService.getCurrentUser());
      task.setClosedAt(ZonedDateTime.now());
      taskRepository.save(task);
    } else {
      // New or Rejected tasks can't be completed
      throw new IllegalStateException("Cannot complete task in status " + task.getStatus());
    }
  }

  protected Task getById(int taskId) {
    return taskRepository.findById(taskId)
        .orElseThrow(() -> new ObjectNotFoundException("Task not found by ID " + taskId));
  }
}
