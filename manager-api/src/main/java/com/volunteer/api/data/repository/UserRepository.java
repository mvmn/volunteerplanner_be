package com.volunteer.api.data.repository;

import com.volunteer.api.data.model.persistence.VPUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
    extends JpaRepository<VPUser, Integer>, JpaSpecificationExecutor<VPUser> {

  @EntityGraph(attributePaths = {"userVerifiedBy.displayName", "lockedBy.displayName"})
  Page<VPUser> findAll(@Nullable Specification<VPUser> spec, Pageable pageable);

  VPUser findByPhoneNumber(final String phoneNumber);

  @Query("select case when count(u) > 0 then true else false end from User u where u.role.name = ?1")
  boolean existsWithRole(final String roleName);

  @Modifying
  @Query(nativeQuery = true, value = "update \"user\" set rating = rating + ?2 where id = ?1")
  void updateRating(final int userId, final int delta);


}
