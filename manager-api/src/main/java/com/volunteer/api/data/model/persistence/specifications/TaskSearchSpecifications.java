package com.volunteer.api.data.model.persistence.specifications;

import com.volunteer.api.data.model.persistence.Category;
import com.volunteer.api.data.model.persistence.Task;
import java.time.ZonedDateTime;
import java.util.Collection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import com.volunteer.api.data.model.TaskStatus;

public class TaskSearchSpecifications {

  public static <T> Specification<T> addSpec(Specification<T> spec, Specification<T> nextSpec) {
    if (spec == null) {
      return nextSpec != null ? Specification.where(nextSpec) : null;
    } else {
      return nextSpec != null ? spec.and(nextSpec) : spec;
    }
  }

  public static Specification<Task> byCustomer(String customer) {
    return new Specification<Task>() {
      private static final long serialVersionUID = 4329119324673056135L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return StringUtils.isNotBlank(customer)
            ? cb.like(cb.lower(root.get("customer")), "%" + customer.trim().toLowerCase() + "%")
            : null;
      }
    };
  }

  public static Specification<Task> byProductId(int productId) {
    return new Specification<Task>() {
      private static final long serialVersionUID = 3010702608721940054L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("product").get("id"), productId);
      }
    };
  }

  public static Specification<Task> byCategories(Collection<Integer> categoryId) {
    return new Specification<Task>() {
      private static final long serialVersionUID = 7189563626300610108L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return root.get("product").get("category").get("id").in(categoryId);
      }
    };
  }

  public static Specification<Task> byVolunteerStoreId(int volunteerStoreId) {
    return new Specification<Task>() {
      private static final long serialVersionUID = 1L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("volunteerStore").get("id"), volunteerStoreId);
      }
    };
  }

  public static Specification<Task> byCustomerStoreId(int customerStoreId) {
    return new Specification<Task>() {
      private static final long serialVersionUID = 1L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("customerStore").get("id"), customerStoreId);
      }
    };
  }

  public static Specification<Task> byStatuses(Collection<TaskStatus> statuses) {
    return new Specification<Task>() {
      private static final long serialVersionUID = 3141880585252774578L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return root.get("status").in(statuses);
      }
    };
  }

  public static Specification<Task> byRemainingQuantityGreaterThan(int belowMinRemainingQuantity) {
    return new Specification<Task>() {
      private static final long serialVersionUID = 4869709261462751877L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.greaterThan(root.get("quantityLeft"), belowMinRemainingQuantity);
      }
    };
  }

  public static Specification<Task> byRemainingQuantityZero() {
    return new Specification<Task>() {
      private static final long serialVersionUID = 4869709261462751877L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("quantityLeft"), 0);
      }
    };
  }

  public static Specification<Task> byDueDateAfter(int dueDateFrom) {
    return new Specification<Task>() {
      private static final long serialVersionUID = -2295925996568209112L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.greaterThan(root.get("deadlineDate"), dueDateFrom);
      }
    };
  }

  public static Specification<Task> byNonExpired() {
    return new Specification<Task>() {
      private static final long serialVersionUID = -3637567343534731154L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.greaterThanOrEqualTo(root.get("deadlineDate"), ZonedDateTime.now());
      }
    };
  }


  public static Specification<Task> byCreator(Integer createdByUserId) {
    return new Specification<Task>() {
      private static final long serialVersionUID = -5516669171889765780L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("createdBy").get("id"), createdByUserId);
      }
    };
  }


  public static Specification<Task> byVerifier(Integer verifiedByUserId) {
    return new Specification<Task>() {
      private static final long serialVersionUID = -2845603886993572577L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("verifiedBy").get("id"), verifiedByUserId);
      }
    };
  }


  public static Specification<Task> byCloser(Integer closedByUserId) {
    return new Specification<Task>() {
      private static final long serialVersionUID = 8821494023479815206L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("closedBy").get("id"), closedByUserId);
      }
    };
  }

  public static Specification<Task> byCategoryPath(String categoryPath) {
    return new Specification<Task>() {
      private static final long serialVersionUID = 8929316547592591148L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Subquery<Category> categoriesSubquery = query.subquery(Category.class);
        Root<Category> categoryRoot = categoriesSubquery.from(Category.class);
        categoriesSubquery.select(categoryRoot.get("id"));
        categoriesSubquery.where(cb.like(categoryRoot.get("path"), categoryPath + "%"));
        return root.get("product").get("category").get("id").in(categoriesSubquery);
      }
    };
  }

  public static Specification<Task> byProductName(String productName) {
    return new Specification<Task>() {
      private static final long serialVersionUID = -1773760051893744509L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return StringUtils.isNotBlank(productName)
            ? cb.like(cb.lower(root.get("product").get("name")),
                "%" + productName.trim().toLowerCase() + "%")
            : null;
      }
    };
  }

  public static Specification<Task> byNote(String note) {
    return new Specification<Task>() {
      private static final long serialVersionUID = 7609707639991817576L;

      @Override
      public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return StringUtils.isNotBlank(note)
            ? cb.like(cb.lower(root.get("note")), "%" + note.trim().toLowerCase() + "%")
            : null;
      }
    };
  }
}
