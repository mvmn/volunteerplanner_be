package com.volunteer.api.data.repository.search;

import com.volunteer.api.data.model.api.search.filter.FilterDto;
import com.volunteer.api.data.model.api.search.sort.SortParameters;

public interface QueryBuilder<T> {

  QueryBuilder<T> withPageNum(final Integer pageNum);

  QueryBuilder<T> withPageSize(final Integer pageSize);

  QueryBuilder<T> withFilter(final FilterDto filter);

  QueryBuilder<T> withSort(final SortParameters sort);

  Query<T> build();

}
