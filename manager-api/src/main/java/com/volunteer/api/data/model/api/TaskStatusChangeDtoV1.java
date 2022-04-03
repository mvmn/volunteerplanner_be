package com.volunteer.api.data.model.api;

import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskStatusChangeDtoV1 {

  @Size(max = 65000)
  private String comment;
}
