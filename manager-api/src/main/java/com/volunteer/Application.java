package com.volunteer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    final SpringApplication application = new SpringApplication(Application.class);
    //application.setApplicationStartup(new BufferingApplicationStartup(10_000));
    //application.setApplicationStartup(new FlightRecorderApplicationStartup());

    application.run(args);
  }
}
