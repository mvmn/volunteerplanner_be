package com.volunteer.api.data.user.service;

import java.util.List;
import java.util.Optional;
import com.volunteer.api.data.user.model.persistence.VPUser;

public interface UserService {

  List<VPUser> getAll();

  VPUser get(final Integer id);

  Optional<VPUser> get(final String userName);

  VPUser create(final VPUser user);

  VPUser update(final VPUser user);

  VPUser changePassword(final Integer id, final String oldPassword, final String newPassword);

}
