package com.volunteer.api.data.model.persistence.specifications;

import com.volunteer.api.data.model.persistence.VPUser;
import org.springframework.data.jpa.domain.Specification;

public final class UserSearchSpecifications {

  public static Specification<VPUser> byUserName(final String value) {
    return (root, query, builder) -> builder.like(root.get("userName"), "%" + value + "%");
  }

  public static Specification<VPUser> byPhoneNumber(final String value) {
    return (root, query, builder) -> builder.like(root.get("phoneNumber"), "%" + value + "%"
    );
  }

  public static Specification<VPUser> byRoleName(final String value) {
    return (root, query, builder) -> builder.equal(root.get("role").get("name"), value);
  }

  public static Specification<VPUser> byFullName(final String value) {
    return (root, query, builder) -> builder.like(root.get("fullName"), "%" + value + "%");
  }

  public static Specification<VPUser> byEmail(final String value) {
    return (root, query, builder) -> builder.equal(root.get("role").get("name"), value);
  }

  public static Specification<VPUser> byAddressRegion(final String value) {
    return (root, query, builder) -> builder.like(root.get("address").get("region"),
        "%" + value + "%");
  }

  public static Specification<VPUser> byAddressCity(final String value) {
    return (root, query, builder) -> builder.like(root.get("address").get("city"),
        "%" + value + "%");
  }

  public static Specification<VPUser> byPhoneNumberVerified(final Boolean value) {
    return (root, query, builder) -> builder.equal(root.get("phoneNumberVerified"), value);
  }

  public static Specification<VPUser> byUserVerified(final Boolean value) {
    return (root, query, builder) -> builder.equal(root.get("userVerified"), value);
  }

  public static Specification<VPUser> byLocked(final Boolean value) {
    return (root, query, builder) -> builder.equal(root.get("locked"), value);
  }

  private UserSearchSpecifications() {
    // empty constructor
  }

}
