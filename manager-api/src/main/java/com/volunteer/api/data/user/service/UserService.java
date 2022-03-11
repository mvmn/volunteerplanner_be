package com.volunteer.api.data.user.service;

import com.volunteer.api.data.user.model.dto.UserDto;
import java.util.List;
import java.util.Optional;

public interface UserService {

  List<UserDto> getAll();

  UserDto get(final Integer id);

  Optional<UserDto> get(final String userName);

  UserDto create(final UserDto user);

  UserDto update(final UserDto user);

  UserDto changePassword(final Integer id, final String oldPassword, final String newPassword);

}
