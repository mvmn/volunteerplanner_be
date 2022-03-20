package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.ProductDtoV1;
import com.volunteer.api.data.model.persistence.Product;
import java.util.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {

  @Mapping(target = "category.path", ignore = true)
  Product map(ProductDtoV1 dto);

  ProductDtoV1 map(Product product);

  Collection<ProductDtoV1> map(Collection<Product> products);
}
