package hamburg.kaischmidt.myarctestlib.arctests;

import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.core.domain.JavaFieldAccess;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.util.Set;

public class AddMethodsMustAccessSetCondition extends ArchCondition<JavaCodeUnit> {

    public AddMethodsMustAccessSetCondition() {
        this("access fields of type Set if the method starts with add");
    }

    public AddMethodsMustAccessSetCondition(String description, Object... args) {
        super(description, args);
    }

    @Override
    public void check(JavaCodeUnit codeUnit, ConditionEvents events) {
        if (!codeUnit.getName().startsWith("add")) {
            return;
        }

        for (JavaFieldAccess fieldAccess : codeUnit.getFieldAccesses()) {
            if (fieldAccess.getTarget().getType().toErasure().isAssignableFrom(Set.class)) {
                events.add(SimpleConditionEvent.satisfied(codeUnit, "CodeUnit "
                        + codeUnit + " starting with add does access a field of type Set. All fine "));
                return;
            }
        }
        events.add(SimpleConditionEvent.violated(codeUnit, "CodeUnit "
                + codeUnit + " starting with add does not access a field of type Set "));
    }
}