package com.volunteer.api.data.user.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.volunteer.api.data.user.model.persistence.VPUser;
import com.volunteer.api.data.user.service.AuthService;
import com.volunteer.api.data.user.service.UserService;
import com.volunteer.api.error.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserService userService;

  @Override
  public VPUser getCurrentUser() {
    Object currentUserName = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (currentUserName != null) {
      return userService.get(currentUserName.toString())
          .orElseThrow(() -> new ObjectNotFoundException("Missing user " + currentUserName));
    }
    return null;
  }
}
