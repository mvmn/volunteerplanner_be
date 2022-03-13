package com.volunteer.api.service.impl;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import com.volunteer.api.data.model.persistence.Role;
import com.volunteer.api.data.repository.RoleRepository;
import com.volunteer.api.error.ObjectNotFoundException;
import com.volunteer.api.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository repository;

  public RoleServiceImpl(final RoleRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Role> getAll() {
    return repository.findAll(Sort.by(Order.asc("name")));
  }

  @Override
  @Transactional
  public Role save(final Role role) {
    return repository.save(role);
  }

  @Override
  public Role get(final String name) {
    return Optional.ofNullable(repository.findByName(name)).orElseThrow(
        () -> new ObjectNotFoundException(String.format("Role '%s' does not exist", name)));
  }

}
