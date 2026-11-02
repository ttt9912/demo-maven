## mvn validate

By default, Maven itself checks that:

* the POM is well-formed and valid, with required elements like groupId, artifactId, and version present
* parent POMs and imports can be resolved, and inheritance and interpolation work (e.g. ${property} references)
* the reactor can be ordered in a multi-module build, with no cyclic dependencies between modules

Teams often bind their own checks to this phase, so they run early and fail fast:

* [x] **maven-enforcer-plugin** for rules like a minimum Maven or Java version, banned dependencies, dependency
  convergence, or no SNAPSHOT dependencies in releases
* [x] **spotless-maven-plugin** check formatting (using ktlint) & add license header to kotlin files
* [x] **git-commit-id-maven-plugin** capture git metadata to `target/classes/git.properties`

## mvn compile

## mvn package

## mvn verify

## mvn install

## mvn site

## mvn deploy