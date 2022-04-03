package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.UserAuthority;
import com.volunteer.api.data.model.api.StoreDtoV1;
import com.volunteer.api.data.model.api.TaskViewDtoV1;
import com.volunteer.api.data.model.persistence.Task;
import com.volunteer.api.security.utils.AuthenticationUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.Mapper;
import org.springframework.security.core.Authentication;

@Mapper(componentModel = "spring", uses = {GenericMapper.class, UserViewDtoV1Mapper.class,
    ProductDtoMapper.class, StoreDtoV1.class})
public abstract class TaskViewDtoV1Mapper {

  public abstract TaskViewDtoV1 map(final Task task);

  public TaskViewDtoV1 map(final Task task, final Authentication authentication) {
    final TaskViewDtoV1 result = map(task);

    // process customer store
    if (result.getCustomerStore().getConfidential()) {
      if (!AuthenticationUtils.hasAuthority(UserAuthority.STORES_VIEW_CONFIDENTIAL,
          authentication)) {
        result.setCustomerStore(null);
      }
    }

    // process comments
    if (!AuthenticationUtils.hasAuthority(UserAuthority.TASKS_VERIFY, authentication)) {
      result.setVerificationComment(null);
    }

    switch (result.getStatus()) {
      case REJECTED:
        if ((!AuthenticationUtils.hasAuthority(UserAuthority.TASKS_REJECT, authentication))
            || (!AuthenticationUtils.hasAuthority(UserAuthority.TASKS_REJECT_MINE,
            authentication))) {
          result.setCloseComment(null);
        }
        break;
      case COMPLETED:
        if (!AuthenticationUtils.hasAuthority(UserAuthority.TASKS_COMPLETE, authentication)) {
          result.setCloseComment(null);
        }
        break;
    }

    return result;
  }

  public abstract Collection<TaskViewDtoV1> map(final Collection<Task> tasks);

  public Collection<TaskViewDtoV1> map(final Collection<Task> tasks,
      final Authentication authentication) {
    if (CollectionUtils.isEmpty(tasks)) {
      return Collections.emptyList();
    }

    return tasks.stream()
        .map(task -> map(task, authentication))
        .collect(Collectors.toList());
  }

}
