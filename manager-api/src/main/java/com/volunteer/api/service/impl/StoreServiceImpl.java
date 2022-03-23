package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.persistence.Store;
import com.volunteer.api.data.repository.StoreRepository;
import com.volunteer.api.data.repository.search.Query;
import com.volunteer.api.data.repository.search.QueryBuilder;
import com.volunteer.api.error.ObjectNotFoundException;
import com.volunteer.api.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements com.volunteer.api.service.StoreService {

  private final StoreRepository repository;
  private final AddressService addressService;

  @Override
  public Page<Store> getAll(final QueryBuilder<Store> queryBuilder) {
    final Query<Store> query = queryBuilder.build();
    return repository.findAll(query.getSpecification(), query.getPageable());
  }

  @Override
  public Store get(final Integer id, final boolean showConfidential) {
    final Store result = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
        String.format("Store with id '%d' does not exist", id)));

    if (result.isConfidential() && !showConfidential) {
      throw new AuthorizationServiceException(String.format("Not allowed to view store '%d'", id));
    }

    return result;
  }

  @Override
  public Store create(final Store store) {
    store.setId(null);
    store.setCity(addressService.getCityById(store.getCity().getId()));

    return repository.save(store);
  }

  @Override
  public Store update(final Store store) {
    final Store current = get(store.getId(), true);

    current.setName(store.getName());
    current.setCity(addressService.getCityById(store.getCity().getId()));
    current.setAddress(store.getAddress());
    current.setConfidential(store.isConfidential());
    current.setNote(store.getNote());

    return repository.save(current);
  }

}
