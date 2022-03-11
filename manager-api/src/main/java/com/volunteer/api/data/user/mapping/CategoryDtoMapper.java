package com.volunteer.api.data.user.mapping;

import java.util.Collection;
import org.mapstruct.Mapper;
import com.volunteer.api.data.user.model.api.CategoryDtoV1;
import com.volunteer.api.data.user.model.dto.Category;

@Mapper(componentModel = "spring")
public interface CategoryDtoMapper {

  public Collection<CategoryDtoV1> map(Collection<Category> categories);

  CategoryDtoV1 map(Category category);

  Category map(CategoryDtoV1 categoryDto);
}
