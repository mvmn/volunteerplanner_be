package com.volunteer.api.data.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.volunteer.api.data.model.persistence.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

  List<Category> findAllByParentId(Integer parentId);

  List<Category> findAllByParentIdNull();

  List<Category> findAllByNameLike(String name);

  List<Category> findAllByParentIdNotNull();
}
