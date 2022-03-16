package com.volunteer.api.data.model.api.search.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("text")
public class ValueTextFilterDto extends ValueFilterDto {

  @NotBlank
  @JsonProperty("value")
  private String value;

}
