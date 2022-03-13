package com.volunteer.api.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.volunteer.api.data.model.persistence.Category;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findAllByParentId(Integer parentId);
    List<Category> findAllByParentIdNull();
    List<Category> findAllByParentIdNotNull();
    List<Category> findAllByNameLike(String name);
}
