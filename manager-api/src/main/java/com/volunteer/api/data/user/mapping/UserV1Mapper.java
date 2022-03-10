package com.volunteer.api.data.user.mapping;

import com.volunteer.api.data.user.model.api.UserV1;
import com.volunteer.api.data.user.model.dto.RoleDto;
import com.volunteer.api.data.user.model.dto.UserDto;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public final class UserV1Mapper {

  public static Collection<UserV1> map(final Collection<UserDto> data) {
    if (CollectionUtils.isEmpty(data)) {
      return Collections.emptyList();
    }

    return data.stream()
        .map(UserV1Mapper::map)
        .collect(Collectors.toList());
  }

  public static UserV1 map(final UserDto user) {
    final UserV1 result = new UserV1();

    result.setId(user.getId());
    result.setUserName(user.getUserName());
    result.setPhoneNumber(user.getPhoneNumber());
    result.setPassword("******");

    result.setRole(user.getRole().getName());

    result.setFullName(user.getFullName());
    result.setEmail(user.getEmail());

    return result;
  }

  public static UserDto map(final UserV1 source) {
    final UserDto result = new UserDto();

    result.setUserName(source.getUserName());
    result.setPhoneNumber(source.getPhoneNumber());
    result.setPassword(source.getPassword());

    final RoleDto role = new RoleDto();
    role.setName(source.getRole());
    result.setRole(role);

    result.setFullName(source.getFullName());
    result.setEmail(source.getEmail());

    return result;
  }

  private UserV1Mapper() {
    // empty constructor
  }

}
