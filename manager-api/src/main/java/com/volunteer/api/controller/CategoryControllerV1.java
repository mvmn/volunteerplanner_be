package com.volunteer.api.controller;

import com.volunteer.api.data.mapping.CategoryDtoMapper;
import com.volunteer.api.data.model.api.CategoryDtoV1;
import com.volunteer.api.data.model.api.GenericCollectionDtoV1;
import com.volunteer.api.data.model.persistence.Category;
import com.volunteer.api.service.CategoryService;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(path = "/api/v1/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CategoryControllerV1 {

  private final CategoryService categoryService;
  private final CategoryDtoMapper categoryDtoMapper;

  @PreAuthorize("hasAuthority('CATEGORIES_VIEW')")
  @GetMapping(path = "/{category-id}")
  @ResponseStatus(HttpStatus.OK)
  public CategoryDtoV1 getById(@PathVariable("category-id") final Integer categoryId) {
    return categoryDtoMapper.map(categoryService.get(categoryId));
  }

  @PreAuthorize("hasAuthority('CATEGORIES_VIEW')")
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public GenericCollectionDtoV1<CategoryDtoV1> getAll() {
    return GenericCollectionDtoV1.<CategoryDtoV1>builder()
        .items(categoryDtoMapper.map(categoryService.getAll()))
        .build();
  }

  @PreAuthorize("hasAuthority('CATEGORIES_VIEW')")
  @GetMapping(path = "/roots")
  @ResponseStatus(HttpStatus.OK)
  public GenericCollectionDtoV1<CategoryDtoV1> getRoots() {
    return GenericCollectionDtoV1.<CategoryDtoV1>builder()
        .items(categoryDtoMapper.map(categoryService.getRoots()))
        .build();
  }
  
  @PreAuthorize("hasAuthority('CATEGORIES_VIEW')")
  @GetMapping(path = "/subcategories")
  @ResponseStatus(HttpStatus.OK)
  public GenericCollectionDtoV1<CategoryDtoV1> getAllSubcategories() {
    return GenericCollectionDtoV1.<CategoryDtoV1>builder()
        .items(categoryDtoMapper.map(categoryService.getAllSubcategories()))
        .build();
  }
  
  @PreAuthorize("hasAuthority('CATEGORIES_VIEW')")
  @GetMapping(path = "/{parent-id}/children")
  @ResponseStatus(HttpStatus.OK)
  public GenericCollectionDtoV1<CategoryDtoV1> getChildren(
      @PathVariable("parent-id") final Integer parentId) {
    return GenericCollectionDtoV1.<CategoryDtoV1>builder()
        .items(categoryDtoMapper.map(categoryService.getByParentId(parentId)))
        .build();
  }

  @PreAuthorize("hasAuthority('CATEGORIES_VIEW')")
  @GetMapping(path = "/search")
  @ResponseStatus(HttpStatus.OK)
  public GenericCollectionDtoV1<CategoryDtoV1> search(
      @NotBlank @RequestParam("name") final String name) {
    return GenericCollectionDtoV1.<CategoryDtoV1>builder()
        .items(categoryDtoMapper.map(categoryService.getByName(name)))
        .build();
  }

  @PreAuthorize("hasAuthority('CATEGORIES_MODIFY')")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CategoryDtoV1 create(@RequestBody @Valid final CategoryDtoV1 category) {
    return categoryDtoMapper.map(categoryService.create(categoryDtoMapper.map(category)));
  }

  @PreAuthorize("hasAuthority('CATEGORIES_MODIFY')")
  @PutMapping(path = "/{category-id}")
  @ResponseStatus(HttpStatus.OK)
  public CategoryDtoV1 update(@PathVariable("category-id") final Integer categoryId,
      @RequestBody @Valid final CategoryDtoV1 category) {
    final Category entity = categoryDtoMapper.map(category);
    entity.setId(categoryId);

    return categoryDtoMapper.map(categoryService.update(entity));
  }

  @PreAuthorize("hasAuthority('CATEGORIES_MODIFY')")
  @DeleteMapping(path = "/{category-id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("category-id") final Integer categoryId) {
    categoryService.delete(categoryId);
  }

}
