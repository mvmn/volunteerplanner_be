package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "category", "name", "note"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDtoV1 {
  private Integer id;

  @NotNull(message = "cannot be empty")
  private CategoryDtoV1 category;

  @NotBlank(message = "cannot be blank")
  @NotNull(message = "cannot be empty")
  private String name;

  private String note;
}
