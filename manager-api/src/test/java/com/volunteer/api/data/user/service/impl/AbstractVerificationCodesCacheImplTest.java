package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;
import com.volunteer.api.service.VerificationCodeCache;
import java.util.Optional;
import org.junit.Assert;

public abstract class AbstractVerificationCodesCacheImplTest {

  public void testCacheFunctionality(VPUser user, VerificationCodeCache unit) {
    Assert.assertTrue(unit.get(user, VerificationCodeType.PHONE).isEmpty());
    Assert.assertTrue(unit.get(user, VerificationCodeType.EMAIL).isEmpty());

    final String expected = "123456";

    unit.put(user, VerificationCodeType.PHONE, expected);
    final Optional<String> actual = unit.get(user, VerificationCodeType.PHONE);
    Assert.assertTrue(actual.isPresent());
    Assert.assertEquals(expected, actual.get());

    unit.delete(user, VerificationCodeType.PHONE);
    Assert.assertTrue(unit.get(user, VerificationCodeType.PHONE).isEmpty());
  }

}
