package hamburg.kaischmidt.myarctestlib.arctests;

import com.tngtech.archunit.core.domain.AccessTarget;
import com.tngtech.archunit.core.domain.JavaCall;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

public class DoNotUseDeprecatedArchCondition extends ArchCondition<JavaCodeUnit> {

  public DoNotUseDeprecatedArchCondition(String description, Object... args) {
    super(description, args);
  }

  public DoNotUseDeprecatedArchCondition() {
    this("not call deprecated CodeUnits");
  }

  @Override public void init(Iterable<JavaCodeUnit> allObjectsToTest) {
    System.out.println("INIT: objects to test are:");
    allObjectsToTest.forEach(codeUnit -> System.out.println(codeUnit.getFullName()));
  }

  @Override
  public void check(JavaCodeUnit codeUnit, ConditionEvents conditionEvents) {
    codeUnit.getCallsFromSelf().forEach(call -> checkCall(call, conditionEvents));
  }

  private void checkCall(JavaCall<?> call, ConditionEvents conditionEvents) {
    AccessTarget target = call.getTarget();

    if (target.isAnnotatedWith(Deprecated.class) || target.getOwner().isAnnotatedWith(Deprecated.class)) {
      conditionEvents.add(SimpleConditionEvent.violated(call.getOrigin(), "CodeUnit "
          + call.getOrigin() + " calls deprecated " + target.getFullName()));
    } else {
      conditionEvents.add(SimpleConditionEvent.satisfied(call.getOrigin(), "CodeUnit "
          + call.getOrigin() + " calls method " + target.getFullName() + " that isn't deprecated"));
    }
  }

  @Override public void finish(ConditionEvents events) {
    System.out.println("FINISH: satisfied calls:");
    events.getAllowed()
        .forEach(conditionEvent -> System.out.println(conditionEvent.getDescriptionLines()));
  }
}
