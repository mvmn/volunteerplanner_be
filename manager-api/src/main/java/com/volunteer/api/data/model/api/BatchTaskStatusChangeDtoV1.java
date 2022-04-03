package com.volunteer.api.data.model.api;

import java.util.Collections;
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
public class BatchTaskStatusChangeDtoV1 {

  @Size(max = 4000)
  private String comment;

  @NotEmpty(message = "cannot be empty")
  @Builder.Default
  @JsonProperty("items")
  private List<Integer> items = Collections.emptyList();
}
