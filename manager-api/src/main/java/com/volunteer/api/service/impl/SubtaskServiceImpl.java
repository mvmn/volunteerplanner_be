package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.SubtaskStatus;
import com.volunteer.api.data.model.persistence.Subtask;
import com.volunteer.api.data.model.persistence.Task;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.repository.ProductRepository;
import com.volunteer.api.data.repository.SubtaskRepository;
import com.volunteer.api.data.repository.TaskRepository;
import com.volunteer.api.data.repository.UserRepository;
import com.volunteer.api.error.InvalidQuantityException;
import com.volunteer.api.error.InvalidStatusException;
import com.volunteer.api.error.ObjectNotFoundException;
import com.volunteer.api.service.SubtaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class SubtaskServiceImpl implements SubtaskService {
  private final SubtaskRepository subtaskRepository;
  private final TaskRepository taskRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;

  @Override
  public Subtask findBySubtaskId(Integer subtaskId) {
    return subtaskRepository
        .findById(subtaskId)
        .orElseThrow(
            () -> new ObjectNotFoundException("Subtask with id " + subtaskId + " not found"));
  }

  @Override
  public Collection<Subtask> findByTaskId(Integer taskId) {
    return subtaskRepository.findByTaskId(taskId);
  }

  @Override
  public Collection<Subtask> findByProductId(Integer productId) {
    return subtaskRepository.findByProductId(productId);
  }

  @Override
  public Collection<Subtask> findByVolunteerId(Integer volunteerId) {
    return subtaskRepository.findByVolunteerId(volunteerId);
  }

  @Override
  public Collection<Subtask> findByVolunteerPrincipal(String principal) {
    VPUser user = userRepository.findByUserName(principal);
    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }
    return subtaskRepository.findByVolunteerId(user.getId());
  }

  @Override
  @Transactional
  public Subtask createSubtask(Subtask subtask) {
    // Update task.QuantityLeft
    Task task = taskRepository.getById(subtask.getTask().getId());
    task.setQuantityLeft(task.getQuantityLeft().subtract(subtask.getQuantity()));
    if (task.getQuantityLeft().compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidQuantityException("Remaining quantity left should not be less than 0");
    }
    task = taskRepository.save(task);

    // Create subtask
    subtask.setId(null);
    subtask.setStatus(SubtaskStatus.IN_PROGRESS);
    subtask.setProduct(productRepository.getById(subtask.getProduct().getId()));
    subtask.setTask(task);
    subtask.setVolunteer(userRepository.getById(subtask.getVolunteer().getId()));

    return subtaskRepository.save(subtask);
  }

  @Override
  public void complete(Integer subtaskId) {
    Subtask subtask = findBySubtaskId(subtaskId);
    if (subtask.getStatus() == SubtaskStatus.REJECTED) {
      throw new InvalidQuantityException("Subtask has invalid status: " + subtask.getStatus());
    }
    subtask.setStatus(SubtaskStatus.COMPLETED);

    subtaskRepository.save(subtask);
  }

  @Override
  @Transactional
  public void reject(Integer subtaskId) {
    // Update subtask
    Subtask subtask = findBySubtaskId(subtaskId);
    if (subtask.getStatus() == SubtaskStatus.COMPLETED) {
      throw new InvalidStatusException("Subtask has invalid status: " + subtask.getStatus());
    }
    subtask.setStatus(SubtaskStatus.REJECTED);

    Subtask result = subtaskRepository.save(subtask);

    // Update task.QuantityLeft
    Task task = taskRepository.getById(result.getTask().getId());
    task.setQuantityLeft(task.getQuantityLeft().add(subtask.getQuantity()));
    taskRepository.save(task);
  }
}
