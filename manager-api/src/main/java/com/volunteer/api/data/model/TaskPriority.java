package com.volunteer.api.data.model;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.Getter;

public enum TaskPriority {

  CRITICAL(1),
  HIGH(2),
  NORMAL(3),
  LOW(4);

  @Getter
  private final int code;

  TaskPriority(int code) {
    this.code = code;
  }

  public static Optional<TaskPriority> byCode(int code) {
    return Stream.of(TaskPriority.values())
        .filter(status -> status.getCode() == code)
        .findAny();
  }

  }
