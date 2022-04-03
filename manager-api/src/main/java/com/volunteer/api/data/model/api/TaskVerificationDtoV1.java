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
public class TaskVerificationDtoV1 {

  @Size(max = 4000)
  private String verificationComment;
}
