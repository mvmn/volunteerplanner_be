package com.volunteer.api.data.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// added due to
// Cannot construct instance of `org.springframework.data.domain.Page`
// (no Creators, like default constructor, exist)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"page", "pageSize", "totalCount", "items"})
public class GenericPageDtoV1<T> {

  @JsonProperty("page")
  private Integer page;
  @JsonProperty("pageSize")
  private Integer pageSize;
  @JsonProperty("totalCount")
  private Long totalCount;

  @JsonProperty("items")
  private Collection<T> items;

}
