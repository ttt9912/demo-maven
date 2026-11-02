## mvn validate

By default, Maven itself checks that:

* the POM is well-formed and valid, with required elements like
  groupId, artifactId, and version present
* parent POMs and imports can be resolved, and inheritance and
  interpolation work (e.g. ${property} references)
* the reactor can be ordered in a multi-module build, with no
  cyclic dependencies between modules

Teams often bind their own checks to this phase, so they run early and fail fast:

[ ] **maven-enforcer-plugin** for rules like a minimum Maven or Java version,
banned dependencies, dependency convergence, or no SNAPSHOT dependencies in releases
[ ] Checkstyle or formatters such as Spotless or fmt-maven-plugin, to check code style before compiling
[ ] buildnumber-maven-plugin or git-commit-id-plugin to capture build or Git metadata early
[ ] license header checks

## mvn compile

## mvn package

## mvn verify

## mvn install

## mvn site

## mvn deploy