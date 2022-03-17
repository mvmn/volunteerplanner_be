package com.volunteer.api.data.model.domain;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class TaskDetalization {
  private Integer productId;
  private BigDecimal quantity;
  private String unitOfMeasure;
}
