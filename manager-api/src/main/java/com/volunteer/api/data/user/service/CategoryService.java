package com.volunteer.api.data.user.service;

import com.volunteer.api.data.user.model.dto.Category;
import java.util.Collection;
import java.util.List;

public interface CategoryService {

  Collection<Category> getAll();

  Category get(final Integer id);

  Category create(final Category category);

  Category update(final Category category);

  List<Category> getRoots();

  List<Category> getByParentId(final Integer parentId);

  List<Category> getByName(final String name);

}
