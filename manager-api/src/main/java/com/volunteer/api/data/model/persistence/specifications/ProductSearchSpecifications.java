package com.volunteer.api.data.model.persistence.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import com.volunteer.api.data.model.persistence.Category;
import com.volunteer.api.data.model.persistence.Product;

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

  public static Specification<Product> byCategoryPath(String categoryPath) {
    return new Specification<Product>() {
      private static final long serialVersionUID = -837414416227802906L;

      @Override
      public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Subquery<Category> categoriesSubquery = query.subquery(Category.class);
        Root<Category> categoryRoot = categoriesSubquery.from(Category.class);
        categoriesSubquery.select(categoryRoot.get("id"));
        categoriesSubquery.where(cb.like(categoryRoot.get("path"), categoryPath + "%"));
        return root.get("category").get("id").in(categoriesSubquery);
      }
    };
  }
}
