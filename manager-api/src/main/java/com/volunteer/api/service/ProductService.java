package com.volunteer.api.service;

import com.volunteer.api.data.model.persistence.Product;

import java.util.Collection;

public interface ProductService {
  Product create(Product product);

  Product update(Product product);

  void delete(Integer id);

  Product getById(Integer id);

  Collection<Product> getAll();

  Collection<Product> findByNameAndCategory(String name, Integer categoryId);
}
