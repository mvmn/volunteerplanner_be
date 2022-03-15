package com.volunteer.api.data.model.api;

import java.util.Set;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volunteer.api.data.model.TaskStatus;
import lombok.Data;

@Data
public class TaskSearchDtoV1 {

  public static enum SortOrder {
    PRIORITY, DUEDATE;
  }

  @JsonProperty("customer")
  private String customer;
  @JsonProperty("productId")
  private Integer productId;
  @JsonProperty("volunteerStoreId")
  private Integer volunteerStoreId;
  @JsonProperty("customerStoreId")
  private Integer customerStoreId;
  @JsonProperty("statuses")
  private Set<TaskStatus> statuses;
  @JsonProperty("categoryIds")
  private Set<Integer> categoryIds;
  @JsonProperty("remainingQuantityMoreThan")
  private Integer remainingQuantityMoreThan;
  @JsonProperty("zeroQuantity")
  private Boolean zeroQuantity;
  @JsonProperty("excludeExpired")
  private Boolean excludeExpired;
  @Min(0)
  @JsonProperty("pageNumber")
  private Integer pageNumber;
  @Min(1)
  @Max(100)
  @JsonProperty("pageSize")
  private Integer pageSize;
  @JsonProperty("sortOrder")
  private SortOrder sortOrder;
}
