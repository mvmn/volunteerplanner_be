package com.volunteer.api.data.repository;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {

  public Optional<VerificationCode> findByTypeAndUserAndGenerationTimestampGreaterThan(
      VerificationCodeType type, VPUser user, Long timestamp);

  @Transactional
  @Modifying
  public int deleteByTypeAndUserAndGenerationTimestampLessThan(VerificationCodeType type,
      VPUser user, Long timestamp);
}
