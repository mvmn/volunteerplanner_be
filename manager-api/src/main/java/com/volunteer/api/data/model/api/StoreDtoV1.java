package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "city", "address", "confidential", "note"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreDtoV1 {

  @JsonProperty("id")
  private Integer id;

  @NotNull
  @NotBlank
  @JsonProperty("name")
  private String name;

  @NotNull
  @Valid
  @JsonProperty("city")
  private CityDtoV1 city;

  @JsonProperty("address")
  private String address;

  @JsonProperty("confidential")
  private Boolean confidential;

  @JsonProperty("note")
  private String note;

}
