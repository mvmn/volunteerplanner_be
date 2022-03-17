package com.volunteer.api.utils;

import org.flywaydb.core.Flyway;

public class DBUtil {

  public static void recreateDb(Flyway flyway) {
    flyway.clean();
    flyway.migrate();
  }
}
