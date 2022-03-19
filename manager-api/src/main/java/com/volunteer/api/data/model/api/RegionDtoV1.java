package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionDtoV1 {

  @NotNull
  @JsonProperty("id")
  private Integer id;

  @JsonProperty("name")
  private String name;

}
