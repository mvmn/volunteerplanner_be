package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.volunteer.api.data.model.SubtaskStatus;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id", "taskId", "quantity", "status", "note", "transportRequired",
    "createdByUserId", "createdAt", "closedByUserId", "closedAt"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubtaskDtoV1 {

  @JsonProperty("id")
  private Integer id;

  @NotNull
  @JsonProperty("taskId")
  private Integer taskId;

  @NotNull
  @JsonProperty("quantity")
  private BigDecimal quantity;

  @JsonProperty("status")
  private SubtaskStatus status;

  @JsonProperty("note")
  private String note;

  @JsonProperty("transportRequired")
  private Boolean transportRequired;

  @JsonProperty("createdByUserId")
  private Integer createdByUserId;
  @JsonProperty("createdAt")
  private Long createdAt;

  @JsonProperty("closedByUserId")
  private Integer closedByUserId;
  @JsonProperty("closedAt")
  private Long closedAt;

}
