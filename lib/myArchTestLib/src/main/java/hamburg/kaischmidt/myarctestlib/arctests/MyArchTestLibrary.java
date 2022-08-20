package hamburg.kaischmidt.myarctestlib.arctests;

import com.tngtech.archunit.PublicAPI;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.PublicAPI.Usage.ACCESS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.codeUnits;

public class MyArchTestLibrary {

    private MyArchTestLibrary() {
    }

    private static final DescribedPredicate<JavaCodeUnit> ARE_DEPRECATED = new DeprecatedPredicate();

    private static final ArchCondition<JavaCodeUnit> NOT_USE_DEPRECATED_OPERATIONS
        = new DoNotUseDeprecatedArchCondition();

    @PublicAPI(usage = ACCESS)
    public static final ArchRule DO_NOT_USE_DEPRECATED_OPS = codeUnits()
        .that(ARE_DEPRECATED)
        .should(NOT_USE_DEPRECATED_OPERATIONS);

    @PublicAPI(usage = ACCESS)
    public static HexagonalArchitecture hexagonalArchitecture() {
        return new HexagonalArchitecture();
    }

    @PublicAPI(usage = ACCESS)
    public static DeprecationAwareArchitecture checkDeprecated() {
        return new DeprecationAwareArchitecture();
    }
}
