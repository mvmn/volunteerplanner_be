## Start locally

- Clone repo
- Run a PostgreSQL server (for macOS the simplest option is via https://postgresapp.com )
- Make sure you have Java 11+ and Gradle 7.x in your PATH
- Run `gradle bootRun --args='--spring.profiles.active=local'`

To adjust PostgreSQL DB parameters - edit ./manager-api/src/main/resources/application.yaml for the `local` profile, or suppy parameters such as DB username and password via bootRun args: `gradle bootRun --args='--spring.profiles.active=local -Dspring.datasource.username=postgres -Dspring.datasource.password=mypass123'`
