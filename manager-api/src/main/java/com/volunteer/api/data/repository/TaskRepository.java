package com.volunteer.api.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.volunteer.api.data.model.persistence.Task;

public interface TaskRepository
    extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {
}