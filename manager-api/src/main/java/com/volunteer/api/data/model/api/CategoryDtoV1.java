package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"id", "parent", "name", "note"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDtoV1 {

  @JsonProperty("id")
  private Integer id;
  @JsonProperty("parent")
  private CategoryDtoV1 parent;

  @JsonProperty("name")
  private String name;
  @JsonProperty("note")
  private String note;

}
