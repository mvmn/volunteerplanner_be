package com.volunteer.api.data.user.service;

import java.util.List;
import com.volunteer.api.data.user.model.persistence.Role;

public interface RoleService {

  List<Role> getAll();

  Role get(final String name);

  Role save(final Role role);

}
