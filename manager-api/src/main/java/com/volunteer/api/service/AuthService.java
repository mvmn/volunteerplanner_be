package com.volunteer.api.service;

import java.util.Set;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.volunteer.api.data.model.persistence.VPUser;


public interface AuthService extends UserDetailsService {

  VPUser getCurrentUser();

  String getCurrentPrincipal();

  Set<String> getCurrentUserRoles();

}
