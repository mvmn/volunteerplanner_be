package com.volunteer.api.data.user.service;

import com.volunteer.api.data.user.model.dto.Store;

import java.util.Collection;
import java.util.Optional;

public interface StoreService {
  Collection<Store> getAll();

  Optional<Store> getById(Integer id);

  Collection<Store> getByName(String name);

  Store create(Store store);

  Store update(Store store);
}
