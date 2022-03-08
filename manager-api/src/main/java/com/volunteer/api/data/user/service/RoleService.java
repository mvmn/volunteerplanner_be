package com.volunteer.api.data.user.service;

import com.volunteer.api.data.user.model.dto.RoleDto;
import java.util.Collection;

public interface RoleService {

  Collection<RoleDto> getAll();

  RoleDto get(final String name);

  RoleDto save(final RoleDto role);

}
