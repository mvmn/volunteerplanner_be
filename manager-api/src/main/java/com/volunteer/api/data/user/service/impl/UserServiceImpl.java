package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.user.model.persistence.VPUser;
import com.volunteer.api.data.user.repository.UserRepository;
import com.volunteer.api.data.user.service.AddressService;
import com.volunteer.api.data.user.service.AuthService;
import com.volunteer.api.data.user.service.RoleService;
import com.volunteer.api.data.user.service.UserService;
import com.volunteer.api.error.InvalidPasswordException;
import com.volunteer.api.error.ObjectAlreadyExistsException;
import com.volunteer.api.error.ObjectNotFoundException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService, AuthService {

  private final UserRepository repository;

  private final PasswordEncoder passwordEncoder;
  private final RoleService roleService;
  private final AddressService addressService;

  // TODO: add pagination and order / filter support
  public List<VPUser> getAll() {
    return repository.findAll(Sort.by(Order.asc("userName")));
  }

  @Override
  public VPUser get(final Integer id) {
    return get(() -> repository.getById(id))
        .orElseThrow(() -> new ObjectNotFoundException(String.format(
            "User with ID '%d' does not exist", id)));
  }

  @Override
  public Optional<VPUser> get(final String userName) {
    return get(() -> repository.findByUserName(userName));
  }

  @Override
  // moved here to avoid circular dependencies
  public VPUser getCurrentUser() {
    final String userName = (String) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    if (StringUtils.isEmpty(userName)) {
      throw new IllegalStateException("Username is missed in security context");
    }

    return get(userName)
        .orElseThrow(() -> new ObjectNotFoundException("Missing user " + userName));
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    final VPUser user = get(username).orElseThrow(
        () -> new UsernameNotFoundException(String.format(
            "User with name '%s' does not exist", username)));

    final Collection<SimpleGrantedAuthority> authorities = List.of(
        new SimpleGrantedAuthority(user.getRole().getName()));

    return new User(user.getUserName(), user.getPassword(), true, true,
        true, !user.isLocked(), authorities);
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
    user.setAddress(addressService.getOrCreate(user.getAddress()));

    try {
      return repository.save(user);
    } catch (final Exception exception) {
      throw exception;
    }
  }

  @Override
  @Transactional
  public VPUser update(final VPUser user) {
    final VPUser current = getCurrentUser();

    if (!Objects.equals(current.getUserName(), user.getUserName())) {
      throw new IllegalArgumentException("username change is not allowed");
    }

    if (!Objects.equals(current.getPhoneNumber(), user.getPhoneNumber())) {
      current.setPhoneNumber(user.getPhoneNumber());
      current.setPhoneNumberVerified(false);
    }

    current.setFullName(user.getFullName());
    current.setEmail(user.getEmail());
    current.setAddress(addressService.getOrCreate(user.getAddress()));

    return repository.save(current);
  }

  @Override
  public VPUser verifyUser(final Integer id) {
    final VPUser source = get(id);
    if (source.isUserVerified()) {
      return source;
    }

    final VPUser current = getCurrentUser();
    if (Objects.equals(source.getId(), current.getId())) {
      throw new AuthorizationServiceException("Can't verify himself");
    }

    source.setUserVerified(true);
    source.setUserVerifiedBy(current);
    source.setUserVerifiedByUserId(current.getId());
    source.setUserVerifiedAt(ZonedDateTime.now());

    return repository.save(source);
  }

  @Override
  public VPUser lock(final Integer id) {
    final VPUser source = get(id);
    if (source.isLocked()) {
      return source;
    }

    final VPUser current = getCurrentUser();
    // we won't prohibit to lock oneself if user has an authority to do so

    source.setLocked(true);
    source.setLockedBy(current);
    source.setLockedByUserId(current.getId());
    source.setLockedAt(ZonedDateTime.now());

    return repository.save(source);
  }

  @Override
  public VPUser unlock(final Integer id) {
    final VPUser source = get(id);
    if (!source.isLocked()) {
      return source;
    }

    final VPUser current = getCurrentUser();
    if (Objects.equals(source.getId(), current.getId())) {
      throw new AuthorizationServiceException("Can't unlock himself");
    }

    source.setLocked(false);
    source.setLockedBy(null);
    source.setLockedAt(null);

    return repository.save(source);
  }

  @Override
  public void passwordChange(final String oldPassword, final String newPassword) {
    final VPUser user = getCurrentUser();
    if (!user.getPassword().equals(passwordEncoder.encode(oldPassword))) {
      throw new InvalidPasswordException();
    }

    user.setPassword(passwordEncoder.encode(newPassword));
  }

  @Override
  public void passwordReset(final String username) {
    final VPUser source = get(username).orElseThrow(() -> new ObjectNotFoundException(
        String.format("User '%s' does not exist yet")));

    // generate random password & change it
    // use sms to supply it to user
  }

  @Override
  public void verifyPhoneNumberStart() {
    final VPUser current = getCurrentUser();
    if (current.isPhoneNumberVerified()) {
      throw new IllegalStateException("Phone number has been verified already");
    }

    // generate code & put into the cache
    // call sms service
  }

  @Override
  public VPUser verifyPhoneNumberComplete(final String code) {
    final VPUser current = getCurrentUser();

    // get code from cache & compare
    return current;
  }

  private Optional<VPUser> get(final Supplier<VPUser> supplier) {
    return Optional.ofNullable(supplier.get());
  }

}
