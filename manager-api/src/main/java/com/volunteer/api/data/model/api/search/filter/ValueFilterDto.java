package com.volunteer.api.data.model.api.search.filter;

import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ValueFilterDto implements FilterDto {

  @NotBlank
  @JsonProperty("field")
  private String field;

}
