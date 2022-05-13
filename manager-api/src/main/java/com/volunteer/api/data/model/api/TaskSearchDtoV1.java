package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volunteer.api.data.model.TaskStatus;
import java.util.Set;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskSearchDtoV1 {

  public static enum SortOrder {
    PRIORITY, DUEDATE, STATUS, PRODUCT_NAME, QUANTITY_LEFT;
  }
  
  public static enum SortDirection {
    ASC, DESC;
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
  @JsonProperty("categoryPath")
  private String categoryPath;

  @JsonProperty("remainingQuantityMoreThan")
  private Integer remainingQuantityMoreThan;
  @JsonProperty("zeroQuantity")
  private Boolean zeroQuantity;

  @JsonProperty("excludeExpired")
  private Boolean excludeExpired;

  @JsonProperty("createdByUserId")
  private Integer createdByUserId;
  @JsonProperty("verifiedByUserId")
  private Integer verifiedByUserId;
  @JsonProperty("closedByUserId")
  private Integer closedByUserId;

  @Min(0)
  @JsonProperty("pageNumber")
  private Integer pageNumber;
  @Min(1)
  @Max(100)
  @JsonProperty("pageSize")
  private Integer pageSize;
  @JsonProperty("sortOrder")
  private SortOrder sortOrder;
  @JsonProperty("sortDirection")
  private SortDirection sortDirection;

  @JsonProperty("searchText")
  private String searchText;
}
