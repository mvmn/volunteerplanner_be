package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.user.model.dto.Category;
import com.volunteer.api.data.user.repository.CategoryRepository;
import com.volunteer.api.data.user.service.CategoryService;
import com.volunteer.api.error.ObjectNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository repository;

  @Override
  public Collection<Category> getAll() {
    return repository.findAll();
  }

  @Override
  public Category get(final Integer id) {
    return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
        String.format("Category with ID '%d' soes not exist", id)));
  }

  @Override
  @Transactional
  public Category create(final Category category) {
    final Category parent = Objects.isNull(category.getParent())
        ? null
        : get(category.getParent().getId());

    category.setParent(parent);
    category.setPath("<empty>");

    final Category result = repository.save(category);
    result.setPath(buildPath(result));

    return repository.save(result);
  }

  @Override
  @Transactional
  public Category update(final Category category) {
    final Category current = get(category.getId());

    // on update we do allow to change name or note
    current.setName(category.getName());
    current.setNote(category.getNote());

    return repository.save(current);
  }

  @Override
  public List<Category> getRoots() {
    return repository.findAllByParentIdNull();
  }

  @Override
  public List<Category> getByParentId(Integer parentId) {
    return repository.findAllByParentId(parentId);
  }

  @Override
  public List<Category> getByName(String name) {
    return repository.findAllByNameLike(StringUtils.defaultString(name) + "%");
  }

  private String buildPath(final Category category) {
    if (Objects.isNull(category.getParent())) {
      return "/" + category.getId() + "/";
    }

    return category.getParent().getPath() + category.getId() + "/";
  }

}
