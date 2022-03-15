package com.volunteer.api.data.repository;

import com.volunteer.api.data.model.SubtaskStatus;
import com.volunteer.api.data.model.persistence.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface SubtaskRepository extends JpaRepository<Subtask, Integer> {
  Collection<Subtask> findByProductId(Integer productId);

  Collection<Subtask> findByStatus(SubtaskStatus status);

  Collection<Subtask> findByTransportRequired(boolean transportRequired);

  Collection<Subtask> findByVolunteerId(Integer volunteerId);

  Collection<Subtask> findByTaskId(Integer taskId);
}
