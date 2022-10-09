package hamburg.kaischmidt.myarctestlib.arctests;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaCodeUnit;

import java.util.Set;

public class ConfigurableNotDeprecatedPredicate extends DescribedPredicate<JavaCodeUnit> {
    private Set<String> packagesToSkip;

    public ConfigurableNotDeprecatedPredicate(String description, Object... params) {
        super(description, params);
    }

    public ConfigurableNotDeprecatedPredicate(Set<String> packagesAllowedToUseDeprecated) {
        this("are deprecated");
        packagesToSkip = packagesAllowedToUseDeprecated;
    }

    @Override
    public boolean test(JavaCodeUnit codeUnit) {
        boolean skip = codeUnit.isAnnotatedWith(Deprecated.class)
                || codeUnit.getOwner().isAnnotatedWith(Deprecated.class)
                || packagesToSkip.contains(codeUnit.getOwner().getPackageName());
        return !skip;
    }
}
