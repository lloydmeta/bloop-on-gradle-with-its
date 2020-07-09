## Bloop with integration tests

This is a fairly minimal recreation a Gradle project with integration tests, using a plugin
for DRYness based on the [official guide](https://docs.gradle.org/current/userguide/java_testing.html#sec:configuring_java_integration_tests), so that it can be applied to subprojects
as needed.

The structure is deliberately similar to what sbt supports, putting integration tests in `src/it`.

### The Gradle project works fine

Tests work

```shell
❯ ./gradlew test

BUILD SUCCESSFUL in 6s
3 actionable tasks: 3 executed
```

Integration tests work

```shell
❯ ./gradlew integrationTest

BUILD SUCCESSFUL in 6s
3 actionable tasks: 2 executed, 1 up-to-date
```

### The `bloopInstall` task finishes

```shell script
❯ ./gradlew bloopInstall

Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.
Use '--warning-mode all' to show the individual deprecation warnings.
See https://docs.gradle.org/6.5/userguide/command_line_interface.html#sec:command_line_warnings

BUILD SUCCESSFUL in 563ms
2 actionable tasks: 2 executed
```

We can also see that a `bloop-on-gradle-with-its-it.json` file is created in `.bloop`

### bloop test fails due find frameworks for the integration test project

```shell
❯ bloop test bloop-on-gradle-with-its-it
Compiling bloop-on-gradle-with-its (1 Scala source)
Compiled bloop-on-gradle-with-its (286ms)
Compiling bloop-on-gradle-with-its-it (1 Scala source)
Compiled bloop-on-gradle-with-its-it (316ms)
[W] Missing configured test frameworks in bloop-on-gradle-with-its-it
===============================================
Total duration: 0ms
No test suites were run.
===============================================
```

Copying and pasting the `project.tests` section of `.bloop/bloop-on-gradle-with-its-test.json` into `.bloop/bloop-on-gradle-with-its-it.json` makes this work

```shell
❯ bloop test bloop-on-gradle-with-its-it
MehItSpec:
- should w/e
Execution took 13ms
1 tests, 1 passed
All tests in com.beachape.MehItSpec passed

===============================================
Total duration: 13ms
All 1 test suites passed.
===============================================
```