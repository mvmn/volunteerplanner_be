package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.TaskDtoV1;
import com.volunteer.api.data.model.persistence.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = GenericMapper.class)
public interface TaskDtoV1Mapper {

//  @Mapping(target = "volunteerStoreId", source = "volunteerStore.id")
//  @Mapping(target = "customerStoreId", source = "customerStore.id")
//  @Mapping(target = "productId", source = "product.id")
//  @Mapping(target = "createdByUserId", source = "createdBy.id")
//  @Mapping(target = "verifiedByUserId", source = "verifiedBy.id")
//  @Mapping(target = "closedByUserId", source = "closedBy.id")
//  TaskDtoV1 map(final Task task);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "volunteerStore", source = "volunteerStoreId")
  @Mapping(target = "customerStore", source = "customerStoreId")
  @Mapping(target = "product", source = "productId")
  @Mapping(target = "quantityLeft", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "verifiedBy", ignore = true)
  @Mapping(target = "verifiedAt", ignore = true)
  @Mapping(target = "closedBy", ignore = true)
  @Mapping(target = "closedAt", ignore = true)
  Task map(final TaskDtoV1 dto);

//  Collection<TaskDtoV1> map(final Collection<Task> tasks);

}
