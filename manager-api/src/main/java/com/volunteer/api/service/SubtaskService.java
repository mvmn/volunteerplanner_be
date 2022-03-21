package com.volunteer.api.service;

import com.volunteer.api.data.model.persistence.Subtask;
import com.volunteer.api.data.repository.search.QueryBuilder;
import java.util.Collection;
import org.springframework.data.domain.Page;

public interface SubtaskService {

  Page<Subtask> getAll(final QueryBuilder<Subtask> queryBuilder);

  Subtask getById(final Integer subtaskId, final boolean onlyMine);

  Collection<Subtask> getByTaskId(final Integer taskId, final boolean onlyMine);

  Subtask create(final Subtask subtask);

  Subtask update(final Subtask subtask);

  Subtask complete(final Integer subtaskId);

  Subtask reject(final Integer subtaskId);
}
