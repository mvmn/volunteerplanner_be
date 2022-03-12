package com.volunteer.api.data.user.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.volunteer.api.data.user.model.api.TaskDetalizationDtoV1;
import com.volunteer.api.data.user.model.api.TaskDtoV1;
import com.volunteer.api.data.user.model.domain.TaskDetalization;
import com.volunteer.api.data.user.model.persistence.Task;

@Mapper(componentModel = "spring", uses = GenericMapper.class)
public interface TaskV1Mapper {

  @Mapping(target = "storeAddressId", source = "storeAddress.id")
  @Mapping(target = "customerAddressId", source = "customerAddress.id")
  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "createdByUserId", source = "createdBy.id")
  @Mapping(target = "verifiedByUserId", source = "verifiedBy.id")
  @Mapping(target = "closedByUserId", source = "closedBy.id")
  TaskDtoV1 map(Task task);

  @Mapping(source = "storeAddressId", target = "storeAddress")
  @Mapping(source = "customerAddressId", target = "customerAddress")
  @Mapping(source = "productId", target = "product")
  @Mapping(source = "createdByUserId", target = "createdBy")
  @Mapping(source = "verifiedByUserId", target = "verifiedBy")
  @Mapping(source = "closedByUserId", target = "closedBy")
  @Mapping(target = "version", ignore = true)
  Task map(TaskDtoV1 dto);
  
  TaskDetalization map(TaskDetalizationDtoV1 dto);
}
