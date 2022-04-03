package com.volunteer.api.service;

import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.repository.search.QueryBuilder;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface UserService extends AuthService {

  Page<VPUser> getAll(final QueryBuilder<VPUser> queryBuilder);

  VPUser get(final Integer id);

  Optional<VPUser> getByPrincipal(final String principal);

  VPUser create(final VPUser user);

  VPUser update(final VPUser user);

  void ratingUpdate(final VPUser user, final int delta);

  VPUser ratingReset(final Integer id);

  VPUser verifyPhoneNumber(final Integer id);

  VPUser verifyUser(final Integer id);

  VPUser lock(final Integer id);

  VPUser unlock(final Integer id);

  void passwordChange(final String oldPassword, final String newPassword);

  void passwordReset(final String username);

  void verifyPhoneNumberStart();

  VPUser verifyPhoneNumberComplete(final String code);

}
