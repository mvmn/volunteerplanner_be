package com.volunteer.api.data.model.api;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"productId", "quantity", "unitOfMeasure"})
public class TaskDetalizationDtoV1 {
  @JsonProperty("productId")
  @NotNull(message = "cannot be empty")
  private Integer productId;
  @NotNull(message = "cannot be empty")
  @Min(value = 1, message = "cannot be less than one")
  @JsonProperty("quantity")
  private Integer quantity;
  @NotNull(message = "cannot be empty")
  @JsonProperty("unitOfMeasure")
  private String unitOfMeasure;
}
