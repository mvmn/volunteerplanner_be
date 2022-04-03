package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volunteer.api.data.model.TaskPriority;
import com.volunteer.api.data.model.TaskStatus;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class TaskDtoV1 {

  @JsonProperty("id")
  private Integer id;

  @NotNull(message = "cannot be empty")
  @JsonProperty("volunteerStoreId")
  private Integer volunteerStoreId;
  @NotNull(message = "cannot be empty")
  @JsonProperty("customerStoreId")
  private Integer customerStoreId;

  @NotNull(message = "cannot be empty")
  @JsonProperty("productId")
  private Integer productId;
  @NotNull(message = "cannot be empty")
  @Min(value = 1, message = "cannot be less than one")
  @JsonProperty("quantity")
  private BigDecimal quantity;
  @NotNull(message = "cannot be empty")
  @JsonProperty("productMeasure")
  private String productMeasure;

  @JsonProperty("priority")
  private TaskPriority priority;
  @NotNull(message = "cannot be empty")
  @JsonProperty("deadlineDate")
  private Long deadlineDate;
  @JsonProperty("note")
  private String note;

  @JsonProperty("status")
  private TaskStatus status;

}
