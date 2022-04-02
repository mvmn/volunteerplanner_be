package com.volunteer.api.service;

import java.util.Set;
import com.volunteer.api.data.model.persistence.VPUser;


public interface AuthService {

  VPUser getCurrentUser();

  String getCurrentPrincipal();

  Set<String> getCurrentUserRoles();

}
