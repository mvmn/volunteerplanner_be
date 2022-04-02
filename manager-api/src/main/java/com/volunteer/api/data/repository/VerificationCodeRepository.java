package com.volunteer.api.data.repository;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {

  Optional<VerificationCode> findByUserAndType(final VPUser user, final VerificationCodeType type);

  void deleteByUserAndType(final VPUser user, final VerificationCodeType type);

}
