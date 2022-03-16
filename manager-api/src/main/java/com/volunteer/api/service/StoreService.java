package com.volunteer.api.service;


import java.util.Collection;
import com.volunteer.api.data.model.persistence.Store;

public interface StoreService {
  Collection<Store> getAll();

  Store getById(Integer id);

  Collection<Store> getByName(String name);

  Store create(Store store);

  Store update(Store store);
}
