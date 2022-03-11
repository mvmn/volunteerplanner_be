package com.volunteer.api.data.user.mapping;

import java.util.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.volunteer.api.data.user.model.api.CategoryDtoV1;
import com.volunteer.api.data.user.model.persistence.Category;

@Mapper(componentModel = "spring")
public interface CategoryDtoMapper {

  Collection<CategoryDtoV1> map(Collection<Category> categories);

  CategoryDtoV1 map(Category category);

  @Mapping(target = "path", ignore = true)
  Category map(CategoryDtoV1 categoryDto);
}
