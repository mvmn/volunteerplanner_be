package com.volunteer.api.data.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.volunteer.api.data.user.model.persistence.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {

}
