# demo-maven

Demo Spring Boot service written in Kotlin.

## Build

```bash
./mvnw verify            # build, unit + integration tests, coverage and detekt checks
./mvnw verify site       # same, plus this site in target/site
./mvnw verify -Powasp    # add the OWASP dependency vulnerability scan
```

## Reports

- [API documentation](dokka/index.html)
- [Static analysis (detekt)](detekt.html)
- [Test coverage (JaCoCo)](jacoco/index.html)
- [Unit test results](surefire.html)
- [Integration test results](failsafe.html)
