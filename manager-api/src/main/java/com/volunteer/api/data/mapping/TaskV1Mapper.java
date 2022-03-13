package com.volunteer.api.data.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.volunteer.api.data.model.api.TaskDetalizationDtoV1;
import com.volunteer.api.data.model.api.TaskDtoV1;
import com.volunteer.api.data.model.domain.TaskDetalization;
import com.volunteer.api.data.model.persistence.Task;

@Mapper(componentModel = "spring", uses = GenericMapper.class)
public interface TaskV1Mapper {

  @Mapping(target = "volunteerStoreId", source = "volunteerStore.id")
  @Mapping(target = "customerStoreId", source = "customerStore.id")
  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "createdByUserId", source = "createdBy.id")
  @Mapping(target = "verifiedByUserId", source = "verifiedBy.id")
  @Mapping(target = "closedByUserId", source = "closedBy.id")
  TaskDtoV1 map(Task task);

  @Mapping(source = "volunteerStoreId", target = "volunteerStore.id")
  @Mapping(source = "customerStoreId", target = "customerStore.id")
  @Mapping(source = "productId", target = "product")
  @Mapping(source = "createdByUserId", target = "createdBy")
  @Mapping(source = "verifiedByUserId", target = "verifiedBy")
  @Mapping(source = "closedByUserId", target = "closedBy")
  @Mapping(target = "version", ignore = true)
  Task map(TaskDtoV1 dto);

  TaskDetalization map(TaskDetalizationDtoV1 dto);

  Task clone(Task task);
}
