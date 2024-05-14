Spring Webflux App that creates transactions, throws a crash, a captureException, and also contains a task that runs every 5 seconds.

The task creates a Custom transaction using @SentryTransaction.  The transactions make use of @SentrySpan to add method names as Spans.  One transaction sends an outgoing request to a separate server which gets a span added to the root transaction through automatic instrumentation.

Source bundles are uploaded, so the errors have source context.

1. create a .env file with `SENTRY_PROJECT`, `SENTRY_ORG`, and `SENTRY_AUTH_TOKEN`

2. fill in dsn to `src/main/resources/application.properties`

3. build with `./gradlew build`

4. run with `java -jar build/libs/demo-0.0.1-SNAPSHOT.jar`

The routes are listed in `src/main/java/com/example/hello/GreetingRouter.java`

The task is under `src/main/java/com/example/hello/ScheduledTaskComponent.java`