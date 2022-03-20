package com.volunteer.api.data.repository.search.impl;

import com.volunteer.api.data.model.api.search.filter.ValueNumericFilterDto;
import com.volunteer.api.data.model.api.search.filter.ValueTextFilterDto;
import com.volunteer.api.data.model.api.search.sort.SortOrder;
import com.volunteer.api.data.model.api.search.sort.SortParameters;
import com.volunteer.api.data.model.persistence.Product;
import com.volunteer.api.data.model.persistence.specifications.ProductSearchSpecifications;
import java.util.Objects;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

public class ProductQueryBuilder extends AbstractQueryBuilder<Product> {

  @Override
  protected Specification<Product> buildFilterSpecification(final ValueTextFilterDto source) {
    final String value = source.getValue().strip();
    switch (source.getField().toLowerCase()) {
      case "name":
        return ProductSearchSpecifications.byName(value);
    }

    return super.buildFilterSpecification(source);
  }

  @Override
  protected Specification<Product> buildFilterSpecification(final ValueNumericFilterDto source) {
    final Number value = source.getValue();
    switch (source.getField().toLowerCase()) {
      case "category.id":
        return ProductSearchSpecifications.byCategoryId(value);
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
      case "category.name":
        entityField = "category.name";
        break;
      case "category.parent.name":
        entityField = "category.parent.name";
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
