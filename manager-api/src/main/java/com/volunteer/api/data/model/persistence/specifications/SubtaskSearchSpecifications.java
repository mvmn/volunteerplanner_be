package com.volunteer.api.data.model.persistence.specifications;

import com.volunteer.api.data.model.SubtaskStatus;
import com.volunteer.api.data.model.persistence.Category;
import com.volunteer.api.data.model.persistence.Subtask;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public final class SubtaskSearchSpecifications {

  public static Specification<Subtask> byCreator(final Number value) {
    return (root, query, builder) -> builder.equal(root.get("createdBy").get("id"),
        value.intValue());
  }

  public static Specification<Subtask> byCloser(final Number value) {
    return (root, query, builder) -> builder.equal(root.get("closedBy").get("id"),
        value.intValue());
  }

  public static Specification<Subtask> byStatus(final String value) {
    final SubtaskStatus status = SubtaskStatus.valueOf(value);
    return (root, query, builder) -> builder.equal(root.get("status"), status);
  }

  public static Specification<Subtask> byCategory(final Number value) {
    return (root, query, builder) -> builder.or(
        builder.equal(root.get("task").get("product").get("category").get("id"), value.intValue()),
        builder.equal(root.get("task").get("product").get("category").get("parent").get("id"),
            value.intValue())
    );
  }

  public static Specification<Subtask> byProduct(final Number value) {
    return (root, query, builder) -> builder.equal(root.get("task").get("product").get("id"),
        value.intValue());
  }

  public static Specification<Subtask> byTask(final Number value) {
    return (root, query, builder) -> builder.equal(root.get("task").get("id"),
        value.intValue());
  }
  
  public static Specification<Subtask> byCategoryPath(String categoryPath) {
    return new Specification<Subtask>() {
      private static final long serialVersionUID = -837414416227802906L;

      @Override
      public Predicate toPredicate(Root<Subtask> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Subquery<Category> categoriesSubquery = query.subquery(Category.class);
        Root<Category> categoryRoot = categoriesSubquery.from(Category.class);
        categoriesSubquery.select(categoryRoot.get("id"));
        categoriesSubquery.where(cb.like(categoryRoot.get("path"), categoryPath + "%"));
        return root.get("task").get("product").get("category").get("id").in(categoriesSubquery);
      }
    };
  }

  private SubtaskSearchSpecifications() {
    // empty constructor
  }

}
