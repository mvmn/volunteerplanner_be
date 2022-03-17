package com.volunteer.api.service;

import java.util.Collection;
import com.volunteer.api.data.model.persistence.Subtask;

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
