package com.volunteer.api.data.model.persistence.specifications;

import com.volunteer.api.data.model.persistence.Subtask;
import org.springframework.data.jpa.domain.Specification;

public final class SubtaskSearchSpecifications {

  public static Specification<Subtask> byCreator(final Number value) {
    return (root, query, builder) -> builder.equal(root.get("createdBy").get("id"),
        value.intValue());
  }

  private SubtaskSearchSpecifications() {
    // empty constructor
  }

}
