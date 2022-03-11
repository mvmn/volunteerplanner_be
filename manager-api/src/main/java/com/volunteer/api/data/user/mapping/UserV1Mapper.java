package com.volunteer.api.data.user.mapping;

import java.util.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.volunteer.api.data.user.model.api.UserDtoV1;
import com.volunteer.api.data.user.model.persistence.Role;
import com.volunteer.api.data.user.model.persistence.VPUser;

@Mapper(componentModel = "spring")
public abstract class UserV1Mapper {

  @Mapping(target = "role", source = "role.name")
  @Mapping(target = "password", constant = "******")
  public abstract UserDtoV1 map(VPUser dto);

  public abstract VPUser map(UserDtoV1 user);

  public Role map(String roleName) {
    Role result = new Role();
    result.setName(roleName);
    return result;
  }

  public abstract Collection<UserDtoV1> map(Collection<VPUser> data);
}
