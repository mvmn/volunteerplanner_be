package com.volunteer.api.data.model.api;

import java.util.Collection;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskBatchDtoV1 {
  @NotNull(message = "cannot be empty")
  @JsonProperty("blueprint")
  private TaskDtoV1 blueprint;
  @NotEmpty(message = "cannot be empty")
  @JsonProperty("details")
  private Collection<TaskDetalizationDtoV1> details;
}
