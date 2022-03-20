package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
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
  private Collection<T> items;

}
