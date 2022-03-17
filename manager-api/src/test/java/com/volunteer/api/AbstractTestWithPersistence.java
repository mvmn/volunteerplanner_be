package com.volunteer.api;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import com.volunteer.api.utils.DBUtil;

public abstract class AbstractTestWithPersistence {

  @BeforeAll
  public static void resetDb(@Autowired Flyway flyway) {
    DBUtil.recreateDb(flyway);
  }
}
