package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.UserDtoV1;
import com.volunteer.api.data.model.persistence.VPUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserViewDtoV1Mapper {

  @Mapping(target = "phoneNumber", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "role", ignore = true)
  @Mapping(target = "phoneNumberVerified", ignore = true)
  @Mapping(target = "userVerified", ignore = true)
  @Mapping(target = "userVerifiedByUserId", ignore = true)
  @Mapping(target = "userVerifiedAt", ignore = true)
  @Mapping(target = "locked", ignore = true)
  @Mapping(target = "lockedByUserId", ignore = true)
  @Mapping(target = "lockedAt", ignore = true)
  UserDtoV1 map(final VPUser source);

}
