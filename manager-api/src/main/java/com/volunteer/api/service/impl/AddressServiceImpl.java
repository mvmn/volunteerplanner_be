package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.persistence.Address;
import com.volunteer.api.data.model.persistence.City;
import com.volunteer.api.data.model.persistence.Region;
import com.volunteer.api.data.repository.AddressRepository;
import com.volunteer.api.data.repository.CityRepository;
import com.volunteer.api.data.repository.RegionRepository;
import com.volunteer.api.error.ObjectNotFoundException;
import com.volunteer.api.service.AddressService;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

  private final RegionRepository regionRepository;
  private final CityRepository cityRepository;
  private final AddressRepository addressRepository;

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

  // TODO: is not necessary any more
  @Override
  @Transactional
  public Address getOrCreate(final Address address) {
    final String regionName = address.getCity().getRegion().getName();
    final String cityName = address.getCity().getName();

    final Optional<City> city = cityRepository.findByName(regionName, cityName);
    if (city.isEmpty()) {
      throw new IllegalArgumentException(String.format("City '%s' is missed for refion '%s'",
          cityName, regionName));
    }

    // normalize address first
    address.setCity(city.get());
    address.setAddress(StringUtils.trimToNull(address.getAddress()));

    return addressRepository.findByCityAndAddress(address.getCity().getId(), address.getAddress())
        .orElseGet(() -> addressRepository.save(address));
  }

}
