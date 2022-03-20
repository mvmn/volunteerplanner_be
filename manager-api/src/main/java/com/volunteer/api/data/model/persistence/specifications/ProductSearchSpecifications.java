package com.volunteer.api.data.model.persistence.specifications;

import com.volunteer.api.data.model.persistence.Product;
import org.springframework.data.jpa.domain.Specification;

public final class ProductSearchSpecifications {

  public static Specification<Product> byCategoryId(final Number value) {
    return (root, query, builder) -> builder.or(
        builder.equal(root.get("category").get("id"), value.intValue()),
        builder.equal(root.get("category").get("parent").get("id"), value.intValue())
    );
  }

  public static Specification<Product> byName(final String value) {
    return (root, query, builder) -> builder.like(root.get("name"), "%" + value + "%");
  }

  private ProductSearchSpecifications() {
    // empty constructor
  }

}
