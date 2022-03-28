package com.volunteer.api.data.repository;

import com.volunteer.api.data.model.persistence.Subtask;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

public interface SubtaskRepository extends JpaRepository<Subtask, Integer>,
    JpaSpecificationExecutor<Subtask> {

  @EntityGraph(value = "subtask.detail")
  Collection<Subtask> findByTaskId(final Integer taskId);

  @EntityGraph(value = "subtask.detail")
  Collection<Subtask> findByTaskIdAndCreatedById(final Integer taskId, final Integer createdById);

  @Override
  @EntityGraph(value = "subtask.detail")
  Page<Subtask> findAll(@Nullable final Specification<Subtask> spec, final Pageable pageable);

}
