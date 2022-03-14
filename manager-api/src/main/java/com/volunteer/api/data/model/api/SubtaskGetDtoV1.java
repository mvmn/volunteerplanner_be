package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.volunteer.api.data.model.SubtaskStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id", "productId", "productName", "quantity", "status", "note", "transportRequired",
        "volunteerId", "volunteerName", "taskId"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubtaskGetDtoV1 {
  private Integer id;

  private Integer productId;

  private String productName;

  private Integer quantity;

  private SubtaskStatus status;

  private String note;

  private boolean transportRequired;

  private Integer volunteerId;

  private String volunteerName;

  private Integer taskId;
}
