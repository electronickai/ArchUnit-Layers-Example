package hamburg.kaischmidt.myarctestlib.arctests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
import com.tngtech.archunit.lang.Priority;
import com.tngtech.archunit.thirdparty.com.google.common.base.Joiner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.codeUnits;
import static java.lang.System.lineSeparator;

public class DeprecationAwareArchitecture implements ArchRule {

  private final Set<String> packagesAllowedToBeCalled = new LinkedHashSet<>();
  private final Set<String> packagesAllowedToUseDeprecated = new LinkedHashSet<>();

  public PackageClassification packages(String packageIdentifier) {
    return new PackageClassification(packageIdentifier);
  }

  @Override public void check(JavaClasses classes) {
    Assertions.check(this, classes);
  }

  @Override public ArchRule because(String reason) {
    return null;
  }

  @Override public ArchRule allowEmptyShould(boolean allowEmptyShould) {
    throw new NotImplementedException();
  }

  @Override public ArchRule as(String newDescription) {
    return null;
  }

  @Override public EvaluationResult evaluate(JavaClasses classes) {
    EvaluationResult result = new EvaluationResult(this, Priority.MEDIUM);
    ConfigurableDeprecatedPredicate predicate = new ConfigurableDeprecatedPredicate(packagesAllowedToUseDeprecated);
    ConfigurableDoNotUseDeprecatedArcCondition condition = new ConfigurableDoNotUseDeprecatedArcCondition(packagesAllowedToBeCalled);
    result.add(codeUnits().that(predicate).should(condition).evaluate(classes));
    return result;
  }

  @Override public String getDescription() {
    List<String> lines = new ArrayList<>();
    lines.add("Deprecation checked architecture");
    if (!packagesAllowedToBeCalled.isEmpty()) {
      lines.add("with the following packages allowed to be called");
      lines.addAll(packagesAllowedToBeCalled);
    }
    if (!packagesAllowedToUseDeprecated.isEmpty()) {
      lines.add("with the following packages allowed to call deprecated methods");
      lines.addAll(packagesAllowedToUseDeprecated);
    }
    return Joiner.on(lineSeparator()).join(lines);
  }

  private DeprecationAwareArchitecture addPackageToBeCalled(String packageIdentifier) {
    packagesAllowedToBeCalled.add(packageIdentifier);
    return this;
  }

  private DeprecationAwareArchitecture addPackageToUseDeprecated(String packageIdentifier) {
    packagesAllowedToUseDeprecated.add(packageIdentifier);
    return this;
  }

  public class PackageClassification {

    private final String packageIdentifier;

    public PackageClassification(String packageIdentifier) {
      this.packageIdentifier = packageIdentifier;
    }

    public DeprecationAwareArchitecture areAllowedToBeCalled() {
      return DeprecationAwareArchitecture.this.addPackageToBeCalled(packageIdentifier);
    }

    public DeprecationAwareArchitecture areAllowedToUseDeprecated() {
      return DeprecationAwareArchitecture.this.addPackageToUseDeprecated(packageIdentifier);
    }
  }
}
