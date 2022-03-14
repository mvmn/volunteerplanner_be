package com.volunteer.api.data.model;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

public enum SubtaskStatus {
  IN_PROGRESS(1), COMPLETED(2), REJECTED(3);

  @Getter
  private final int code;

  SubtaskStatus(int code) {
    this.code = code;
  }

  public static Optional<SubtaskStatus> byCode(int code) {
    return Stream.of(SubtaskStatus.values()).filter(ts -> ts.getCode() == code).findAny();
  }
}
