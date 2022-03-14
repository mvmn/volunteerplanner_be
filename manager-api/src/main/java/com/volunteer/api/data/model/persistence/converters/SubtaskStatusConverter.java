package com.volunteer.api.data.model.persistence.converters;

import com.volunteer.api.data.model.SubtaskStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SubtaskStatusConverter implements AttributeConverter<SubtaskStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(SubtaskStatus attribute) {
    return attribute.getCode();
  }

  @Override
  public SubtaskStatus convertToEntityAttribute(Integer dbData) {
    return dbData == null ? null
        : SubtaskStatus.byCode(dbData)
            .orElseThrow(() -> new IllegalArgumentException("Unknown subtask status code " + dbData));
  }
}
