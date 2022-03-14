package com.volunteer.api.service;

import com.volunteer.api.data.model.persistence.Subtask;

import java.util.Collection;
import java.util.Optional;

public interface SubtaskService {
    Subtask findBySubtaskId(Integer subtaskId);

    Collection<Subtask> findByTaskId(Integer taskId);

    Collection<Subtask> findByProductId(Integer productId);

    Collection<Subtask> findByVolunteerId(Integer volunteerId);

    Collection<Subtask> findByVolunteerPrincipal(String principal);

    Subtask createSubtask(Subtask subtask);

    void complete(Integer subtaskId);

    void reject(Integer subtaskId);
}
