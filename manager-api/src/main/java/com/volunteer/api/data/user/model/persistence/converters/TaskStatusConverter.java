package com.volunteer.api.data.user.model.persistence.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import com.volunteer.api.data.user.model.TaskStatus;

@Converter(autoApply = true)
public class TaskStatusConverter implements AttributeConverter<TaskStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(TaskStatus attribute) {
    return attribute.getCode();
  }

  @Override
  public TaskStatus convertToEntityAttribute(Integer dbData) {
    return dbData == null ? null
        : TaskStatus.byCode(dbData)
            .orElseThrow(() -> new IllegalArgumentException("Unknown task status code " + dbData));
  }
}
