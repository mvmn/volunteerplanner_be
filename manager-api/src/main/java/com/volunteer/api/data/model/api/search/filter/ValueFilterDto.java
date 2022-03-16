package com.volunteer.api.data.model.api.search.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ValueFilterDto implements FilterDto {

  @NotBlank
  @JsonProperty("field")
  private String field;

}
