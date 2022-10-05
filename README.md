# ArchUnit-Layers-Example

Usually [ArchUnit](https://www.archunit.org/) is explained on the level of the quite easy understandable fluent API of the `Lang Layer` like

```java
classes().that().resideInAPackage(codeRoot).should().should().dependOnClassesThat().areInterfaces()
```

We can easily understand that level because this layer is really a powerful tool and due to the composable character of this fluent API, a lot of rules are possible.

However, with this project we want to give a brief overview about other useful but more sophisticated use cases with ArchUnit:
- How do you use the Core API to create more specific tests that cannot be covered by the fluent API of the `Lang Layer`?
- How to write your own Predicates (`that()`) and Conditions (`should()`) using the Core API?
- How to use your own Predicates and Conditions within your ArchUnit tests?
- How to use the Library API to make your rules easily configurable, similar the existing Architectures like `LayeredArchitecture` and `OnionArchitecture`?
- How to check your ArchUnit tests against valid architectures but also against architectures that are violating your defined rules?

In this way this project is rather a complement to the good known accompanying project of ArchUnit called [ArchUnit-Examples](https://github.com/TNG/ArchUnit-Examples).
This project has been presented in the scope of the `Java Forum Nord 2022`. Feel free to download the [slides](./ArchUnit-Layers-Example.pdf).

## Project setup

The project is build as a modular maven project. So, after cloning or forking the project, your IDE should grasp the structure of it.
The Java language level has been deliberately set to Java 8. So the project should (probably) work with the Java version you are using.

the project consists of the following modules:
- `core-example`: Example implementation using the core API (implementation of the example of the slides).
- `lib`: Library project that contains predicates, rules and conditions that are used within the projects. The idea is that such a library may be used in several (sub-) projects to align to a kind of macro architecture.
- `rain`: Test projects to check whether architecture violations are recognized correctly (kind of negative tests).
- `sunshine`: Test projects to check that valid architectures are recognized to be valid (kind of positive tests). 

## Troubleshooting

If you are using IntelliJ and get an error message like `` when executing the tests and neither `Reload All Maven Projects` nor `invalidate Caches...` helps ot, try to disable the `--release option for cross complilation`
or make sure that the configured SDK of the project matches the bytecode version of the compiler like suggest in this [Stackoverflow post](https://stackoverflow.com/questions/40448203/intellij-says-the-package-does-not-exist-but-i-can-access-the-package).