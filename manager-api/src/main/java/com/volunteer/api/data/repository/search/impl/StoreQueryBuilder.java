package com.volunteer.api.data.repository.search.impl;

import com.volunteer.api.data.model.api.search.filter.ValueBoolFilterDto;
import com.volunteer.api.data.model.api.search.filter.ValueNumberFilterDto;
import com.volunteer.api.data.model.api.search.filter.ValueTextFilterDto;
import com.volunteer.api.data.model.api.search.sort.SortOrder;
import com.volunteer.api.data.model.api.search.sort.SortParameters;
import com.volunteer.api.data.model.persistence.Store;
import com.volunteer.api.data.model.persistence.specifications.StoreSearchSpecifications;
import java.util.Objects;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StoreQueryBuilder extends AbstractQueryBuilder<Store> {

  @Override
  protected Specification<Store> buildFilterSpecification(final ValueBoolFilterDto source) {
    final Boolean value = source.getValue();
    switch (source.getField().toLowerCase()) {
      case "confidential":
        return StoreSearchSpecifications.byConfidential(value);
    }

    return super.buildFilterSpecification(source);
  }

  @Override
  protected Specification<Store> buildFilterSpecification(final ValueTextFilterDto source) {
    final String value = source.getValue().strip();
    if (!value.isEmpty()) {
      switch (source.getField().toLowerCase()) {
        case "name":
          return StoreSearchSpecifications.byName(value);
        case "note":
          return StoreSearchSpecifications.byNote(value);
        case "address":
          return StoreSearchSpecifications.byAddress(value);
      }
    }

    return super.buildFilterSpecification(source);
  }

  @Override
  protected Specification<Store> buildFilterSpecification(final ValueNumberFilterDto source) {
    final Number value = source.getValue();
    switch (source.getField().toLowerCase()) {
      case "region.id":
        return StoreSearchSpecifications.byRegion(value);
      case "city.id":
        return StoreSearchSpecifications.byCity(value);
    }

    return super.buildFilterSpecification(source);
  }

  @Override
  protected Sort buildSort(final SortParameters sort) {
    if (Objects.isNull(sort)) {
      return Sort.by(Order.asc("name"));
    }

    final String entityField;
    switch (sort.getField().toLowerCase()) {
      case "name":
        entityField = "name";
        break;
      case "city.name":
        entityField = "city.name";
        break;
      case "region.name":
        entityField = "city.region.name";
        break;
      case "confidential":
        entityField = "confidential";
        break;
      default:
        throw new IllegalArgumentException(String.format(
            "Sort by '%s' is not supported", sort.getField()));
    }

    return (sort.getOrder() == SortOrder.ASC)
        ? Sort.by(Order.asc(entityField))
        : Sort.by(Order.desc(entityField));
  }

}
