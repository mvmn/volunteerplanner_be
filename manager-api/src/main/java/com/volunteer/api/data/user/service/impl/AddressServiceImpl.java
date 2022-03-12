package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.user.model.persistence.Address;
import com.volunteer.api.data.user.repository.AddressRepository;
import com.volunteer.api.data.user.service.AddressService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Collection<Address> getAll() {
        return addressRepository.findAll();
    }

    @Override
    public Optional<Address> get(Integer id) {
        return addressRepository.findById(id);
    }

    @Override
    public Address save(Address address) {
        Optional<Address> existing = addressRepository.findByRegionAndCityAndAddress(
                address.getRegion(), address.getCity(), address.getAddress());
        return existing.orElse(addressRepository.save(address));
    }

    @Override
    public List<String> getDictinctRegions(String region) {
        region = StringUtils.defaultIfBlank(region, "").replace("*", "%");
        return addressRepository.findDistinctRegions(region);
    }

    @Override
    public List<String> getDictinctCities(String city) {
        city = StringUtils.defaultIfBlank(city, "").replace("*", "%");
        return addressRepository.findDistinctCities(city);
    }
}
