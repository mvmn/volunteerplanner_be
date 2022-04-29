## Start locally

- Clone repo
- Run a PostgreSQL server
- Make sure you have Gradle 7.x in your PATH
- Run `gradle bootRun --args='--spring.profiles.active=local'`

To adjust PostgreSQL DB parameters - edit ./manager-api/src/main/resources/application.yaml for the `local` profile
