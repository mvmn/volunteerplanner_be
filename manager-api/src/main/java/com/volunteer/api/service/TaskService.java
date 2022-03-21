package com.volunteer.api.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.volunteer.api.data.model.TaskStatus;
import com.volunteer.api.data.model.domain.TaskDetalization;
import com.volunteer.api.data.model.persistence.Task;

public interface TaskService {

  Optional<Task> getTaskById(int taskId);

  List<Task> getTasksByIds(List<Integer> taskIds);

  Task createTask(Task task);

  List<Task> batchCreate(Task blueprint, List<TaskDetalization> details);

  void verify(int taskId);

  void reject(int taskId);

  void complete(int taskId);

  int batchStatusChange(Collection<Integer> taskIds, TaskStatus status);

  Page<Task> search(String customer, Integer productId, Integer volunteerStoreId,
      Integer customerStoreId, Collection<TaskStatus> statuses, Collection<Integer> categoryIds,
      Integer remainingQuantityMoreThan, boolean zeroQuantity, boolean excludeExpired,
      Integer createdByUserId, Integer verifiedByUserId, Integer closedByUserId,
      Pageable pagingAndSorting);
}
