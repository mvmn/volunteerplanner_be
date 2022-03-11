package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.user.model.dto.UserDto;
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
  public List<UserDto> getAll() {
    return repository.findAll(Sort.by(Order.asc("userName")));
  }

  @Override
  public UserDto get(final Integer id) {
    return get(() -> repository.getById(id))
        .orElseThrow(() -> new ObjectNotFoundException(String.format(
            "User with ID '%d' does not exist", id)));
  }

  @Override
  public Optional<UserDto> get(final String userName) {
    return get(() -> repository.findByUserName(userName));
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    final UserDto user = get(username).orElseThrow(
        () -> new UsernameNotFoundException(String.format(
            "User with name '%s' does not exist", username)));

    final Collection<SimpleGrantedAuthority> authorities = List.of(
        new SimpleGrantedAuthority(user.getRole().getName()));

    return new User(user.getUserName(), user.getPassword(), authorities);
  }

  @Override
  @Transactional
  public UserDto create(final UserDto user) {
    final Optional<UserDto> current = get(user.getUserName());
    if (current.isPresent()) {
      throw new ObjectAlreadyExistsException(String.format(
          "User with '%s' username already exists", user.getUserName()));
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(roleService.get(user.getRole().getName()));

    // TODO: add address support
    return repository.save(user);
  }

  public UserDto update(final UserDto user) {
    return null;
  }

  public UserDto changePassword(final Integer id, final String oldPassword,
      final String newPassword) {
    final UserDto user = get(id);
    if (!user.getPassword().equals(passwordEncoder.encode(oldPassword))) {
      throw new InvalidPasswordException();
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    return user;
  }

  private Optional<UserDto> get(final Supplier<UserDto> supplier) {
    return Optional.ofNullable(supplier.get());
  }

}
