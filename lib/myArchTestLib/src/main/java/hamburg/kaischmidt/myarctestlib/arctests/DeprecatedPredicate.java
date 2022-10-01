package hamburg.kaischmidt.myarctestlib.arctests;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaCodeUnit;

public class DeprecatedPredicate extends DescribedPredicate<JavaCodeUnit> {
    public DeprecatedPredicate(String description, Object... params) {
        super(description, params);
    }

    public DeprecatedPredicate() {
        this("are deprecated");
    }

    @Override
    public boolean apply(JavaCodeUnit codeUnit) { //TODO update ArchUnit
        return !codeUnit.isAnnotatedWith(Deprecated.class)
            && !codeUnit.getOwner().isAnnotatedWith(Deprecated.class);

    }
}
