package com.volunteer.api.data.repository;

import com.volunteer.api.data.model.persistence.Subtask;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubtaskRepository extends JpaRepository<Subtask, Integer>,
    JpaSpecificationExecutor<Subtask> {

  Collection<Subtask> findByTaskId(final Integer taskId);

  Collection<Subtask> findByTaskIdAndCreatedById(final Integer taskId, final Integer createdById);

}
