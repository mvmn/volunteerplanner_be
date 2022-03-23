package com.volunteer.api.data.model;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.Getter;

public enum TaskStatus {
  NEW(1),
  VERIFIED(2),
  COMPLETED(3),
  REJECTED(4);

  @Getter
  private final int code;

  TaskStatus(int code) {
    this.code = code;
  }

  public static Optional<TaskStatus> byCode(int code) {
    return Stream.of(TaskStatus.values())
        .filter(status -> status.getCode() == code)
        .findAny();
  }

}
