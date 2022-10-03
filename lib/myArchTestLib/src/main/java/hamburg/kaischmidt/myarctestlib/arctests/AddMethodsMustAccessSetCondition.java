package hamburg.kaischmidt.myarctestlib.arctests;

import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaFieldAccess;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.util.Set;

public class AddMethodsMustAccessSetCondition extends ArchCondition<JavaMethod> {

    public AddMethodsMustAccessSetCondition() {
        this("access fields of type Set if the method starts with add");
    }

    public AddMethodsMustAccessSetCondition(String description, Object... args) {
        super(description, args);
    }

    @Override
    public void check(JavaMethod method, ConditionEvents events) {
        if (!method.getName().startsWith("add")) {
            return;
        }

        for (JavaFieldAccess fieldAccess : method.getFieldAccesses()) {
            if (fieldAccess.getTarget().getType().toErasure().isAssignableFrom(Set.class)) {
                events.add(SimpleConditionEvent.satisfied(method, "Method "
                        + method + " starting with add does access a field of type Set. All fine "));
                return;
            }
        }
        events.add(SimpleConditionEvent.violated(method, "Method "
                + method + " starting with add does not access a field of type Set "));
    }
}