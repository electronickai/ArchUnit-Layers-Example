package hamburg.kaischmidt.myarctestlib.arctests;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaCodeUnit;

public class NotDeprecatedPredicate extends DescribedPredicate<JavaCodeUnit> {
    public NotDeprecatedPredicate(String description, Object... params) {
        super(description, params);
    }

    public NotDeprecatedPredicate() {
        this("are deprecated");
    }

    @Override
    public boolean test(JavaCodeUnit codeUnit) {
        return !codeUnit.isAnnotatedWith(Deprecated.class)
            && !codeUnit.getOwner().isAnnotatedWith(Deprecated.class);

    }
}
