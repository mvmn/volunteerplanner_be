package com.volunteer.api.data.model.api.search.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonTypeName("operator")
public class OperatorFilterDto implements FilterDto {

  @NotNull
  @JsonProperty("operator")
  private Operator operator;

  @Valid
  @NotEmpty
  @JsonProperty("operands")
  private List<@NotNull FilterDto> operands;

  public enum Operator {
    @JsonProperty("and")
    AND,
    @JsonProperty("or")
    OR
  }

}
