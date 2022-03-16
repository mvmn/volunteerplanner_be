package com.volunteer.api.data.model.api.search.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("numeric")
public class ValueNumericFilterDto extends ValueFilterDto {

  @NotNull
  @JsonProperty("value")
  private Number value;

}
