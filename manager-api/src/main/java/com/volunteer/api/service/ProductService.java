package com.volunteer.api.service;

import com.volunteer.api.data.model.persistence.Product;
import com.volunteer.api.data.repository.search.QueryBuilder;
import org.springframework.data.domain.Page;

public interface ProductService {

  Page<Product> getAll(final QueryBuilder<Product> queryBuilder);

  Product getById(final Integer id);

  Product create(final Product product);

  Product update(final Product product);

  void delete(final Integer id);

}
