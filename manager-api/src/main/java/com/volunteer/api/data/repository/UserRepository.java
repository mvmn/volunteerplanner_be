package com.volunteer.api.data.repository;

import com.volunteer.api.data.model.persistence.VPUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
    extends JpaRepository<VPUser, Integer>, JpaSpecificationExecutor<VPUser> {

  VPUser findByUserName(final String userName);

}
