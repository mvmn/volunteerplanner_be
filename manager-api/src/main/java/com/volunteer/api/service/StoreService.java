package com.volunteer.api.service;


import com.volunteer.api.data.model.persistence.Store;
import com.volunteer.api.data.repository.search.QueryBuilder;
import java.util.Collection;
import org.springframework.data.domain.Page;

public interface StoreService {

  Page<Store> getAll(final QueryBuilder<Store> queryBuilder);

  Store get(final Integer id, final boolean showConfidential);

  Collection<Store> getByCityId(final Integer cityId);

  Store create(final Store store);

  Store update(final Store store);

  void delete(final Integer id);

}
