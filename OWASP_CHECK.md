## OWASP dependency check

Checks the dependencies in the pom against a vulnerabilities database.

It lives in a profile, so run it with `./mvnw verify -Powasp`. Keep it out of the normal build because it
downloads and updates a large vulnerability database, which is slow and needs network access.

It fails the build for any dependency with a **CVSS** score of 7 or higher (the owasp.fail.cvss property) and
writes an HTML and JSON report.

Test-scoped dependencies are skipped. Sonatype OSS Index is disabled because it now requires credentials.

Request a **free NVD API key** and export it as `NVD_API_KEY`. Without one, the first database download can take a
very long time.

Run OWASP dependency check:

```shell
export NVD_API_KEY=748D46E6-0286-408A-8FD6-B83C9286BC55
./mvnw verify -Powasp
```

In CI, run this check as a separate nightly or weekly job, and cache
`~/.m2/repository/org/owasp/dependency-check-data` between runs.