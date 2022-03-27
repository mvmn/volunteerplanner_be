package com.volunteer.api.data.repository;

import com.volunteer.api.data.model.persistence.Task;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

public interface TaskRepository
    extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {

  @Override
  @EntityGraph(value = "task.detail")
  List<Task> findAllById(final Iterable<Integer> ids);

  @Override
  @EntityGraph(value = "task.detail")
  Page<Task> findAll(@Nullable final Specification<Task> spec, final Pageable pageable);

}
