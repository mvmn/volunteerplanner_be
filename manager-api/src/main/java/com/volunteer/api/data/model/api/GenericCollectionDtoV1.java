package com.volunteer.api.data.model.api;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericCollectionDtoV1<T> {

  @JsonProperty("items")
  protected Collection<T> items;
}
