package com.volunteer.api.data.user.repository;

import com.volunteer.api.data.user.model.persistence.VPUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<VPUser, Integer> {

  VPUser findByUserName(final String userName);

}
