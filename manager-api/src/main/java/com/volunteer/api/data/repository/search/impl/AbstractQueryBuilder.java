package com.volunteer.api.data.repository.search.impl;

import com.volunteer.api.data.model.api.search.filter.FilterDto;
import com.volunteer.api.data.model.api.search.filter.OperatorFilterDto;
import com.volunteer.api.data.model.api.search.filter.ValueBoolFilterDto;
import com.volunteer.api.data.model.api.search.filter.ValueFilterDto;
import com.volunteer.api.data.model.api.search.filter.ValueNumericFilterDto;
import com.volunteer.api.data.model.api.search.filter.ValueTextFilterDto;
import com.volunteer.api.data.model.api.search.sort.SortParameters;
import com.volunteer.api.data.repository.search.Query;
import com.volunteer.api.data.repository.search.QueryBuilder;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public abstract class AbstractQueryBuilder<T> implements QueryBuilder<T> {

  private int pageNum = 1;
  private int pageSize = 10;

  private FilterDto filter;
  private SortParameters sort;

  @Override
  public QueryBuilder<T> withPageNum(final Integer pageNum) {
    if (Objects.nonNull(pageNum)) {
      this.pageNum = pageNum;
    }

    return this;
  }

  @Override
  public QueryBuilder<T> withPageSize(final Integer pageSize) {
    if (Objects.nonNull(pageSize)) {
      this.pageSize = pageSize;
    }

    return this;
  }

  @Override
  public QueryBuilder<T> withFilter(final FilterDto filter) {
    this.filter = filter;
    return this;
  }

  @Override
  public QueryBuilder<T> withSort(final SortParameters sort) {
    this.sort = sort;
    return this;
  }

  @Override
  public Query<T> build() {
    return new Query<>(buildSpecification(), buildPageable());
  }

  protected Specification<T> buildFilterSpecification(final ValueBoolFilterDto source) {
    return null;
  }

  protected Specification<T> buildFilterSpecification(final ValueNumericFilterDto source) {
    return null;
  }

  protected Specification<T> buildFilterSpecification(final ValueTextFilterDto source) {
    return null;
  }

  protected abstract Sort buildSort(final SortParameters sort);

  private Specification<T> buildSpecification() {
    if (Objects.isNull(filter)) {
      return null;
    }

    return buildSpecification(filter);
  }

  private Specification<T> buildSpecification(final FilterDto source) {
    if (source instanceof OperatorFilterDto) {
      return buildFilterSpecification((OperatorFilterDto) source);
    }

    if (source instanceof ValueFilterDto) {
      return buildFilterSpecification((ValueFilterDto) source);
    }

    throw new IllegalArgumentException(String.format(
        "Filter type '%s' is not yet supported", source.getClass().getCanonicalName()));
  }

  private Specification<T> buildFilterSpecification(final ValueFilterDto source) {
    if (source instanceof ValueBoolFilterDto) {
      return buildFilterSpecification((ValueBoolFilterDto) source);
    }

    if (source instanceof ValueNumericFilterDto) {
      return buildFilterSpecification((ValueNumericFilterDto) source);
    }

    if (source instanceof ValueTextFilterDto) {
      return buildFilterSpecification((ValueTextFilterDto) source);
    }

    throw new IllegalArgumentException(String.format(
        "Filter type '%s' is not yet supported", source.getClass().getCanonicalName()));
  }

  private Specification<T> buildFilterSpecification(final OperatorFilterDto source) {
    final List<FilterDto> operands = source.getOperands();

    // nothing to combine here
    if (operands.size() == 1) {
      return buildSpecification(operands.get(0));
    }

    Specification<T> result = null;
    for (final FilterDto operand : operands) {
      if (Objects.isNull(result)) {
        result = Specification.where(buildSpecification(operand));
        continue;
      }

      switch (source.getOperator()) {
        case OR:
          result = result.or(buildSpecification(operand));
          break;
        case AND:
          result = result.and(buildSpecification(operand));
          break;
        default:
          throw new IllegalArgumentException(String.format("Operator '%s' is not supported",
              source.getOperator()));
      }
    }

    return result;
  }

  private Pageable buildPageable() {
    return PageRequest.of(pageNum, pageSize, buildSort(sort));
  }

}
