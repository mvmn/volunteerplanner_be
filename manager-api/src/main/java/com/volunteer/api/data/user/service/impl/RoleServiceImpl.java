package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.user.model.dto.RoleDto;
import com.volunteer.api.data.user.repository.RoleRepository;
import com.volunteer.api.data.user.service.RoleService;
import com.volunteer.api.error.ObjectNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository repository;

  public RoleServiceImpl(final RoleRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<RoleDto> getAll() {
    return repository.findAll(Sort.by(Order.asc("name")));
  }

  @Override
  public RoleDto get(final String name) {
    return Optional.ofNullable(repository.findByName(name))
        .orElseThrow(() -> new ObjectNotFoundException(
            String.format("Role '%s' does not exist", name)));
  }

}
