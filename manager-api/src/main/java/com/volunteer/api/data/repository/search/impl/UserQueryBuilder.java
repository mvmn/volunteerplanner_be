package com.volunteer.api.data.repository.search.impl;

import com.volunteer.api.data.model.api.search.filter.ValueBoolFilterDto;
import com.volunteer.api.data.model.api.search.filter.ValueTextFilterDto;
import com.volunteer.api.data.model.api.search.sort.SortOrder;
import com.volunteer.api.data.model.api.search.sort.SortParameters;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.specifications.UserSearchSpecifications;
import java.util.Objects;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component
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
      case "username":
        return UserSearchSpecifications.byUserName(value);
      case "phone":
        return UserSearchSpecifications.byPhoneNumber(value);
      case "role.name":
        return UserSearchSpecifications.byRoleName(value);
      case "fullname":
        return UserSearchSpecifications.byFullName(value);
      case "email":
        return UserSearchSpecifications.byEmail(value);
      case "address.region":
        return UserSearchSpecifications.byAddressRegion(value);
      case "address.city":
        return UserSearchSpecifications.byAddressCity(value);
    }

    return super.buildFilterSpecification(source);
  }

  @Override
  protected Sort buildSort(final SortParameters sort) {
    if (Objects.isNull(sort)) {
      return Sort.by(Order.asc("userName"));
    }

    final String entityField;
    switch (sort.getField().toLowerCase()) {
      case "username":
        entityField = "userName";
        break;
      case "role.name":
        entityField = "role.name";
        break;
      case "fullname":
        entityField = "fullName";
        break;
      case "address.region":
        entityField = "address.region";
        break;
      case "address.city":
        entityField = "address.city";
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
