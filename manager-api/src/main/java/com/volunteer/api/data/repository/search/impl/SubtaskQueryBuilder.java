package com.volunteer.api.data.repository.search.impl;

import com.volunteer.api.data.model.api.search.sort.SortParameters;
import com.volunteer.api.data.model.persistence.Subtask;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.specifications.SubtaskSearchSpecifications;
import com.volunteer.api.service.UserService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class SubtaskQueryBuilder extends AbstractQueryBuilder<Subtask> {

  private final UserService userService;

  private boolean showOnlyMine;

  public SubtaskQueryBuilder withShowOnlyMine(final boolean showOnlyMine) {
    this.showOnlyMine = showOnlyMine;
    return this;
  }

  @Override
  protected Specification<Subtask> buildSpecification() {
    final Specification<Subtask> result = super.buildSpecification();
    if (!showOnlyMine) {
      return result;
    }

    final VPUser currentUser = userService.getCurrentUser();
    final Specification<Subtask> currentUserSpec = SubtaskSearchSpecifications.byCreator(
        currentUser.getId());

    return Objects.isNull(result) ? currentUserSpec
        : Specification.where(currentUserSpec).and(result);
  }


  @Override
  protected Sort buildSort(SortParameters sort) {
    return null;
  }

}
