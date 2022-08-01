import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "dependency..")
public class PackageDependencyTest {

  @ArchTest
  public static final ArchRule sourceApiShouldDependOnFoo =
      classes().that().resideInAPackage("..source.api")
          .should().dependOnClassesThat().resideInAPackage("..foo..");

  @ArchTest
  public static final ArchRule sourceShouldAlwaysDependOnOutsideFoo =
      classes().that().resideInAPackage("..source..")
          .should().dependOnClassesThat().resideOutsideOfPackage("..foo..");

  @ArchTest
  public static final ArchRule sourceOtherShouldNotDependOnFoo =
      noClasses().that().resideInAPackage("..source.other")
          .should().dependOnClassesThat().resideInAPackage("..foo..");
}
