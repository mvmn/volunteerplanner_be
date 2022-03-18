package com.volunteer.api.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.volunteer.api.data.model.persistence.Product;

import java.util.Collection;

public interface ProductRepository extends JpaRepository<Product, Integer> {
  Collection<Product> findByNameLikeAndCategoryId(String string, Integer categoryId);
}
