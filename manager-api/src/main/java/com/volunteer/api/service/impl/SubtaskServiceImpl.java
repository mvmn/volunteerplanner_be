package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.SubtaskStatus;
import com.volunteer.api.data.model.persistence.Subtask;
import com.volunteer.api.data.model.persistence.Task;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.repository.SubtaskRepository;
import com.volunteer.api.data.repository.search.Query;
import com.volunteer.api.data.repository.search.QueryBuilder;
import com.volunteer.api.error.InvalidQuantityException;
import com.volunteer.api.error.InvalidStatusException;
import com.volunteer.api.error.ObjectNotFoundException;
import com.volunteer.api.service.SubtaskService;
import com.volunteer.api.service.TaskService;
import com.volunteer.api.service.UserService;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubtaskServiceImpl implements SubtaskService {

  private final SubtaskRepository repository;

  private final TaskService taskService;
  private final UserService userService;

  @Override
  public Page<Subtask> getAll(final QueryBuilder<Subtask> queryBuilder) {
    final Query<Subtask> query = queryBuilder.build();
    return repository.findAll(query.getSpecification(), query.getPageable());
  }

  @Override
  public Subtask getById(final Integer subtaskId, final boolean onlyMine) {
    final Subtask result = repository.findById(subtaskId).orElseThrow(
        () -> new ObjectNotFoundException(String.format(
            "Subtask with id '%d' not found", subtaskId)));

    if (onlyMine) {
      final VPUser user = userService.getCurrentUser();
      if (!result.getCreatedBy().getId().equals(user.getId())) {
        throw new AuthorizationServiceException(String.format(
            "You are not allowed to view subtask '%d'", subtaskId));
      }
    }

    return result;
  }

  @Override
  public Collection<Subtask> getByTaskId(final Integer taskId, final boolean onlyMine) {
    if (onlyMine) {
      final VPUser user = userService.getCurrentUser();
      return repository.findByTaskIdAndCreatedById(taskId, user.getId());
    } else {
      return repository.findByTaskId(taskId);
    }
  }

  @Override
  @Transactional
  public Subtask create(final Subtask subtask) {
    validateQuantity(subtask.getQuantity());

    final Task task = taskService.get(subtask.getTask().getId());

    final BigDecimal delta = taskService.subtractRemainingQuantity(task, subtask.getQuantity());
    validateRemainingQuantity(delta);

    final VPUser user = userService.getCurrentUser();

    subtask.setQuantity(delta);
    subtask.setTask(task);
    subtask.setTaskId(task.getId());
    subtask.setStatus(SubtaskStatus.IN_PROGRESS);
    subtask.setCreatedBy(user);
    subtask.setCreatedAt(ZonedDateTime.now());

    return repository.save(subtask);
  }

  @Override
  @Transactional
  public Subtask update(final Subtask subtask) {
    final Subtask current = getById(subtask.getId(), true);
    final Task task = current.getTask();

    if (current.getStatus() != SubtaskStatus.IN_PROGRESS) {
      throw new InvalidStatusException("Modification of closed task is prohibited");
    }

    validateQuantity(subtask.getQuantity());

    current.setNote(subtask.getNote());
    current.setTransportRequired(subtask.isTransportRequired());

    BigDecimal delta = subtask.getQuantity().subtract(current.getQuantity());
    if (delta.compareTo(BigDecimal.ZERO) != 0) {
      delta = taskService.subtractRemainingQuantity(task, delta);
      validateRemainingQuantity(delta);

      current.setQuantity(current.getQuantity().add(delta));
    }

    return repository.save(current);
  }

  @Override
  public Subtask complete(final Integer subtaskId) {
    final Subtask subtask = getById(subtaskId, false);
    switch (subtask.getStatus()) {
      case COMPLETED:
        return subtask;
      case IN_PROGRESS:
        final VPUser user = userService.getCurrentUser();

        subtask.setStatus(SubtaskStatus.COMPLETED);
        subtask.setClosedBy(user);
        subtask.setClosedAt(ZonedDateTime.now());

        return repository.save(subtask);
      default:
        throw new InvalidStatusException(String.format(
            "It's prohibited to change status '%s' to '%s'",
            subtask.getStatus().name(), SubtaskStatus.COMPLETED.name()));
    }
  }

  @Override
  @Transactional
  public Subtask reject(final Integer subtaskId, final boolean onlyMine) {
    final Subtask subtask = getById(subtaskId, onlyMine);
    switch (subtask.getStatus()) {
      case REJECTED:
        return subtask;
      case IN_PROGRESS:
        final VPUser user = userService.getCurrentUser();
        taskService.subtractRemainingQuantity(subtask.getTask(), subtask.getQuantity().negate());

        subtask.setStatus(SubtaskStatus.REJECTED);
        subtask.setClosedBy(user);
        subtask.setClosedAt(ZonedDateTime.now());

        return repository.save(subtask);
      default:
        throw new InvalidStatusException(String.format(
            "It's prohibited to change status '%s' to '%s'",
            subtask.getStatus().name(), SubtaskStatus.REJECTED.name()));
    }
  }

  private void validateQuantity(final BigDecimal quantity) {
    if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidQuantityException("quantity must be greater then 0");
    }
  }

  private void validateRemainingQuantity(final BigDecimal delta) {
    if (delta.compareTo(BigDecimal.ZERO) == 0) {
      throw new InvalidQuantityException("There is no remaining quantity for selected task");
    }
  }

}
