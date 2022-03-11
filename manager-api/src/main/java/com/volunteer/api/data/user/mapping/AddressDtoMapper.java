package com.volunteer.api.data.user.mapping;

import com.volunteer.api.data.user.model.api.AddressDtoV1;
import com.volunteer.api.data.user.model.dto.Address;

import java.util.Collection;
import java.util.stream.Collectors;

public class AddressDtoMapper {
    public static Collection<AddressDtoV1> map(Collection<Address> addresses) {
        return addresses.stream().map(AddressDtoMapper::map)
                .collect(Collectors.toList());
    }

    public static AddressDtoV1 map(Address address) {
        return AddressDtoV1.builder()
                .id(address.getId())
                .region(address.getRegion())
                .city(address.getCity())
                .address(address.getAddress())
                .build();
    }

    public static Address map(AddressDtoV1 addressDto) {
        return Address.builder()
                .id(addressDto.getId())
                .region(addressDto.getRegion())
                .city(addressDto.getCity())
                .address(addressDto.getAddress())
                .build();
    }
}
