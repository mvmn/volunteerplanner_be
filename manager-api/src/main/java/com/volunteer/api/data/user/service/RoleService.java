package com.volunteer.api.data.user.service;

import com.volunteer.api.data.user.model.dto.RoleDto;
import java.util.Collection;
import java.util.List;

public interface RoleService {

  List<RoleDto> getAll();

  RoleDto get(final String name);

}
