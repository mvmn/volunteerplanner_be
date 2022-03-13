package com.volunteer.api.data.mapping;

import java.util.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.volunteer.api.data.model.api.UserDtoV1;
import com.volunteer.api.data.model.persistence.Role;
import com.volunteer.api.data.model.persistence.VPUser;

@Mapper(componentModel = "spring")
public abstract class UserV1Mapper {

  @Mapping(target = "role", source = "role.name")
  @Mapping(target = "password", constant = "******")

  public abstract UserDtoV1 map(VPUser dto);


  @Mapping(target = "version", ignore = true)
  @Mapping(target = "phoneNumberVerified", ignore = true)
  @Mapping(target = "userVerified", ignore = true)
  @Mapping(target = "userVerifiedBy", ignore = true)
  @Mapping(target = "userVerifiedByUserId", ignore = true)
  @Mapping(target = "userVerifiedAt", ignore = true)
  @Mapping(target = "locked", ignore = true)
  @Mapping(target = "lockedBy", ignore = true)
  @Mapping(target = "lockedByUserId", ignore = true)
  @Mapping(target = "lockedAt", ignore = true)
  public abstract VPUser map(UserDtoV1 user);

  public Role map(String roleName) {
    Role result = new Role();
    result.setName(roleName);
    return result;
  }

  public abstract Collection<UserDtoV1> map(Collection<VPUser> data);
}
