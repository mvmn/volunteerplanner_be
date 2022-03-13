package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.user.model.persistence.Address;
import com.volunteer.api.data.user.repository.AddressRepository;
import com.volunteer.api.data.user.service.AddressService;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

  private final AddressRepository repository;

  @Override
  public Collection<Address> getAll() {
    return repository.findAll();
  }

  @Override
  public Optional<Address> get(Integer id) {
    return repository.findById(id);
  }

  @Override
  public Address getOrCreate(Address address) {
    // normalize address first
    address.setRegion(StringUtils.trimToNull(address.getRegion()));
    address.setCity(StringUtils.trimToNull(address.getCity()));
    address.setAddress(StringUtils.trimToNull(address.getAddress()));

    return repository.findByRegionAndCityAndAddress(address.getRegion(), address.getCity(),
        address.getAddress()).orElseGet(() -> repository.save(address));
  }

  @Override
  public List<String> getRegions(String region) {
    return repository.findDistinctRegions(StringUtils.defaultIfBlank(region, "").replace("*", "%"));
  }

  @Override
  public List<String> getCities(String city) {
    return repository.findDistinctCities(StringUtils.defaultIfBlank(city, "").replace("*", "%"));
  }
}
