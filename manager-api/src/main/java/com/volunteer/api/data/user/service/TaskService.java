package com.volunteer.api.data.user.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import com.volunteer.api.data.user.model.TaskStatus;
import com.volunteer.api.data.user.model.domain.TaskDetalization;
import com.volunteer.api.data.user.model.persistence.Task;

public interface TaskService {

  Optional<Task> getTaskById(int taskId);

  List<Task> getTasksByIds(List<Integer> taskIds);

  Task createTask(Task task);

  List<Task> batchCreate(Task blueprint, List<TaskDetalization> details);

  void verify(int taskId);

  void reject(int taskId);

  void complete(int taskId);

  int batchStatusChange(Collection<Integer> taskIds, TaskStatus status);
}
