package com.volunteer.api.data.model.api.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volunteer.api.data.model.api.search.sort.SortParameters;
import javax.validation.Valid;
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
public class SearchDto<T> {

  @Builder.Default
  @Min(value = 1)
  @JsonProperty("page")
  private int page = 1;

  @Builder.Default
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
