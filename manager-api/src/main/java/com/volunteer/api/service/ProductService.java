package com.volunteer.api.service;

import com.volunteer.api.data.model.persistence.Product;
import com.volunteer.api.data.repository.search.QueryBuilder;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProductService {

  Page<Product> getAll(final QueryBuilder<Product> queryBuilder);

  Product get(final Integer id);

  List<Product> getByCategoryId(final Integer categoryId);

  Product create(final Product product);

  Product update(final Product product);

  void delete(final Integer id);

}
