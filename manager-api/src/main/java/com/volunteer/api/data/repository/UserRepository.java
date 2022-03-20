package com.volunteer.api.data.repository;

import com.volunteer.api.data.model.persistence.VPUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
    extends JpaRepository<VPUser, Integer>, JpaSpecificationExecutor<VPUser> {

  VPUser findByUserName(final String userName);

  @Query("select case when count(u) > 0 then true else false end from User u where u.role.name = ?1")
  boolean existsWithRole(final String roleName);

}
