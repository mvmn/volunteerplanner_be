package com.volunteer.api.data.user;

import com.volunteer.api.data.user.mapping.CategoryDtoMapper;
import com.volunteer.api.data.user.model.api.CategoryDtoV1;
import com.volunteer.api.data.user.model.dto.Category;
import com.volunteer.api.data.user.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(path = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryControllerV1 {
    private final CategoryService categoryService;

    @Autowired
    public CategoryControllerV1(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Collection<CategoryDtoV1>> getAll() {
        return ResponseEntity.ok(CategoryDtoMapper.map(categoryService.getAll()));
    }

    @GetMapping(path = "/{category-id}")
    public ResponseEntity<CategoryDtoV1> getById(@PathVariable("category-id") final Integer categoryId) {
        Optional<Category> category = categoryService.get(categoryId);
        return category
                .map(c -> ResponseEntity.ok(CategoryDtoMapper.map(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoryDtoV1> create(@RequestBody final CategoryDtoV1 category) {
        CategoryDtoV1 result = CategoryDtoMapper.map(categoryService.save(CategoryDtoMapper.map(category)));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping(path = "/root")
    public ResponseEntity<Collection<CategoryDtoV1>> getRootCategories() {
        return ResponseEntity.ok(CategoryDtoMapper.map(categoryService.getRootCategories()));
    }

    @GetMapping(path = "/children/{parent-category-id}")
    public ResponseEntity<Collection<CategoryDtoV1>> getChildren(
            @PathVariable("parent-category-id") final Integer parentCategoryId) {
        return ResponseEntity.ok(CategoryDtoMapper.map(categoryService.getByParentId(parentCategoryId)));
    }

    @GetMapping(path = "/search/{name}")
    public ResponseEntity<Collection<CategoryDtoV1>> searchByName(
            @PathVariable("name") final String name) {
        return ResponseEntity.ok(CategoryDtoMapper.map(categoryService.getByNameStartedWith(name)));
    }
}
