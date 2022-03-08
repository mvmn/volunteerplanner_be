package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.user.model.dto.RoleDto;
import com.volunteer.api.data.user.repository.RoleRepository;
import com.volunteer.api.data.user.service.RoleService;
import java.util.Collection;
import java.util.Optional;
import javax.transaction.Transactional;
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
  public Collection<RoleDto> getAll() {
    return repository.findAll(Sort.by(Order.asc("name")));
  }

  @Override
  public RoleDto get(final String name) {
    final Optional<RoleDto> result = Optional.ofNullable(repository.findByName(name));
    if (result.isPresent()) {
      return result.get();
    }

    final RoleDto role = new RoleDto();
    role.setName(name);

    return save(role);
  }

  @Override
  @Transactional
  public RoleDto save(final RoleDto role) {
    return repository.save(role);
  }
}
