package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.GenericPageDtoV1;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public final class GenericPageDtoMapper {

  public static <T, R> GenericPageDtoV1<R> map(final Integer page, final Integer pageSize,
      final Page<T> data, final Function<T, R> converter) {
    return GenericPageDtoV1.<R>builder()
        .page(page != null ? page.intValue() : 0)
        .pageSize(pageSize != null ? pageSize.intValue() : 100)
        .totalCount(data.getTotalElements())
        .items(data.stream()
            .map(converter::apply)
            .collect(Collectors.toList()))
        .build();
  }

  private GenericPageDtoMapper() {
    // empty constructor
  }

}
