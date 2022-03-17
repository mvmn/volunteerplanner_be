package com.volunteer.api.data.model.api.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volunteer.api.data.model.api.search.sort.SortParameters;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Data;

@Data
public class SearchDto<T> {

  @Min(value = 0)
  @JsonProperty("page")
  private Integer page = 0;

  @Min(value = 1)
  @Max(value = 100)
  @JsonProperty("pageSize")
  private int pageSize = 10;

  @Valid
  @JsonProperty("filter")
  private T filter;

  @Valid
  @JsonProperty("sort")
  private SortParameters sort;

}
