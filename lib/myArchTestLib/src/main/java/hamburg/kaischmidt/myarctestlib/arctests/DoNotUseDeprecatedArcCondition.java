package hamburg.kaischmidt.myarctestlib.arctests;

import com.tngtech.archunit.core.domain.AccessTarget;
import com.tngtech.archunit.core.domain.JavaCall;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;



public class DoNotUseDeprecatedArcCondition extends ArchCondition<JavaCodeUnit> {

    public DoNotUseDeprecatedArcCondition(String description, Object... args) {
        super(description, args);
    }

    public DoNotUseDeprecatedArcCondition() {
        this("not call deprecated CodeUnits");
    }

    @Override
    public void check(JavaCodeUnit javaMethod, ConditionEvents conditionEvents) {
        javaMethod.getCallsFromSelf().forEach(call -> checkCall(call, conditionEvents));
    }

    private void checkCall(JavaCall<?> call, ConditionEvents conditionEvents) {
        AccessTarget target = call.getTarget();

        if (target.isAnnotatedWith(Deprecated.class) || target.getOwner().isAnnotatedWith(Deprecated.class)) {
            conditionEvents.add(SimpleConditionEvent.violated(call.getOrigin(), "CodeUnit "
                + call.getOrigin() + " calls deprecated " + call.getTarget().getFullName()));
        }

    }

}
