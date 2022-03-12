package com.volunteer.api.data.user.service;

import com.volunteer.api.data.user.model.persistence.VPUser;
import java.util.List;
import java.util.Optional;

public interface UserService {

  List<VPUser> getAll();

  VPUser get(final Integer id);

  Optional<VPUser> get(final String userName);

  VPUser create(final VPUser user);

  VPUser update(final VPUser user);

  VPUser verifyUser(final Integer id);

  VPUser lock(final Integer id);

  VPUser unlock(final Integer id);

  void passwordChange(final String oldPassword, final String newPassword);

  void passwordReset(final String username);

  void verifyPhoneNumberStart();

  VPUser verifyPhoneNumberComplete(final String code);

}
