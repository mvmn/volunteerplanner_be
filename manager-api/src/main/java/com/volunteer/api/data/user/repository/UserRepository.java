package com.volunteer.api.data.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.volunteer.api.data.user.model.persistence.VPUser;

@Repository
public interface UserRepository extends JpaRepository<VPUser, Integer> {

  VPUser findByUserName(final String userName);

}
