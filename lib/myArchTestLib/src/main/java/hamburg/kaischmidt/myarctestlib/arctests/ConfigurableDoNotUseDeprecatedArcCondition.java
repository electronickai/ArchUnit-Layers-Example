package hamburg.kaischmidt.myarctestlib.arctests;

import com.tngtech.archunit.core.domain.AccessTarget;
import com.tngtech.archunit.core.domain.JavaCall;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.util.Set;

public class ConfigurableDoNotUseDeprecatedArcCondition extends ArchCondition<JavaCodeUnit> {

  public static final String DEFAULT_DESCRIPTION = "not call deprecated CodeUnits";
  private final Set<String> packagesAllowedToBeCalled;

  public ConfigurableDoNotUseDeprecatedArcCondition(Set<String> packagesAllowedToBeCalled) {
    super(DEFAULT_DESCRIPTION);
    this.packagesAllowedToBeCalled = packagesAllowedToBeCalled;
  }

  @Override
  public void check(JavaCodeUnit codeUnit, ConditionEvents conditionEvents) {
    codeUnit.getCallsFromSelf().forEach(call -> checkCall(call, conditionEvents));
  }

  private void checkCall(JavaCall<?> call, ConditionEvents conditionEvents) {
    AccessTarget target = call.getTarget();

    if (callIsConfiguredToBeValid(call)) {
      return;
    }

    if (target.isAnnotatedWith(Deprecated.class) || target.getOwner()
        .isAnnotatedWith(Deprecated.class)) {
      conditionEvents.add(SimpleConditionEvent.violated(call.getOrigin(), "CodeUnit "
          + call.getOrigin() + " calls deprecated " + target.getFullName()));
    } else {
      conditionEvents.add(SimpleConditionEvent.satisfied(call.getOrigin(), "CodeUnit "
          + call.getOrigin() + " calls method " + target.getFullName() + " that isn't deprecated"));
    }
  }

  private boolean callIsConfiguredToBeValid(JavaCall<?> call) {
    return targetIsAllowedToBeUsed(call.getTarget());
  }

  private boolean targetIsAllowedToBeUsed(AccessTarget target) {
    return packagesAllowedToBeCalled.stream()
        .anyMatch(packagePrefix -> target.getFullName().startsWith(packagePrefix));
  }
}
