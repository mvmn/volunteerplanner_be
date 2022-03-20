package com.volunteer.api.service;

import com.volunteer.api.data.model.persistence.Category;
import java.util.List;

public interface CategoryService {

  List<Category> getRoots();

  Category get(final Integer id);

  List<Category> getByParentId(final Integer parentId);

  List<Category> getByName(final String name);

  Category create(final Category category);

  Category update(final Category category);

  void delete(final Integer id);

}
