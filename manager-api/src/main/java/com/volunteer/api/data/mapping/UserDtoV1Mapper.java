package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.UpdateCurrentUserDtoV1;
import com.volunteer.api.data.model.api.UserDtoV1;
import com.volunteer.api.data.model.persistence.Role;
import com.volunteer.api.data.model.persistence.VPUser;
import java.util.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = GenericMapper.class)
public abstract class UserDtoV1Mapper {

  @Mapping(target = "role", source = "role.name")
  @Mapping(target = "password", constant = "******")
  @Mapping(target = "userVerifiedBy", source = "userVerifiedBy.displayName")
  public abstract UserDtoV1 map(final VPUser entity);
  
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "role", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "rating", ignore = true)
  @Mapping(target = "phoneNumberVerified", ignore = true)
  @Mapping(target = "userVerified", ignore = true)
  @Mapping(target = "userVerifiedBy", ignore = true)
  @Mapping(target = "userVerifiedByUserId", ignore = true)
  @Mapping(target = "userVerifiedAt", ignore = true)
  @Mapping(target = "locked", ignore = true)
  @Mapping(target = "lockedBy", ignore = true)
  @Mapping(target = "lockedByUserId", ignore = true)
  @Mapping(target = "lockedAt", ignore = true)
  public abstract VPUser map(final UpdateCurrentUserDtoV1 dto);

  @Mapping(target = "version", ignore = true)
  @Mapping(target = "rating", ignore = true)
  @Mapping(target = "phoneNumberVerified", ignore = true)
  @Mapping(target = "userVerified", ignore = true)
  @Mapping(target = "userVerifiedBy", ignore = true)
  @Mapping(target = "userVerifiedByUserId", ignore = true)
  @Mapping(target = "userVerifiedAt", ignore = true)
  @Mapping(target = "locked", ignore = true)
  @Mapping(target = "lockedBy", ignore = true)
  @Mapping(target = "lockedByUserId", ignore = true)
  @Mapping(target = "lockedAt", ignore = true)
  public abstract VPUser map(final UserDtoV1 dto);

  public Role map(final String roleName) {
    final Role result = new Role();
    result.setName(roleName);
    return result;
  }

  public abstract Collection<UserDtoV1> map(Collection<VPUser> data);
}
