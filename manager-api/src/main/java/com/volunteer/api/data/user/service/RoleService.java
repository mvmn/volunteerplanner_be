package com.volunteer.api.data.user.service;

import java.util.Collection;
import com.volunteer.api.data.user.model.persistence.Role;

public interface RoleService {

  Collection<Role> getAll();

  Role get(final String name);

  Role save(final Role role);

}
