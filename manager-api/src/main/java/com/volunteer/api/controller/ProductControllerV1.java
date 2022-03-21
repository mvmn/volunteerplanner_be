package com.volunteer.api.controller;

import com.volunteer.api.data.mapping.GenericPageDtoMapper;
import com.volunteer.api.data.mapping.ProductDtoMapper;
import com.volunteer.api.data.model.api.GenericPageDtoV1;
import com.volunteer.api.data.model.api.ProductDtoV1;
import com.volunteer.api.data.model.api.search.SearchDto;
import com.volunteer.api.data.model.api.search.filter.FilterDto;
import com.volunteer.api.data.model.persistence.Product;
import com.volunteer.api.data.repository.search.impl.ProductQueryBuilder;
import com.volunteer.api.service.ProductService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(path = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProductControllerV1 {

  private final ProductService service;
  private final ProductDtoMapper productDtoMapper;

  @PreAuthorize("hasAuthority('PRODUCTS_VIEW')")
  @GetMapping("/{product-id}")
  @ResponseStatus(HttpStatus.OK)
  public ProductDtoV1 getById(@PathVariable("product-id") final Integer id) {
    return productDtoMapper.map(service.getById(id));
  }

  // filter by category and / or name
  // could be used for autosuggestion
  @PreAuthorize("hasAuthority('PRODUCTS_VIEW')")
  @PostMapping("/search")
  public GenericPageDtoV1<ProductDtoV1> getAll(@RequestBody @Valid final SearchDto<FilterDto> body,
      final ProductQueryBuilder queryBuilder) {
    final Page<Product> result = service.getAll(queryBuilder
        .withPageNum(body.getPage())
        .withPageSize(body.getPageSize())
        .withFilter(body.getFilter())
        .withSort(body.getSort())
    );

    return GenericPageDtoMapper.map(body.getPage(), body.getPageSize(), result,
        productDtoMapper::map);
  }

  @PreAuthorize("hasAuthority('PRODUCTS_MODIFY')")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductDtoV1 create(@RequestBody @Valid ProductDtoV1 product) {
    return productDtoMapper.map(service.create(productDtoMapper.map(product)));
  }

  @PreAuthorize("hasAuthority('PRODUCTS_MODIFY')")
  @PutMapping("/{product-id}")
  @ResponseStatus(HttpStatus.OK)
  public ProductDtoV1 update(@PathVariable("product-id") final Integer id,
      @RequestBody @Valid final ProductDtoV1 product) {
    final Product entity = productDtoMapper.map(product);
    entity.setId(id);

    return productDtoMapper.map(service.update(entity));
  }

  @PreAuthorize("hasAuthority('PRODUCTS_MODIFY')")
  @DeleteMapping("/{product-id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("product-id") final Integer id) {
    service.delete(id);
  }

}
