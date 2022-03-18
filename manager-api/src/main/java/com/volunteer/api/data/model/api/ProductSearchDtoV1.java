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
@JsonPropertyOrder({"id", "category", "name", "note"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductSearchDtoV1 {
  private String name;
  private Integer catcgoryId;
}