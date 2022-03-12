package com.volunteer.api.data.user;

import com.volunteer.api.data.user.mapping.AddressDtoMapper;
import com.volunteer.api.data.user.model.api.AddressDtoV1;
import com.volunteer.api.data.user.model.persistence.Address;
import com.volunteer.api.data.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/address", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressControllerV1 {

    private final AddressService addressService;

    @Autowired
    public AddressControllerV1(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping(path = "/{address-id}")
    public ResponseEntity<AddressDtoV1> getById(@PathVariable("address-id") final Integer addressId) {
        Optional<Address> address = addressService.get(addressId);
        return address
                .map(a -> ResponseEntity.ok(AddressDtoMapper.map(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AddressDtoV1> create(@RequestBody final AddressDtoV1 address) {
        AddressDtoV1 result = AddressDtoMapper.map(addressService.save(AddressDtoMapper.map(address)));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping(path = "/regions")
    public ResponseEntity<List<String>> getRegions() {
        return ResponseEntity.ok(addressService.getDictinctRegions(null));
    }

    @GetMapping(path = "/regions/{region}")
    public ResponseEntity<List<String>> getRegions(@PathVariable("region") final String region) {
        return ResponseEntity.ok(addressService.getDictinctRegions(region));
    }

    @GetMapping(path = "/cities")
    public ResponseEntity<List<String>> getAllCities() {
        return ResponseEntity.ok(addressService.getDictinctCities(null));
    }

    @GetMapping(path = "/cities/{city}")
    public ResponseEntity<List<String>> getCities(@PathVariable("city") final String city) {
        return ResponseEntity.ok(addressService.getDictinctCities(city));
    }
}
