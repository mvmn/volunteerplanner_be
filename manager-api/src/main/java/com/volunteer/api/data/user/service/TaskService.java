package com.volunteer.api.data.user.service;

import java.util.Optional;
import com.volunteer.api.data.user.model.persistence.Task;

public interface TaskService {

  Task createTask(Task task);

  Optional<Task> getTaskById(int taskId);

  void verify(int taskId);

  void reject(int taskId);

  void complete(int taskId);

}
