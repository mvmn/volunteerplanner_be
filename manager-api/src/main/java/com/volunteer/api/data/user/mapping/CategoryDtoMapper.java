package com.volunteer.api.data.user.mapping;

import com.volunteer.api.data.user.model.api.CategoryDtoV1;
import com.volunteer.api.data.user.model.dto.Category;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoryDtoMapper {

    public static Collection<CategoryDtoV1> map(Collection<Category> categories) {
        return categories.stream().map(CategoryDtoMapper::map).collect(Collectors.toList());
    }

    public static CategoryDtoV1 map(Category category) {
        return CategoryDtoV1.builder()
                .id(category.getId())
                .name(category.getName())
                .note(category.getNote())
                .path(category.getPath())
                .parentCategory(Optional.ofNullable(category.getParentCategory())
                        .map(CategoryDtoMapper::map)
                        .orElse(null))
                .build();
    }

    public static Category map(CategoryDtoV1 categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .note(categoryDto.getNote())
                .path(categoryDto.getPath())
                .parentCategory(Optional.ofNullable(categoryDto.getParentCategory())
                        .map(CategoryDtoMapper::map)
                        .orElse(null))
                .build();
    }
}
