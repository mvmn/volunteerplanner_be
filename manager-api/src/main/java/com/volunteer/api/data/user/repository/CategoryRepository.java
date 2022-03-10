package com.volunteer.api.data.user.repository;

import com.volunteer.api.data.user.model.dto.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findAllByParentId(Integer parentId);
    List<Category> findAllByParentIdNull();
    List<Category> findAllByParentIdNotNull();
    List<Category> findAllByNameLike(String name);
}
