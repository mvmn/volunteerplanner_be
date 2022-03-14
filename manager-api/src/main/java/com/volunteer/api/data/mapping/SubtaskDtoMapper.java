package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.SubtaskDtoV1;
import com.volunteer.api.data.model.api.SubtaskGetDtoV1;
import com.volunteer.api.data.model.persistence.Subtask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = GenericMapper.class)
public interface SubtaskDtoMapper {
  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "volunteerId", source = "volunteer.id")
  @Mapping(target = "taskId", source = "task.id")
  SubtaskDtoV1 map(Subtask subtask);

  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "productName", source = "product.name")
  @Mapping(target = "volunteerId", source = "volunteer.id")
  @Mapping(target = "volunteerName", source = "volunteer.fullName")
  @Mapping(target = "taskId", source = "task.id")
  SubtaskGetDtoV1 mapGet(Subtask subtask);

  @Mapping(source = "productId", target = "product.id")
  @Mapping(source = "volunteerId", target = "volunteer.id")
  @Mapping(source = "taskId", target = "task.id")
  Subtask map(SubtaskDtoV1 dto);

  Collection<SubtaskDtoV1> map(Collection<Subtask> subtasks);

  Collection<SubtaskGetDtoV1> mapGet(Collection<Subtask> subtasks);
}
