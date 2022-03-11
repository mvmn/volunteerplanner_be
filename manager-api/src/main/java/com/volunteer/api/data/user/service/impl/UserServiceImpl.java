package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.user.model.persistence.VPUser;
import com.volunteer.api.data.user.repository.UserRepository;
import com.volunteer.api.data.user.service.RoleService;
import com.volunteer.api.data.user.service.UserService;
import com.volunteer.api.error.InvalidPasswordException;
import com.volunteer.api.error.ObjectAlreadyExistsException;
import com.volunteer.api.error.ObjectNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import javax.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

  private final UserRepository repository;

  private final PasswordEncoder passwordEncoder;
  private final RoleService roleService;

  public UserServiceImpl(final UserRepository repository, final PasswordEncoder passwordEncoder,
      final RoleService roleService) {
    this.repository = repository;

    this.passwordEncoder = passwordEncoder;
    this.roleService = roleService;
  }

  // TODO: add pagination and order / filter support
  public List<VPUser> getAll() {
    return repository.findAll(Sort.by(Order.asc("userName")));
  }

  @Override
  public Optional<VPUser> get(final Integer id) {
    return get(() -> repository.getById(id));
  }

  @Override
  public Optional<VPUser> get(final String userName) {
    return get(() -> repository.findByUserName(userName));
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    final VPUser user = get(username).orElseThrow(
        () -> new UsernameNotFoundException(String.format(
            "User with name '%s' does not exist", username)));

    final Collection<SimpleGrantedAuthority> authorities = List.of(
        new SimpleGrantedAuthority(user.getRole().getName()));

    return new User(user.getUserName(), user.getPassword(), authorities);
  }

  @Override
  @Transactional
  public VPUser create(final VPUser user) {
    final Optional<VPUser> current = get(user.getUserName());
    if (current.isPresent()) {
      throw new ObjectAlreadyExistsException(String.format(
          "User with '%s' username already exists", user.getUserName()));
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(roleService.get(user.getRole().getName()));

    // TODO: add address support
    return repository.save(user);
  }

  public VPUser update(final VPUser user) {
    return null;
  }

  public VPUser changePassword(final Integer id, final String oldPassword,
      final String newPassword) {
    final VPUser user = get(id).orElseThrow(
        () -> new ObjectNotFoundException(String.format(
            "User with id '%d' does not exist", id)));

    if (!user.getPassword().equals(passwordEncoder.encode(oldPassword))) {
      throw new InvalidPasswordException();
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    return user;
  }

  private Optional<VPUser> get(final Supplier<VPUser> supplier) {
    return Optional.ofNullable(supplier.get());
  }

}
