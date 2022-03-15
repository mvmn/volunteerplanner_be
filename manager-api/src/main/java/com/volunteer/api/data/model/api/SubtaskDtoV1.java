package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.volunteer.api.data.model.SubtaskStatus;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@JsonPropertyOrder({"id", "productId", "quantity", "status", "note", "transportRequired", "volunteerId", "taskId"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubtaskDtoV1 {
  private Integer id;

  @NotNull(message = "cannot be empty")
  private Integer productId;

  @NotNull(message = "cannot be empty")
  private Integer quantity;

  private SubtaskStatus status;

  private String note;

  private boolean transportRequired;

  @NotNull(message = "cannot be empty")
  private Integer volunteerId;

  private Integer taskId;
}
