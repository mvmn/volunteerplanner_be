package com.volunteer.api.data.user.repository;

import com.volunteer.api.data.user.model.persistence.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface StoreRepository extends JpaRepository<Store, Integer> {
  Collection<Store> findAllByNameLike(String name);
}
