package com.volunteer.api.data.user;

import com.volunteer.api.data.user.mapping.CategoryDtoMapper;
import com.volunteer.api.data.user.model.api.CategoryDtoV1;
import com.volunteer.api.data.user.model.persistence.Category;
import com.volunteer.api.data.user.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(path = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CategoryControllerV1 {
    private final CategoryService categoryService;
    private final CategoryDtoMapper categoryDtoMapper;

    @GetMapping
    public ResponseEntity<Collection<CategoryDtoV1>> getAll() {
        return ResponseEntity.ok(categoryDtoMapper.map(categoryService.getAll()));
    }

    @GetMapping(path = "/{category-id}")
    public ResponseEntity<CategoryDtoV1> getById(@PathVariable("category-id") final Integer categoryId) {
        Optional<Category> category = categoryService.get(categoryId);
        return category
                .map(c -> ResponseEntity.ok(categoryDtoMapper.map(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoryDtoV1> create(@RequestBody final CategoryDtoV1 category) {
        CategoryDtoV1 result = categoryDtoMapper.map(categoryService.save(categoryDtoMapper.map(category)));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping(path = "/root")
    public ResponseEntity<Collection<CategoryDtoV1>> getRootCategories() {
        return ResponseEntity.ok(categoryDtoMapper.map(categoryService.getRootCategories()));
    }

    @GetMapping(path = "/children/{parent-category-id}")
    public ResponseEntity<Collection<CategoryDtoV1>> getChildren(
            @PathVariable("parent-category-id") final Integer parentCategoryId) {
        return ResponseEntity.ok(categoryDtoMapper.map(categoryService.getByParentId(parentCategoryId)));
    }

    @GetMapping(path = "/search/{name}")
    public ResponseEntity<Collection<CategoryDtoV1>> searchByName(
            @PathVariable("name") final String name) {
        return ResponseEntity.ok(categoryDtoMapper.map(categoryService.getByNameStartedWith(name)));
    }
}
