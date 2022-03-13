package com.volunteer.api.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.volunteer.api.data.model.persistence.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

  Role findByName(final String name);

}
