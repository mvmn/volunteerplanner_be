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
@JsonPropertyOrder({"id", "name", "address", "contactPerson", "note"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreDtoV1 {
  private Integer id;
  private String name;
  private AddressDtoV1 address;
  private String contactPerson;
  private String note;
}
