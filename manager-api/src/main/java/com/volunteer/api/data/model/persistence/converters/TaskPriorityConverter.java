package com.volunteer.api.data.model.persistence.converters;

import com.volunteer.api.data.model.TaskPriority;
import java.util.Objects;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TaskPriorityConverter implements AttributeConverter<TaskPriority, Integer> {

  @Override
  public Integer convertToDatabaseColumn(final TaskPriority attribute) {
    return attribute.getCode();
  }

  @Override
  public TaskPriority convertToEntityAttribute(final Integer dbData) {
    return Objects.isNull(dbData)
        ? null
        : TaskPriority.byCode(dbData).orElseThrow(() -> new IllegalArgumentException(
            "Unknown task priority code " + dbData));
  }

}
