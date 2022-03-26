package com.volunteer.api.service.impl;

import com.volunteer.api.data.model.UserRole;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;
import com.volunteer.api.data.repository.UserRepository;
import com.volunteer.api.data.repository.search.Query;
import com.volunteer.api.data.repository.search.QueryBuilder;
import com.volunteer.api.error.InvalidPasswordException;
import com.volunteer.api.error.ObjectNotFoundException;
import com.volunteer.api.service.AddressService;
import com.volunteer.api.service.RoleService;
import com.volunteer.api.service.SmsService;
import com.volunteer.api.service.UserService;
import com.volunteer.api.service.VerificationCodeService;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final RoleService roleService;
  private final AddressService addressService;
  private final VerificationCodeService verificationCodeService;
  private final SmsService smsService;

  public Page<VPUser> getAll(final QueryBuilder<VPUser> queryBuilder) {
    final Query<VPUser> query = queryBuilder.build();
    return repository.findAll(query.getSpecification(), query.getPageable());
  }

  @Override
  public VPUser get(final Integer id) {
    return get(() -> repository.getById(id)).orElseThrow(
        () -> new ObjectNotFoundException(String.format("User with ID '%d' does not exist", id)));
  }

  @Override
  public Optional<VPUser> get(final String userName) {
    return get(() -> repository.findByUserName(userName));
  }

  @Override
  public String getCurrentUserName() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth != null ? (String) auth.getPrincipal() : null;
  }

  @Override
  public Set<String> getCurrentUserRoles() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth != null ? auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .collect(Collectors.toCollection(TreeSet::new)) : Collections.emptySet();
  }

  @Override
  // moved here to avoid circular dependencies
  public VPUser getCurrentUser() {
    String userName = getCurrentUserName();
    if (StringUtils.isEmpty(userName)) {
      throw new IllegalStateException("Username is missed in security context");
    }

    return get(userName).orElseThrow(() -> new ObjectNotFoundException("Missing user " + userName));
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    final VPUser user = get(username).orElseThrow(
        () -> new UsernameNotFoundException(String.format(
            "User with name '%s' does not exist", username)));

    final Collection<SimpleGrantedAuthority> authorities;
    if (isVerified(user)) {
      final UserRole role = UserRole.forValue(user.getRole().getName());
      authorities = role.getAuthorities().stream()
          .map(authority -> new SimpleGrantedAuthority(authority.name()))
          .collect(Collectors.toSet());
    } else {
      authorities = Collections.emptySet();
    }

    return new User(user.getUserName(), user.getPassword(), true, true,
        true, !user.isLocked(), authorities);
  }

  @Override
  @Transactional
  public VPUser create(final VPUser user) {
    // userName uniqueness detection moved on DB layer
    // proper exception handling is implemented

    user.setRole(roleService.get(user.getRole().getName()));
    final boolean isRootUser = user.getRole().getName().equalsIgnoreCase(UserRole.ROOT.getValue());

    // it's allowed to create only one ROOT
    if (isRootUser && repository.existsWithRole(UserRole.ROOT.getValue())) {
      throw new IllegalArgumentException("It's not allowed to create user with root privileges");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    VPUser result = repository.save(user);
    if (!isRootUser) {
      return result;
    }

    result.setPhoneNumberVerified(true);
    result.setUserVerified(true);
    result.setUserVerifiedBy(result);
    result.setUserVerifiedByUserId(result.getId());
    result.setUserVerifiedAt(ZonedDateTime.now());

    return repository.save(result);
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

    return repository.save(current);
  }

  @Override
  public VPUser verifyPhoneNumber(final Integer id) {
    final VPUser source = get(id);
    if (source.isPhoneNumberVerified()) {
      return source;
    }

    source.setPhoneNumberVerified(true);
    return repository.save(source);
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
    source.setLockedByUserId(null);
    source.setLockedAt(null);

    return repository.save(source);
  }

  @Override
  public void passwordChange(final String oldPassword, final String newPassword) {
    final VPUser user = getCurrentUser();
    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new InvalidPasswordException();
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    repository.save(user);
  }

  @Override
  public void passwordReset(final String username) {
    // final VPUser source = get(username).orElseThrow(() -> new ObjectNotFoundException(
    // String.format("User '%s' does not exist yet")));

    // generate random password & change it
    // use sms to supply it to user
  }

  @Override
  public void verifyPhoneNumberStart() {
    final VPUser current = getCurrentUser();
    if (current.isPhoneNumberVerified()) {
      throw new IllegalStateException("Phone number has been verified already");
    }

    Pair<Boolean, String> verificationCode =
        verificationCodeService.getOrCreate(current, VerificationCodeType.PHONE);
    if (verificationCode.getKey()) {
      // Only send if it's newly created. Don't send same code twice
      smsService.send(current, "Kod veryfikatsiji: " + verificationCode.getValue());
    }
  }

  @Override
  @Transactional
  public VPUser verifyPhoneNumberComplete(final String code) {
    final VPUser current = getCurrentUser();

    // get code from cache & compare
    if (!verificationCodeService.matches(current, code, VerificationCodeType.PHONE)) {
      throw new IllegalArgumentException("Verification code doesn't match");
    }
    current.setPhoneNumberVerified(true);
    verificationCodeService.cleanup(current, VerificationCodeType.PHONE);

    return repository.save(current);
  }

  private boolean isVerified(final VPUser user) {
    return user.isPhoneNumberVerified() && user.isUserVerified();
  }

  private Optional<VPUser> get(final Supplier<VPUser> supplier) {
    return Optional.ofNullable(supplier.get());
  }

}
