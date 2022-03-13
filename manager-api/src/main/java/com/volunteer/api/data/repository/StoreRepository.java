package com.volunteer.api.data.repository;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import com.volunteer.api.data.model.persistence.Store;

public interface StoreRepository extends JpaRepository<Store, Integer> {
  Collection<Store> findAllByNameLikeIgnoreCase(String name);
}
