package com.volunteer.api.data.repository.search.impl;

import com.volunteer.api.data.model.api.search.filter.ValueBoolFilterDto;
import com.volunteer.api.data.model.api.search.filter.ValueTextFilterDto;
import com.volunteer.api.data.model.api.search.sort.SortOrder;
import com.volunteer.api.data.model.api.search.sort.SortParameters;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.specifications.UserSearchSpecifications;
import java.util.Objects;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserQueryBuilder extends AbstractQueryBuilder<VPUser> {

  @Override
  protected Specification<VPUser> buildFilterSpecification(final ValueBoolFilterDto source) {
    final boolean value = source.getValue();
    switch (source.getField().toLowerCase()) {
      case "phoneverified":
        return UserSearchSpecifications.byPhoneNumberVerified(value);
      case "userverified":
        return UserSearchSpecifications.byUserVerified(value);
      case "locked":
        return UserSearchSpecifications.byLocked(value);
    }

    return super.buildFilterSpecification(source);
  }

  @Override
  protected Specification<VPUser> buildFilterSpecification(final ValueTextFilterDto source) {
    final String value = source.getValue().strip();
    switch (source.getField().toLowerCase()) {
      case "phone":
        return UserSearchSpecifications.byPhoneNumber(value);
      case "role.name":
        return UserSearchSpecifications.byRoleName(value);
      case "displayname":
        return UserSearchSpecifications.byDisplayName(value);
    }

    return super.buildFilterSpecification(source);
  }

  @Override
  protected Sort buildSort(final SortParameters sort) {
    if (Objects.isNull(sort)) {
      return Sort.by(Order.asc("displayName"));
    }

    final String entityField;
    switch (sort.getField().toLowerCase()) {
      case "role":
        entityField = "role.name";
        break;
      case "displayname":
        entityField = "displayName";
        break;
      case "rating":
        entityField = "rating";
        break;
      case "phonenumber":
        entityField = "phoneNumber";
        break;
      case "phonenumberverified":
        entityField = "phoneNumberVerified";
        break;
      case "locked":
        entityField = "locked";
        break;
      case "userverifiedby":
        entityField = "userVerifiedBy.displayName";
        break;
      case "userverifiedbyuserid":
        entityField = "userVerifiedByUserId";
        break;
      case "userverified":
        entityField = "userVerified";
        break;
      default:
        throw new IllegalArgumentException(String.format(
            "Sort by '%s' is not supported", sort.getField()));
    }

    return (sort.getOrder() == SortOrder.ASC)
        ? Sort.by(Order.asc(entityField))
        : Sort.by(Order.desc(entityField));
  }

}
