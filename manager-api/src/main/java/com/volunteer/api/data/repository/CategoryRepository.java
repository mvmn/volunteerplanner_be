package com.volunteer.api.data.repository;

import com.volunteer.api.data.model.persistence.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

  List<Category> findAllByParentId(Integer parentId);

  List<Category> findAllByParentIdNull();

  List<Category> findAllByNameLike(String name);
}
