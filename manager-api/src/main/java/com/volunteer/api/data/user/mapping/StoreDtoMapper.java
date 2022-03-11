package com.volunteer.api.data.user.mapping;

import com.volunteer.api.data.user.model.api.StoreDtoV1;
import com.volunteer.api.data.user.model.dto.Store;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface StoreDtoMapper {

  Collection<StoreDtoV1> map(Collection<Store> stores);

  StoreDtoV1 map(Store store);

  Store map(StoreDtoV1 storeDto);
}
