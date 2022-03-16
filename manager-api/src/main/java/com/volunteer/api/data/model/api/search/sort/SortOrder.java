package com.volunteer.api.data.model.api.search.sort;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SortOrder {

  @JsonProperty("asc")
  ASC,
  @JsonProperty("desc")
  DESC

}
