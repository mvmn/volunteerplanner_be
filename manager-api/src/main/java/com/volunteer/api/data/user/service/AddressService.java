package com.volunteer.api.data.user.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import com.volunteer.api.data.user.model.persistence.Address;

public interface AddressService {
    Collection<Address> getAll();

    Optional<Address> get(final Integer id);

    Address save(final Address address);

    List<String> getDictinctRegions(String region);

    List<String> getDictinctCities(String city);
}
