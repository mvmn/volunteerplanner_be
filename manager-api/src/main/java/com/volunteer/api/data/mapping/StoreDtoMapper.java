package com.volunteer.api.data.mapping;

import java.util.Collection;
import org.mapstruct.Mapper;
import com.volunteer.api.data.model.api.StoreDtoV1;
import com.volunteer.api.data.model.persistence.Store;

@Mapper(componentModel = "spring")
public interface StoreDtoMapper {

  Collection<StoreDtoV1> map(Collection<Store> stores);

  StoreDtoV1 map(Store store);

  Store map(StoreDtoV1 storeDto);
}
