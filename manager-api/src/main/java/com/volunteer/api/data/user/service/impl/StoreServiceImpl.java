package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.user.model.persistence.Store;
import com.volunteer.api.data.user.repository.AddressRepository;
import com.volunteer.api.data.user.repository.StoreRepository;
import com.volunteer.api.data.user.service.StoreService;
import com.volunteer.api.error.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
  private final StoreRepository storeRepository;
  private final AddressRepository addressRepository;

  @Override
  public Collection<Store> getAll() {
    return storeRepository.findAll();
  }

  @Override
  public Store getById(Integer id) {
    return storeRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Store with id " + id + " not found"));
  }

  @Override
  public Collection<Store> getByName(String name) {
    name = name.replace("*", "%");
    return storeRepository.findAllByNameLikeIgnoreCase(StringUtils.defaultString(name) + "%");
  }

  @Override
  public Store create(Store store) {
    store.setId(null);
    updateAddress(store);
    return storeRepository.save(store);
  }

  @Override
  public Store update(Store store) {
    Store current = getById(store.getId());

    current.setAddress(store.getAddress());
    current.setName(store.getName());
    current.setNote(store.getNote());
    current.setContactPerson(store.getContactPerson());

    updateAddress(current);

    return storeRepository.save(current);
  }

  private void updateAddress(Store store) {
    store.setAddress(addressRepository.getById(store.getAddress().getId()));
  }
}
