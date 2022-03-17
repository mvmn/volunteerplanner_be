package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.persistence.Product;
import com.volunteer.api.data.repository.CategoryRepository;
import com.volunteer.api.data.repository.ProductRepository;
import com.volunteer.api.error.ObjectNotFoundException;
import com.volunteer.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Override
  public Product create(Product product) {
    product.setId(null);
    return productRepository.save(product);
  }

  @Override
  public Product update(Product product) {
    Product current = getById(product.getId());

    current.setName(product.getName());
    current.setCategory(product.getCategory());
    current.setNote(product.getNote());

    updateCategory(current);

    return productRepository.save(current);
  }

  @Override
  public void delete(Integer id) {
    productRepository.deleteById(id);
  }

  @Override
  public Collection<Product> getAll() {
    return productRepository.findAll();
  }

  @Override
  public Product getById(Integer id) {
    return productRepository
        .findById(id)
        .orElseThrow(() -> new ObjectNotFoundException("Product with id " + id + " not found"));
  }

  @Override
  public Collection<Product> findByNameAndCategory(String name, Integer categoryId) {
    name = name.replace("*", "%") + "%";

    return productRepository.findByNameLikeAndCategoryId(name, categoryId);
  }

  private void updateCategory(Product product) {
    product.setCategory(categoryRepository.getById(product.getCategory().getId()));
  }
}
