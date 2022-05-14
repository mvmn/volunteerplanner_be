package com.volunteer.api.data.repository.search.impl;

import com.volunteer.api.data.model.api.search.filter.ValueNumberFilterDto;
import com.volunteer.api.data.model.api.search.filter.ValueTextFilterDto;
import com.volunteer.api.data.model.api.search.sort.SortOrder;
import com.volunteer.api.data.model.api.search.sort.SortParameters;
import com.volunteer.api.data.model.persistence.Subtask;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.specifications.SubtaskSearchSpecifications;
import com.volunteer.api.service.UserService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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

    final Specification<Subtask> currentUserSpec = buildCreatedByMeSpecification();
    return Objects.isNull(result)
        ? currentUserSpec
        : Specification.where(currentUserSpec).and(result);
  }

  @Override
  protected Specification<Subtask> buildFilterSpecification(final ValueTextFilterDto source) {
    final String value = source.getValue().strip();
    switch (source.getField().toLowerCase()) {
      case "status":
        return SubtaskSearchSpecifications.byStatus(value);
      case "categoryPath":
        return SubtaskSearchSpecifications.byCategoryPath(value);
    }

    return super.buildFilterSpecification(source);
  }

  @Override
  protected Specification<Subtask> buildFilterSpecification(final ValueNumberFilterDto source) {
    final Number value = source.getValue();
    switch (source.getField().toLowerCase()) {
      case "createdby.id":
        return SubtaskSearchSpecifications.byCreator(value);
      case "closedby.id":
        return SubtaskSearchSpecifications.byCloser(value);
      case "task.id":
        return SubtaskSearchSpecifications.byTask(value);
      case "task.product.id":
        return SubtaskSearchSpecifications.byProduct(value);
      case "task.product.category.id":
        return SubtaskSearchSpecifications.byCategory(value);
    }

    return super.buildFilterSpecification(source);
  }

  @Override
  protected Sort buildSort(final SortParameters sort) {
    if (Objects.isNull(sort)) {
      return Sort.by(Order.asc("task.deadlineDate"));
    }

    final String entityField;
    switch (sort.getField().toLowerCase()) {
      case "status":
        entityField = "status";
        break;
      case "task.deadlinedate":
        entityField = "task.deadlineDate";
        break;
      case "task.priority":
        entityField = "task.priority";
        break;
      case "task.product.name":
        entityField = "task.product.name";
        break;
      default:
        throw new IllegalArgumentException(String.format(
            "Sort by '%s' is not supported", sort.getField()));
    }

    return (sort.getOrder() == SortOrder.ASC)
        ? Sort.by(Order.asc(entityField))
        : Sort.by(Order.desc(entityField));
  }

  private Specification<Subtask> buildCreatedByMeSpecification() {
    final VPUser currentUser = userService.getCurrentUser();
    return SubtaskSearchSpecifications.byCreator(currentUser.getId());
  }

}
