package com.volunteer.api.service;

import com.volunteer.api.data.model.persistence.Address;
import com.volunteer.api.data.model.persistence.City;
import com.volunteer.api.data.model.persistence.Region;
import java.util.List;

public interface AddressService {

  List<Region> getAllRegions();

  Region getRegionById(final Integer regionId);

  List<City> getAllRegionCities(final Integer regionId);

  City getCityById(final Integer cityId);

  Address getOrCreate(final Address address);

}
