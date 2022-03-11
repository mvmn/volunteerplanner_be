package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.user.model.persistence.Role;
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
  public Collection<Role> getAll() {
    return repository.findAll(Sort.by(Order.asc("name")));
  }

  @Override
  public Role get(final String name) {
    final Optional<Role> result = Optional.ofNullable(repository.findByName(name));
    if (result.isPresent()) {
      return result.get();
    }

    final Role role = new Role();
    role.setName(name);

    return save(role);
  }

  @Override
  @Transactional
  public Role save(final Role role) {
    return repository.save(role);
  }
}
