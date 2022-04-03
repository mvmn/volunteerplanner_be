package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.model.persistence.VerificationCode.VerificationCodeType;
import com.volunteer.api.service.VerificationCodeCache;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;

public abstract class AbstractVerificationCodeCacheImplTest {

  public void testCacheFunctionality(VPUser user, VerificationCodeCache unit) {
    Assert.assertTrue(unit.getCode(user, VerificationCodeType.PHONE).isEmpty());
    Assert.assertTrue(unit.getCode(user, VerificationCodeType.EMAIL).isEmpty());

    Pair<Boolean, String> createdCode = unit.getOrCreateCode(user, VerificationCodeType.PHONE);
    Pair<Boolean, String> obtainedCode = unit.getOrCreateCode(user, VerificationCodeType.PHONE);

    Assert.assertTrue(unit.getCode(user, VerificationCodeType.PHONE).isPresent());
    Assert.assertTrue(createdCode.getKey());
    Assert.assertFalse(obtainedCode.getKey());

    Assert.assertEquals(createdCode.getValue(), obtainedCode.getValue());

    unit.delete(user, VerificationCodeType.PHONE);
    Assert.assertTrue(unit.getCode(user, VerificationCodeType.PHONE).isEmpty());
  }

}
