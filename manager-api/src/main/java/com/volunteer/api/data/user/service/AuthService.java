package com.volunteer.api.data.user.service;

import com.volunteer.api.data.user.model.persistence.VPUser;


public interface AuthService {

  VPUser getCurrentUser();

}
