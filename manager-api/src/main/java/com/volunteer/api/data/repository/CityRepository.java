package com.volunteer.api.data.repository;

import com.volunteer.api.data.model.persistence.City;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CityRepository extends JpaRepository<City, Integer> {

  @Query("select c from City c where c.region.id = ?1 order by c.name asc")
  List<City> findAllByRegion(final Integer regionId);

  @Query("select c from City c where c.region.name = ?1 and c.name = ?2")
  Optional<City> findByName(final String regionName, final String cityName);

}
