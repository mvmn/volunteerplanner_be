package com.volunteer.api.controller;

import com.volunteer.api.data.mapping.GenericPageDtoMapper;
import com.volunteer.api.data.mapping.StoreDtoMapper;
import com.volunteer.api.data.model.UserAuthority;
import com.volunteer.api.data.model.api.GenericCollectionDtoV1;
import com.volunteer.api.data.model.api.GenericPageDtoV1;
import com.volunteer.api.data.model.api.StoreDtoV1;
import com.volunteer.api.data.model.api.search.SearchDto;
import com.volunteer.api.data.model.api.search.filter.FilterDto;
import com.volunteer.api.data.model.persistence.Store;
import com.volunteer.api.data.repository.search.impl.StoreQueryBuilder;
import com.volunteer.api.utils.AuthenticationUtils;
import com.volunteer.api.service.StoreService;
import java.util.Collection;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
@RequestMapping(path = "/api/v1/stores", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class StoreControllerV1 {

  private final StoreService service;
  private final StoreDtoMapper mapper;

  private final ObjectFactory<StoreQueryBuilder> queryBuilderFactory;

  @PreAuthorize("hasAuthority('STORES_VIEW_PUBLIC') or hasAuthority('STORES_VIEW_CONFIDENTIAL')")
  @GetMapping(path = "/{store-id}")
  @ResponseStatus(HttpStatus.OK)
  public StoreDtoV1 getById(@PathVariable("store-id") final Integer id,
      final Authentication authentication) {
    final boolean showConfidential = AuthenticationUtils.hasAuthority(
        UserAuthority.STORES_VIEW_CONFIDENTIAL, authentication);

    return mapper.map(service.get(id, showConfidential));
  }

  @PreAuthorize("hasAuthority('STORES_VIEW_CONFIDENTIAL')")
  @PostMapping(path = "/search")
  @ResponseStatus(HttpStatus.OK)
  public GenericPageDtoV1<StoreDtoV1> search(@RequestBody @Valid final SearchDto<FilterDto> body) {
    final Page<Store> result = service.getAll(queryBuilderFactory.getObject()
        .withPageNum(body.getPage())
        .withPageSize(body.getPageSize())
        .withFilter(body.getFilter())
        .withSort(body.getSort())
    );

    return GenericPageDtoMapper.map(body.getPage(), body.getPageSize(), result,
        mapper::map);
  }

  @PreAuthorize("hasAuthority('STORES_VIEW_CONFIDENTIAL')")
  @GetMapping(path = "/search")
  @ResponseStatus(HttpStatus.OK)
  public GenericCollectionDtoV1<StoreDtoV1> search(@RequestParam("city.id") final Integer cityId) {
    final Collection<StoreDtoV1> result = mapper.map(service.getByCityId(cityId));

    return GenericCollectionDtoV1.<StoreDtoV1>builder()
        .items(result)
        .build();
  }

  @PreAuthorize("hasAuthority('STORES_MODIFY')")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public StoreDtoV1 create(@RequestBody @Valid final StoreDtoV1 store) {
    return mapper.map(service.create(mapper.map(store)));
  }

  @PreAuthorize("hasAuthority('STORES_MODIFY')")
  @PutMapping(path = "/{store-id}")
  @ResponseStatus(HttpStatus.OK)
  public StoreDtoV1 update(@PathVariable("store-id") final Integer id,
      @RequestBody @Valid final StoreDtoV1 store) {
    final Store entity = mapper.map(store);
    entity.setId(id);

    return mapper.map(service.update(entity));
  }

  @PreAuthorize("hasAuthority('STORES_MODIFY')")
  @DeleteMapping(path = "/{store-id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("store-id") final Integer id) {
    service.delete(id);
  }

}
