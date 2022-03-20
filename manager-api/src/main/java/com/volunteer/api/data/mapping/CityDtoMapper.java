package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.CityDtoV1;
import com.volunteer.api.data.model.persistence.City;
import java.util.Collection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = GenericMapper.class)
public interface CityDtoMapper {

  CityDtoV1 map(final City city);

  City map(CityDtoV1 cityDto);

  Collection<CityDtoV1> map(Collection<City> cities);

}
