package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.TaskDtoV1;
import com.volunteer.api.data.model.persistence.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = GenericMapper.class)
public interface TaskDtoV1Mapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "volunteerStore", source = "volunteerStoreId")
  @Mapping(target = "customerStore", source = "customerStoreId")
  @Mapping(target = "product", source = "productId")
  @Mapping(target = "quantityLeft", ignore = true)
  @Mapping(target = "subtaskCount", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "verifiedBy", ignore = true)
  @Mapping(target = "verifiedAt", ignore = true)
  @Mapping(target = "verificationComment", ignore = true)
  @Mapping(target = "closedBy", ignore = true)
  @Mapping(target = "closedAt", ignore = true)
  @Mapping(target = "closeComment", ignore = true)
  Task map(final TaskDtoV1 dto);


}
