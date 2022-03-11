package com.volunteer.api.data.user.mapping;

import java.util.Collection;
import org.mapstruct.Mapping;
import com.volunteer.api.data.user.model.api.UserV1;
import com.volunteer.api.data.user.model.dto.RoleDto;
import com.volunteer.api.data.user.model.dto.UserDto;

public abstract class UserV1Mapper {

  @Mapping(target = "role", source = "role.name")
  @Mapping(target = "password", constant = "******")
  public abstract UserV1 map(UserDto dto);

  public abstract UserDto map(UserV1 user);

  public RoleDto map(String roleName) {
    RoleDto result = new RoleDto();
    result.setName(roleName);
    return result;
  }

  public abstract Collection<UserV1> map(Collection<UserDto> data);
}
