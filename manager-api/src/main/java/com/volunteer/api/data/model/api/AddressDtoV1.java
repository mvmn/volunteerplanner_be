package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.volunteer.api.data.model.persistence.City;
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
@JsonPropertyOrder({"id", "city", "address"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDtoV1 {

  @JsonProperty("id")
  private Integer id;

  @NotNull
  @Valid
  @JsonProperty("city")
  private City city;

  @NotNull
  @NotBlank
  @JsonProperty("address")
  private String address;

}
