package com.volunteer.api.service;

import com.volunteer.api.data.model.TaskStatus;
import com.volunteer.api.data.model.persistence.Task;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

  Task get(int taskId);

  Collection<Task> get(final Collection<Integer> taskIds);

  Page<Task> search(String customer, Integer productId, Integer volunteerStoreId,
      Integer customerStoreId, Collection<TaskStatus> statuses, Collection<Integer> categoryIds,
      Integer remainingQuantityMoreThan, boolean zeroQuantity, boolean excludeExpired,
      Integer createdByUserId, Integer verifiedByUserId, Integer closedByUserId,
      Pageable pagingAndSorting);

  //Page<Task> search(final QueryBuilder<Task> queryBuilder);

  Task create(Task task);

  List<Task> create(final Collection<Task> tasks);

  BigDecimal subtractRemainingQuantity(final Task task, final BigDecimal delta);

  void changeStatus(final Integer taskId, final TaskStatus status);

  void changeStatus(final Collection<Integer> taskIds, TaskStatus status);

}
