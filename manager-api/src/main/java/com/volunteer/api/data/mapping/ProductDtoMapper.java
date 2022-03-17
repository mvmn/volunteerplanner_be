package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.ProductDtoV1;
import com.volunteer.api.data.model.persistence.Product;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {
  Product map(ProductDtoV1 dto);

  ProductDtoV1 map(Product product);

  Collection<ProductDtoV1> map(Collection<Product> products);
}
