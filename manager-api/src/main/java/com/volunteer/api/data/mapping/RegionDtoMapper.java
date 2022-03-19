package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.RegionDtoV1;
import com.volunteer.api.data.model.persistence.Region;
import java.util.Collection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegionDtoMapper {

  RegionDtoV1 map(final Region region);

  Region map(RegionDtoV1 regionDto);

  Collection<RegionDtoV1> map(Collection<Region> regions);

}
