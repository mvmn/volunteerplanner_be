package com.volunteer.api.data.user.service;

import java.util.Collection;
import java.util.Optional;
import com.volunteer.api.data.user.model.persistence.Category;

public interface CategoryService {
    Collection<Category> getAll();

    Optional<Category> get(final Integer id);

    Category save(Category category);

    Collection<Category> getRootCategories();

    Collection<Category> getByParentId(Integer parentId);

    Collection<Category> getByNameStartedWith(String name);
}
