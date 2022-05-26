package com.volunteer.api.data.model.persistence.specifications;

import com.volunteer.api.data.model.persistence.VPUser;
import org.springframework.data.jpa.domain.Specification;

public final class UserSearchSpecifications {

  public static Specification<VPUser> byPhoneNumber(final String value) {
    return (root, query, builder) -> builder.like(builder.lower(root.get("phoneNumber")), "%" + value.toLowerCase() + "%"
    );
  }

  public static Specification<VPUser> byRoleName(final String value) {
    return (root, query, builder) -> builder.equal(root.get("role").get("name"), value);
  }

  public static Specification<VPUser> byDisplayName(final String value) {
    return (root, query, builder) -> builder.like(builder.lower(root.get("displayName")), "%" + value.toLowerCase() + "%");
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
