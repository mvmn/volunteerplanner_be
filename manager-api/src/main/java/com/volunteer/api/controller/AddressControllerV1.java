package com.volunteer.api.controller;

import com.volunteer.api.data.mapping.CityDtoMapper;
import com.volunteer.api.data.mapping.RegionDtoMapper;
import com.volunteer.api.data.model.api.CityDtoV1;
import com.volunteer.api.data.model.api.GenericCollectionDtoV1;
import com.volunteer.api.data.model.api.RegionDtoV1;
import com.volunteer.api.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/address", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressControllerV1 {

  private final AddressService addressService;

  private final RegionDtoMapper regionDtoMapper;
  private final CityDtoMapper cityDtoMapper;

  // access for anybody
  @GetMapping(path = "/regions")
  @ResponseStatus(HttpStatus.OK)
  public GenericCollectionDtoV1<RegionDtoV1> getRegions() {
    return GenericCollectionDtoV1.<RegionDtoV1>builder()
        .items(regionDtoMapper.map(addressService.getAllRegions()))
        .build();
  }

  @GetMapping(path = "/regions/{region-id}")
  @ResponseStatus(HttpStatus.OK)
  public RegionDtoV1 getRegion(@PathVariable("region-id") final Integer regionId) {
    return regionDtoMapper.map(addressService.getRegionById(regionId));
  }

  @GetMapping(path = "/regions/{region-id}/cities")
  @ResponseStatus(HttpStatus.OK)
  public GenericCollectionDtoV1<CityDtoV1> getRegionCities(
      @PathVariable("region-id") final Integer regionId) {
    return GenericCollectionDtoV1.<CityDtoV1>builder()
        .items(cityDtoMapper.map(addressService.getAllRegionCities(regionId)))
        .build();
  }

  @GetMapping(path = "/cities/{city-id}")
  @ResponseStatus(HttpStatus.OK)
  public CityDtoV1 getCity(@PathVariable("city-id") final Integer cityId) {
    return cityDtoMapper.map(addressService.getCityById(cityId));
  }

}
