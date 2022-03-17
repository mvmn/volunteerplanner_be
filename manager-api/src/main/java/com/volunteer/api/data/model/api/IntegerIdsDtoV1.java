package com.volunteer.api.data.model.api;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntegerIdsDtoV1 {
  @JsonProperty("ids")
  @NotEmpty(message = "cannot be empty")
  @Size(min = 1, max = 100, message = "allowed number of items is between 1 and 100")
  private List<Integer> ids;
}
