package com.volunteer.api.controller;

import com.volunteer.api.data.mapping.ProductDtoMapper;
import com.volunteer.api.data.model.api.ProductDtoV1;
import com.volunteer.api.data.model.api.ProductSearchDtoV1;
import com.volunteer.api.data.model.persistence.Product;
import com.volunteer.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProductControllerV1 {
  private final ProductService productService;
  private final ProductDtoMapper productDtoMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductDtoV1 create(@RequestBody @Valid ProductDtoV1 product) {
    return productDtoMapper.map(productService.create(productDtoMapper.map(product)));
  }

  @PutMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority('root') or hasAuthority('operator')")
  public ProductDtoV1 update(
      @PathVariable("id") final Integer id, @RequestBody @Valid final ProductDtoV1 product) {
    Product entity = productDtoMapper.map(product);
    entity.setId(id);

    return productDtoMapper.map(productService.update(entity));
  }

  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority('root') or hasAuthority('operator')")
  public void delete(@PathVariable("id") final Integer id) {
    productService.delete(id);
  }

  @GetMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public ProductDtoV1 findById(@PathVariable("id") final Integer id) {
    return productDtoMapper.map(productService.getById(id));
  }

  @GetMapping()
  public Collection<ProductDtoV1> findAll() {
    return productDtoMapper.map(productService.getAll());
  }

  @PostMapping("search")
  public Collection<ProductDtoV1> findByNameAndCaegory(@RequestBody ProductSearchDtoV1 searchDto) {
    return productDtoMapper.map(
        productService.findByNameAndCategory(searchDto.getName(), searchDto.getCatcgoryId()));
  }
}
