package com.volunteer.api.data.user.model.domain;

import lombok.Data;

@Data
public class TaskDetalization {
  private Integer productId;
  private Integer quantity;
  private String unitOfMeasure;
}
