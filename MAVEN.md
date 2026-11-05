## mvn validate

By default, Maven itself checks that:

* the POM is well-formed and valid, with required elements like groupId, artifactId, and version present
* parent POMs and imports can be resolved, and inheritance and interpolation work (e.g. ${property} references)
* the reactor can be ordered in a multi-module build, with no cyclic dependencies between modules

### Plugins

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
etc.)and the spring allopen plugin, which makes @Component/@Configuration classes open so Spring can proxy them.

**Dependencies:** Maven resolves the compile-scope (and provided-scope) dependencies at this point, and downloads them
into `~/.m2` if they’re missing. Test-scoped dependencies like spring-boot-starter-webmvc-test are not needed yet.

### Plugins

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

## mvn test

## mvn package

`mvn package` bundles the compiled output into a distributable file in `target/`, usually a JAR or WAR depending on
`<packaging>` (the default is jar).

What happens in this project at `package`:

* **maven-jar-plugin builds** a plain JAR, target/demo-maven-0.0.1-SNAPSHOT.jar, containing only the classes and
  resources.
* **spring-boot-maven-plugin:repackage** (bound automatically by the Spring Boot parent) then turns it into an
  executable fat JAR. It adds all runtime dependencies under BOOT-INF/lib plus Spring Boot’s launcher, so you can run
  java -jar target/demo-maven-0.0.1-SNAPSHOT.jar. The original thin JAR is kept as
  demo-maven-0.0.1-SNAPSHOT.jar.original.

NOTE: In CI, prefer `mvn verify` over package, so the integration tests and any verify-phase checks also run.

## mvn verify

`mvn verify` runs integration tests and final quality checks on the packaged artifact. It’s the last phase before
anything leaves your machine.

**Failsafe** runs at `integration-test`. It picks up test classes named *IT, IT*, or *ITCase. Surefire skips those
names, so unit tests and integration tests stay separate. Failsafe’s integration-test goal doesn’t fail the build when
tests fail; it only records the results to `target/failsafe-reports`. Its verify goal fails the build later, at the
verify phase. This split lets post-integration-test cleanup still run, so no container or server is left behind.

### Plugins

Plugins commonly bound to verify:

* **JaCoCo check**
    * Fails the build if code coverage falls below the thresholds you set.
    * Reports to `target/site/jacoco/index.html`
* **detekt** (Kotlin) or **SpotBugs/PMD** (Java) for static analysis.
    * Reports to `target/detekt.html` and
      `target/detekt.xml`.
    * To adjust the rules, run `./mvnw detekt:generate-config` and point the plugin at the generated
      `detekt.yml`
* **Testcontainers-based tests**, which don’t need a plugin: they run as part of the Failsafe tests and start Docker
  containers themselves

## mvn install

`mvn install` builds your project and copies the result into your local Maven repository (usually `~/.m2/repository`),
so other projects on the same machine can use it as a dependency.

Why it matters: If project B depends on project A, and A isn’t published to a remote repository, you run mvn install in
A. Then B can resolve A from the local repo. It’s also how multi-module builds share artifacts between separate builds.

### Plugins

Few plugins bind to the install phase itself.
Most of what affects install comes from plugins bound earlier, which attach extra files that install then copies along
with the main artifact

## mvn site

`mvn site` generates a project documentation website from your POM and any extra docs you’ve written.

Contains resources from:

* Reports configured in the `<reporting>` section of the POM, such as Javadoc, Surefire test results, JaCoCo coverage,
  Checkstyle, PMD, SpotBugs, and Javadoc cross-reference (JXR)
* Hand-written pages you put in `src/site/markdown/`, written in Markdown, APT, FML (FAQs), or XDoc.
* `src/site/site.xml` controls the menus, skin, and layout.
* `maven-project-info-reports-plugin` project summary, team/developers, licenses, dependencies, etc.

#### Run the site lifecycle:

```shell
mvn verify site
```

IMPORTANT: `site` is a separate lifecycle and doesn’t run verify

#### See docs locally

Open in browser: `/target/site/index.html`

#### Related commands:

* `mvn site:stage` combines multi-module sites into one directory so the links between modules work.
* `mvn site-deploy` uploads the site to the location set in `<distributionManagement><site>`.

#### GitHub Pages:

`.github/site.yml`

In GitHub, go to Settings → Pages and set the source to GitHub Actions.

### Plugins

* **Site plugin** for project info reports: summary, team, licenses, SCM, issues, CI, dependencies and plugins
* **Dokka** for API docs, because the **Javadoc plugin** ignores Kotlin sources.

## mvn deploy

