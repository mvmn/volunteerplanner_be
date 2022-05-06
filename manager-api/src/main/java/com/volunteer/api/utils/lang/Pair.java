package com.volunteer.api.utils.lang;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Pair<A, B> {
  private final A a;
  private final B b;

  public static <A, B> Pair<A, B> of(A a, B b) {
    return new Pair<>(a, b);
  }
}
