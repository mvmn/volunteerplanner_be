package com.volunteer.api.data.model.persistence.specifications;

import com.volunteer.api.data.model.persistence.Store;
import org.springframework.data.jpa.domain.Specification;

public final class StoreSearchSpecifications {

  public static Specification<Store> byName(final String value) {
    return (root, query, builder) -> builder.like(builder.lower(root.get("name")), "%" + value.toLowerCase() + "%");
  }

  public static Specification<Store> byAddress(final String value) {
    return (root, query, builder) -> builder.like(builder.lower(root.get("address")), "%" + value.toLowerCase() + "%");
  }

  public static Specification<Store> byNote(final String value) {
    return (root, query, builder) -> builder.like(builder.lower(root.get("note")), "%" + value.toLowerCase() + "%");
  }

  public static Specification<Store> byRegion(final Number value) {
    return (root, query, builder) -> builder.equal(root.get("city").get("region").get("id"),
        value.intValue());
  }

  public static Specification<Store> byCity(final Number value) {
    return (root, query, builder) -> builder.equal(root.get("city").get("id"), value.intValue());
  }

  public static Specification<Store> byConfidential(final Boolean value) {
    return (root, query, builder) -> builder.equal(root.get("confidential"), value);
  }

  private StoreSearchSpecifications() {
    // empty constructor
  }

}
