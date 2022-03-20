package com.volunteer.api.data.repository;

import com.volunteer.api.data.model.persistence.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StoreRepository extends JpaRepository<Store, Integer>,
    JpaSpecificationExecutor<Store> {

}
