package com.volunteer.api.data.user.service;


import com.volunteer.api.data.user.model.persistence.Store;

import java.util.Collection;

public interface StoreService {
  Collection<Store> getAll();

  Store getById(Integer id);

  Collection<Store> getByName(String name);

  Store create(Store store);

  Store update(Store store);
}
