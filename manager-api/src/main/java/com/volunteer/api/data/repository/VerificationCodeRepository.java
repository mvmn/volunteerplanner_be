package com.volunteer.api.data.repository;

import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {

  Optional<VerificationCode> findByTypeAndUserAndCreatedAtGreaterThan(
      VerificationCodeType type, VPUser user, Long timestamp);

  @Transactional
  @Modifying
  int deleteByTypeAndUserAndCreatedAtLessThan(VerificationCodeType type, VPUser user,
      Long timestamp);

  @Transactional
  @Modifying
  int deleteByTypeAndUser(VerificationCodeType type, VPUser user);

}
