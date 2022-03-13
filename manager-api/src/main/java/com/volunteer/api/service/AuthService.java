package com.volunteer.api.service;

import com.volunteer.api.data.model.persistence.VPUser;


public interface AuthService {

  VPUser getCurrentUser();

}
