package com.volunteer.api.controller;

import com.volunteer.api.data.mapping.CategoryDtoMapper;
import com.volunteer.api.data.model.api.CategoryDtoV1;
import com.volunteer.api.data.model.persistence.Category;
import com.volunteer.api.service.CategoryService;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CategoryControllerV1 {

  private final CategoryService categoryService;
  private final CategoryDtoMapper categoryDtoMapper;

  // access for logged in user
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Collection<CategoryDtoV1> getAll() {
    return categoryDtoMapper.map(categoryService.getAll());
  }

  @GetMapping(path = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public CategoryDtoV1 getById(@PathVariable("id") final Integer categoryId) {
    return categoryDtoMapper.map(categoryService.get(categoryId));
  }

  @GetMapping(path = "/root")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Collection<CategoryDtoV1>> getRoots() {
    return ResponseEntity.ok(categoryDtoMapper.map(categoryService.getRoots()));
  }

  @GetMapping(path = "/children/{parent-id}")
  @ResponseStatus(HttpStatus.OK)
  public Collection<CategoryDtoV1> getChildren(@PathVariable("parent-id") final Integer parentId) {
    return categoryDtoMapper.map(categoryService.getByParentId(parentId));
  }

  @GetMapping(path = "/search/{name}")
  @ResponseStatus(HttpStatus.OK)
  public Collection<CategoryDtoV1> searchByName(@PathVariable("name") final String name) {
    return categoryDtoMapper.map(categoryService.getByName(name));
  }

  // access for root or operator only
  @PreAuthorize("hasAuthority('root') or hasAuthority('operator')")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CategoryDtoV1 create(@RequestBody final CategoryDtoV1 category) {
    return categoryDtoMapper.map(categoryService.create(categoryDtoMapper.map(category)));
  }

  @PreAuthorize("hasAuthority('root') or hasAuthority('operator')")
  @PutMapping(path = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public CategoryDtoV1 update(@PathVariable("id") final Integer categoryId,
      @RequestBody final CategoryDtoV1 category) {
    final Category entity = categoryDtoMapper.map(category);
    entity.setId(categoryId);

    return categoryDtoMapper.map(categoryService.update(entity));
  }

}
