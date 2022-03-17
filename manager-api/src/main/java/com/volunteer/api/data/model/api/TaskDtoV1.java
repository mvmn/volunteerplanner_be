package com.volunteer.api.data.model.api;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.volunteer.api.data.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"id", "customer", "volunteerStoreId", "customerStoreId", "productId",
    "quantity", "productMeasure", "priority", "deadlineDate", "note", "statusId", "createdByUserId",
    "createdAt", "verifiedByUserId", "verifiedAt", "closedByUserId", "closedAt", "quantityLeft"})
@JsonInclude(Include.NON_NULL)
public class TaskDtoV1 {
  @JsonProperty("id")
  private Integer id;
  @NotNull(message = "cannot be empty")
  @JsonProperty("customer")
  private String customer;
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
  @NotNull(message = "cannot be empty")
  @JsonProperty("priority")
  private Integer priority;
  @NotNull(message = "cannot be empty")
  @JsonProperty("deadlineDate")
  private Long deadlineDate;
  @JsonProperty("note")
  private String note;
  @JsonProperty("status")
  private TaskStatus status;
  @JsonProperty("createdByUserId")
  private Integer createdByUserId;
  @JsonProperty("createdAt")
  private Long createdAt;
  @JsonProperty("verifiedByUserId")
  private Integer verifiedByUserId;
  @JsonProperty("verifiedAt")
  private Long verifiedAt;
  @JsonProperty("closedByUserId")
  private Integer closedByUserId;
  @JsonProperty("closedAt")
  private Long closedAt;
  @JsonProperty("quantityLeft")
  private BigDecimal quantityLeft;
}
