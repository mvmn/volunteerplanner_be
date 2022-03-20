package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.persistence.City;
import com.volunteer.api.data.model.persistence.Region;
import com.volunteer.api.data.repository.CityRepository;
import com.volunteer.api.data.repository.RegionRepository;
import com.volunteer.api.error.ObjectNotFoundException;
import com.volunteer.api.service.AddressService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

  private final RegionRepository regionRepository;
  private final CityRepository cityRepository;

  @Override
  public List<Region> getAllRegions() {
    return regionRepository.findAll(Sort.by(Order.asc("name")));
  }

  @Override
  public Region getRegionById(final Integer regionId) {
    return regionRepository.findById(regionId).orElseThrow(() -> new ObjectNotFoundException(
        String.format("Region with ID '%d' does not exist", regionId)));
  }

  @Override
  public List<City> getAllRegionCities(final Integer regionId) {
    return cityRepository.findAllByRegion(regionId);
  }

  @Override
  public City getCityById(final Integer cityId) {
    return cityRepository.findById(cityId).orElseThrow(() -> new ObjectNotFoundException(
        String.format("City with ID '%d' does not exist", cityId)));
  }

}
