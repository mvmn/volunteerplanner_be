package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.volunteer.api.data.model.TaskPriority;
import com.volunteer.api.data.model.TaskStatus;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"id", "volunteerStore", "customerStore", "product",
    "quantity", "quantityLeft", "productMeasure", "priority", "deadlineDate", "note",
    "status", "subtaskCount", "createdBy", "createdAt", "verifiedBy", "verifiedAt",
    "closedBy", "closedAt"})
@JsonInclude(Include.NON_NULL)
public class TaskViewDtoV1 {

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("volunteerStore")
  private StoreDtoV1 volunteerStore;
  @JsonProperty("customerStore")
  private StoreDtoV1 customerStore;

  @JsonProperty("product")
  private ProductDtoV1 product;

  @JsonProperty("quantity")
  private BigDecimal quantity;
  @JsonProperty("quantityLeft")
  private BigDecimal quantityLeft;
  @JsonProperty("productMeasure")
  private String productMeasure;

  @JsonProperty("priority")
  private TaskPriority priority;
  @JsonProperty("deadlineDate")
  private Long deadlineDate;
  @JsonProperty("note")
  private String note;

  @JsonProperty("status")
  private TaskStatus status;

  @JsonProperty("subtaskCount")
  private Long subtaskCount;

  @JsonProperty("createdBy")
  private UserDtoV1 createdBy;
  @JsonProperty("createdAt")
  private Long createdAt;

  @JsonProperty("verifiedBy")
  private UserDtoV1 verifiedBy;
  @JsonProperty("verifiedAt")
  private Long verifiedAt;

  @JsonProperty("closedBy")
  private UserDtoV1 closedBy;
  @JsonProperty("closedAt")
  private Long closedAt;

}
