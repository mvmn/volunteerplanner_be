package com.volunteer.api.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.volunteer.api.data.model.persistence.Address;
import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Query("select a from Address a where a.city.id = ?1 and a.address = ?2")
    Optional<Address> findByCityAndAddress(final Integer cityId, final String address);

}
