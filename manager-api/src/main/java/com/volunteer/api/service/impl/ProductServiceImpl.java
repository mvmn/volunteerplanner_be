package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.persistence.Product;
import com.volunteer.api.data.model.persistence.specifications.ProductSearchSpecifications;
import com.volunteer.api.data.repository.ProductRepository;
import com.volunteer.api.data.repository.search.Query;
import com.volunteer.api.data.repository.search.QueryBuilder;
import com.volunteer.api.error.ObjectNotFoundException;
import com.volunteer.api.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements com.volunteer.api.service.ProductService {

  private final ProductRepository repository;
  private final CategoryService categoryService;

  @Override
  public Page<Product> getAll(final QueryBuilder<Product> queryBuilder) {
    final Query<Product> query = queryBuilder.build();
    return repository.findAll(query.getSpecification(), query.getPageable());
  }

  @Override
  public Product get(final Integer id) {
    return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
        String.format("Product with id '%d' not found", id)));
  }

  @Override
  public List<Product> getByCategoryId(final Integer categoryId) {
    return repository.findAll(ProductSearchSpecifications.byCategoryId(categoryId),
        Sort.by(Order.asc("name")));
  }

  @Override
  public Product create(final Product product) {
    product.setId(null);
    product.setCategory(categoryService.get(product.getCategory().getId()));

    return repository.save(product);
  }

  @Override
  public Product update(final Product product) {
    final Product current = get(product.getId());

    current.setName(product.getName());
    current.setCategory(categoryService.get(product.getCategory().getId()));
    current.setNote(product.getNote());

    return repository.save(current);
  }

  @Override
  public void delete(final Integer id) {
    repository.deleteById(id);
  }

}
