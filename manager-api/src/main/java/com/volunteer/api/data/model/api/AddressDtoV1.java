package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "region", "city", "address"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDtoV1 {

  private Integer id;
  private String region;
  private String city;
  private String address;

}
