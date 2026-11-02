## mvn validate

By default, Maven itself checks that:

* the POM is well-formed and valid, with required elements like groupId, artifactId, and version present
* parent POMs and imports can be resolved, and inheritance and interpolation work (e.g. ${property} references)
* the reactor can be ordered in a multi-module build, with no cyclic dependencies between modules

#### Plugins

Teams often bind their own checks to this phase, so they run early and fail fast:

* [x] **maven-enforcer-plugin** for rules like a minimum Maven or Java version, banned dependencies, dependency
  convergence, or no SNAPSHOT dependencies in releases
* [x] **spotless-maven-plugin** check formatting (using ktlint) & add license header to kotlin files
* [x] **git-commit-id-maven-plugin** capture git metadata to `target/classes/git.properties`

## mvn compile

`mvn compile` compiles main source code into .class files in target/classes. It does not compile or run tests, and it
doesn’t package anything.

To compile the tests too, use `mvn test-compile`. To compile and run them, use `mvn test`.

**For Kotlin:** the `kotlin-maven-plugin` compiles src/main/kotlin. It uses the compiler arguments (-Xjsr305=strict,
etc.)and the spring all-open plugin, which makes @Component/@Configuration classes open so Spring can proxy them.

**Dependencies:** Maven resolves the compile-scope (and provided-scope) dependencies at this point, and downloads them
into `~/.m2` if they’re missing. Test-scoped dependencies like spring-boot-starter-webmvc-test are not needed yet.

#### Plugins

These are the plugins people commonly attach to compile itself or to the phases just before it:

At `compile` (or right after, at process-classes):

* **kapt / KSP** (Kotlin annotation processing via kotlin-maven-plugin) for libraries like MapStruct, or Spring’s
  spring-boot-configuration-processor. In Java projects this goes through annotationProcessorPaths in
  maven-compiler-plugin.
* **Error Prone or NullAway** (Java only), which hook into javac to catch bugs at compile time.
* **Bytecode enhancement**, such as Hibernate’s `hibernate-enhance-maven-plugin` (lazy loading, dirty tracking) or
  AspectJ’s `aspectj-maven-plugin`. These run at process-classes because they modify the compiled .class files.
* **Spring Boot AOT** (spring-boot-maven-plugin:process-aot). It generates ahead-of-time code for GraalVM native images
  or faster startup, and runs at prepare-package, after compilation.

Code generation at `generate-sources`, before compile:

* **openapi-generator-maven-plugin** generates API clients or server interfaces from an OpenAPI spec. This is very
  common in Spring projects.
* **protobuf / gRPC** plugins such as protobuf-maven-plugin.
* **jOOQ codegen** generates type-safe SQL classes from a database schema, often combined with Flyway or Testcontainers.
* **jaxb / cxf-codegen** for XML or SOAP projects.
* **build-helper-maven-plugin** adds extra source folders, for example generated sources, to the compile path.

Resources at `process-resources`:

* **maven-resources-plugin** with filtering, which the Spring Boot parent already configures.
* **frontend-maven-plugin**, when a Spring app bundles a JS frontend. It’s usually bound to generate-resources.

## mvn package

## mvn verify

TODO: detekt (detekt-maven-plugin), the static analysis tool for Kotlin. It’s usually bound to verify, but some teams
run it early at compile or process-classes.

## mvn install

## mvn site

## mvn deploy