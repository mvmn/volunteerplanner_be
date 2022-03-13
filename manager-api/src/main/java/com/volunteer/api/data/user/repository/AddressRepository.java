package com.volunteer.api.data.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.volunteer.api.data.user.model.persistence.Address;
import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    Optional<Address> findByRegionAndCityAndAddress(String region, String city, String address);

    @Query("select distinct a.region from Address a where a.region like ?1% order by a.region")
    List<String> findDistinctRegions(String region);

    @Query("select distinct a.city from Address a where a.city like ?1% order by a.city")
    List<String> findDistinctCities(String city);
}
