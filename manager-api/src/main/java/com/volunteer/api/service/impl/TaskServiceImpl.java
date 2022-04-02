package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.TaskPriority;
import com.volunteer.api.data.model.TaskStatus;
import com.volunteer.api.data.model.persistence.Store;
import com.volunteer.api.data.model.persistence.Task;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.specifications.TaskSearchSpecifications;
import com.volunteer.api.data.repository.TaskRepository;
import com.volunteer.api.error.InvalidStatusException;
import com.volunteer.api.error.ObjectNotFoundException;
import com.volunteer.api.service.TaskService;
import com.volunteer.api.service.UserService;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

  private final TaskRepository repository;
  private final UserService userService;

  @Override
  public Page<Task> search(String customer, Integer productId, Integer volunteerStoreId,
      Integer customerStoreId, Collection<TaskStatus> statuses, Collection<Integer> categoryIds,
      Integer remainingQuantityMoreThan, boolean zeroQuantity, boolean excludeExpired,
      Integer createdByUserId, Integer verifiedByUserId, Integer closedByUserId,
      Pageable pagingAndSorting) {
    Specification<Task> searchSpec = null;

    if (customer != null) {
      searchSpec = TaskSearchSpecifications.addSpec(searchSpec,
          TaskSearchSpecifications.byCustomer(customer));
    }
    if (productId != null) {
      searchSpec = TaskSearchSpecifications.addSpec(searchSpec,
          TaskSearchSpecifications.byProductId(productId));
    }
    if (volunteerStoreId != null) {
      searchSpec = TaskSearchSpecifications.addSpec(searchSpec,
          TaskSearchSpecifications.byVolunteerStoreId(volunteerStoreId));
    }
    if (customerStoreId != null) {
      searchSpec = TaskSearchSpecifications.addSpec(searchSpec,
          TaskSearchSpecifications.byCustomerStoreId(customerStoreId));
    }
    if (statuses != null && !statuses.isEmpty()) {
      searchSpec = TaskSearchSpecifications.addSpec(searchSpec,
          TaskSearchSpecifications.byStatuses(statuses));
    }
    if (categoryIds != null && !categoryIds.isEmpty()) {
      searchSpec = TaskSearchSpecifications.addSpec(searchSpec,
          TaskSearchSpecifications.byCategories(categoryIds));
    }
    if (zeroQuantity) {
      searchSpec = TaskSearchSpecifications.addSpec(searchSpec,
          TaskSearchSpecifications.byRemainingQuantityZero());
    } else if (remainingQuantityMoreThan != null) {
      searchSpec = TaskSearchSpecifications.addSpec(searchSpec,
          TaskSearchSpecifications.byRemainingQuantityGreaterThan(remainingQuantityMoreThan));
    }
    if (excludeExpired) {
      searchSpec =
          TaskSearchSpecifications.addSpec(searchSpec, TaskSearchSpecifications.byNonExpired());
    }
    if (createdByUserId != null) {
      searchSpec = TaskSearchSpecifications.addSpec(searchSpec,
          TaskSearchSpecifications.byCreator(createdByUserId));
    }
    if (verifiedByUserId != null) {
      searchSpec = TaskSearchSpecifications.addSpec(searchSpec,
          TaskSearchSpecifications.byVerifier(verifiedByUserId));
    }
    if (closedByUserId != null) {
      searchSpec = TaskSearchSpecifications.addSpec(searchSpec,
          TaskSearchSpecifications.byCloser(closedByUserId));
    }

    return searchSpec != null ? repository.findAll(searchSpec, pagingAndSorting)
        : repository.findAll(pagingAndSorting);
  }

  @Override
  public Task get(int taskId) {
    return repository.findById(taskId).orElseThrow(() -> new ObjectNotFoundException(
        String.format("task with ID '%d' does not exist", taskId)));
  }

  @Override
  public Collection<Task> get(final Collection<Integer> taskIds) {
    if (CollectionUtils.isEmpty(taskIds)) {
      return Collections.emptyList();
    }

    return repository.findAllById(taskIds);
  }

  @Override
  public Task create(final Task task) {
    return repository.save(prepareCreate(task, userService.getCurrentUser(), ZonedDateTime.now()));
  }

  @Override
  public Task update(final Task task, final boolean onlyMine) {
    final Task current = get(task.getId());
    // while status is NEW we could allow adjustments
    if (current.getStatus() != TaskStatus.NEW) {
      throw new InvalidStatusException(String.format(
          "Modification of task with status '%s' is prohibited", current.getStatus()));
    }

    final VPUser currentUser = userService.getCurrentUser();
    if (onlyMine && !Objects.equals(task.getCreatedBy().getId(), currentUser.getId())) {
      throw new AuthorizationServiceException(String.format(
          "You are not allowed to modify task '%d'", task.getId()));
    }

    validateDeadlineDate(task.getDeadlineDate());
    validateVolunteerStore(task.getVolunteerStore());

    current.setVolunteerStore(task.getVolunteerStore());
    current.setCustomerStore(task.getCustomerStore());
    current.setProduct(task.getProduct());
    current.setQuantity(task.getQuantity());
    current.setQuantityLeft(current.getQuantity());
    current.setProductMeasure(task.getProductMeasure());
    current.setPriority(Optional.ofNullable(task.getPriority()).orElse(TaskPriority.NORMAL));
    current.setDeadlineDate(task.getDeadlineDate());
    current.setNote(task.getNote());

    return repository.save(current);
  }

  @Override
  public void delete(final Integer taskId, final boolean onlyMine) {
    final Optional<Task> current = repository.findById(taskId);
    if (current.isEmpty()) {
      return;
    }

    final Task task = current.get();
    if (task.getStatus() != TaskStatus.NEW) {
      throw new InvalidStatusException(String.format(
          "Can't delete task with status '%s'", task.getStatus()));
    }

    final VPUser currentUser = userService.getCurrentUser();
    if (onlyMine && !Objects.equals(task.getCreatedBy().getId(), currentUser.getId())) {
      throw new AuthorizationServiceException(String.format(
          "You are not allowed to delete task '%d'", task.getId()));
    }

    repository.deleteById(taskId);
  }

  @Override
  @Transactional
  public List<Task> create(final Collection<Task> tasks) {
    if (CollectionUtils.isEmpty(tasks)) {
      return Collections.emptyList();
    }

    final VPUser currentUser = userService.getCurrentUser();
    final ZonedDateTime currentTime = ZonedDateTime.now();

    final List<Task> result = tasks.stream()
        .map(task -> prepareCreate(task, currentUser, currentTime))
        .collect(Collectors.toList());

    return repository.saveAll(result);
  }

  @Override
  public void changeStatus(final Integer taskId, final TaskStatus status, final boolean onlyMine) {
    final Optional<Task> result = prepareStatusChange(taskId, status, userService.getCurrentUser(),
        ZonedDateTime.now(), onlyMine);
    result.ifPresent(repository::save);
  }

  @Override
  @Transactional
  public void changeStatus(final Collection<Integer> taskIds, final TaskStatus status,
      final boolean onlyMine) {
    if (CollectionUtils.isEmpty(taskIds)) {
      return;
    }

    final VPUser currentUser = userService.getCurrentUser();
    final ZonedDateTime currentTime = ZonedDateTime.now();

    final List<Task> result = taskIds.stream()
        .map(taskId -> prepareStatusChange(taskId, status, currentUser, currentTime, onlyMine))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());

    repository.saveAll(result);
  }

  @Override
  @Transactional
  public BigDecimal subtractRemainingQuantity(final Task task, final BigDecimal delta) {
    final int deltaSign = delta.compareTo(BigDecimal.ZERO);

    // delta is zero
    // nothing to do here
    if (deltaSign == 0) {
      return delta;
    }

    final BigDecimal result;
    switch (task.getStatus()) {
      case NEW:
        throw new InvalidStatusException("Task is not verified yet");
      case VERIFIED:
        // reject handling
        if (deltaSign < 0) {
          task.setQuantityLeft(task.getQuantity().min(task.getQuantityLeft().subtract(delta)));
          result = delta;
        } else {
          if (task.getQuantityLeft().compareTo(delta) >= 0) {
            task.setQuantityLeft(task.getQuantityLeft().subtract(delta));
            result = delta;
          } else {
            result = task.getQuantityLeft();
            task.setQuantityLeft(BigDecimal.ZERO);
          }
        }

        break;
      case COMPLETED:
        throw new InvalidStatusException("Task has been completed already");
      case REJECTED:
        // reject handling
        if (deltaSign < 0) {
          task.setQuantityLeft(task.getQuantity().min(task.getQuantityLeft().subtract(delta)));
          result = delta;
        } else {
          throw new InvalidStatusException("Task has been rejected already");
        }

        break;
      default:
        throw new InvalidStatusException(String.format("Task status '%s' is not supported",
            task.getStatus()));
    }

    repository.save(task);
    return result;
  }

  private Task prepareCreate(final Task task, final VPUser currentUser,
      final ZonedDateTime currentTime) {
    validateDeadlineDate(task.getDeadlineDate());
    validateVolunteerStore(task.getVolunteerStore());

    task.setPriority(Optional.ofNullable(task.getPriority()).orElse(TaskPriority.NORMAL));
    task.setStatus(TaskStatus.NEW);

    task.setQuantityLeft(task.getQuantity());
    task.setCreatedAt(ZonedDateTime.now());

    task.setCreatedBy(currentUser);
    task.setCreatedAt(currentTime);

    task.setSubtaskCount(0L);

    return task;
  }

  private Optional<Task> prepareStatusChange(final int taskId, final TaskStatus status,
      final VPUser currentUser, final ZonedDateTime currentTime, final Boolean onlyMine) {
    final Task task = get(taskId);
    if (task.getStatus() == status) {
      return Optional.empty();
    }

    if (onlyMine && !Objects.equals(task.getCreatedBy().getId(), currentUser.getId())) {
      throw new AuthorizationServiceException(String.format(
          "You are not allowed to change task '%d' status", taskId));
    }

    switch (status) {
      case VERIFIED:
        prepareStatusChangeVerified(task, currentUser, currentTime);
        break;
      case COMPLETED:
        prepareStatusChangeCompleted(task, currentUser, currentTime);
        break;
      case REJECTED:
        prepareStatusChangeRejected(task, currentUser, currentTime);
        break;
      default:
        throw new InvalidStatusException(String.format(
            "Unsupported status '%s' for status change operation", status));
    }

    return Optional.of(task);
  }

  private void prepareStatusChangeVerified(final Task task, final VPUser currentUser,
      final ZonedDateTime currentTime) {
    if (task.getStatus() != TaskStatus.NEW) {
      throw new IllegalStateException(String.format("Cannot verify task in status '%s'",
          task.getStatus()));
    }

    // task could be created with valid deadline date
    // but validation could be performed after.
    // In this case we won't allow mark task verification till deadline date won't be corrected
    validateDeadlineDate(task.getDeadlineDate());

    task.setStatus(TaskStatus.VERIFIED);
    task.setVerifiedBy(currentUser);
    task.setVerifiedAt(currentTime);
  }

  private void prepareStatusChangeCompleted(final Task task, final VPUser currentUser,
      final ZonedDateTime currentTime) {
    if (task.getStatus() != TaskStatus.VERIFIED) {
      throw new IllegalStateException(String.format("Cannot complete task in status '%s'",
          task.getStatus()));
    }

    task.setStatus(TaskStatus.COMPLETED);
    task.setClosedBy(currentUser);
    task.setClosedAt(currentTime);
  }

  private void prepareStatusChangeRejected(final Task task, final VPUser currentUser,
      final ZonedDateTime currentTime) {
    // Disallow rejecting completed
    if (task.getStatus() == TaskStatus.COMPLETED) {
      throw new IllegalStateException("Cannot reject task in status " + task.getStatus());
    }

    task.setStatus(TaskStatus.REJECTED);
    task.setClosedBy(currentUser);
    task.setClosedAt(currentTime);

    // consider to reject all subtasks which are in progress yet
    // later we could add SMS notification like stop working on it, task has been rejected
  }

  private void validateDeadlineDate(final ZonedDateTime date) {
    if (date.isBefore(ZonedDateTime.now())) {
      throw new IllegalArgumentException("Deadline date cannot be in the past");
    }
  }


  private void validateVolunteerStore(final Store store) {
    if (store.isConfidential()) {
      throw new IllegalArgumentException("Volunteer store must be public");
    }
  }

}
