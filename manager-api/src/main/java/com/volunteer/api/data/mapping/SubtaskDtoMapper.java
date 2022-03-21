package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.SubtaskDtoV1;
import com.volunteer.api.data.model.persistence.Subtask;
import java.util.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = GenericMapper.class)
public interface SubtaskDtoMapper {

  SubtaskDtoV1 map(final Subtask subtask);

  @Mapping(target = "task.id", source = "taskId")
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "closedBy", ignore = true)
  Subtask map(final SubtaskDtoV1 dto);

  Collection<SubtaskDtoV1> map(Collection<Subtask> subtasks);

}
