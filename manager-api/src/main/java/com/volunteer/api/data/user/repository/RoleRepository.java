package com.volunteer.api.data.user.repository;

import com.volunteer.api.data.user.model.dto.RoleDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleDto, Integer> {

  RoleDto findByName(final String name);

}
