package com.volunteer.api.data.model.api.search.sort;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SortParameters {

  @NotBlank
  @JsonProperty("field")
  private String field;

  @JsonProperty("order")
  private SortOrder order = SortOrder.ASC;

}
