package com.volunteer.api.data.repository.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Getter
@RequiredArgsConstructor
public class Query<T> {

  private final Specification<T> specification;
  private final Pageable pageable;


}
