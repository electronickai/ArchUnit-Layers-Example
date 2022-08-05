package arctests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import hamburg.kaischmidt.myarctestlib.arctests.MyArchTestLibrary;

import static org.assertj.core.api.Assertions.assertThat;

@AnalyzeClasses(packages = "deprecated..")
public class NoDeprecationCallsTest {

    //This test fails because the deprecated methods are called from the non deprecated "CallerOfDeprecatedMethod"
    @ArchTest
    public static void test_doNotUseDeprecatedOps(JavaClasses classes) {

        try {
            MyArchTestLibrary.DO_NOT_USE_DEPRECATED_OPS.check(classes);
        } catch (Throwable err) {
            assertThat(err)
                    .hasMessageContaining("Rule 'code units that are deprecated should not call deprecated CodeUnits' was violated")
                    .hasMessageContaining("CodeUnit JavaMethod{deprecated.callers.CallerOfDeprecatedMethod.callDeprecatedMethod()} calls deprecated deprecated.callees.ClassContainingDeprecatedMethod.deprecatedMethod()")
                    .hasMessageContaining("CodeUnit JavaMethod{deprecated.callers.CallerOfDeprecatedMethod.callDeprecatedMethod()} calls deprecated deprecated.callees.DeprecatedClass.methodOfDeprecatedClass()");
            return;
        }
        throw new AssertionError("This should throw an AssertionError to indicated that the architecture is wrong");
    }

    //This would be an alternative way for the same functionality
    @ArchTest
    public static void test_doNotUseDeprecatedOpsAlternative(JavaClasses classes) {

        try {
            MyArchTestLibrary.checkDeprecated().check(classes);
        } catch (Throwable err) {
            assertThat(err)
                    .hasMessageContaining("Rule 'Deprecation checked architecture' was violated")
                    .hasMessageContaining("CodeUnit JavaMethod{deprecated.callers.CallerOfDeprecatedMethod.callDeprecatedMethod()} calls deprecated deprecated.callees.ClassContainingDeprecatedMethod.deprecatedMethod()")
                    .hasMessageContaining("CodeUnit JavaMethod{deprecated.callers.CallerOfDeprecatedMethod.callDeprecatedMethod()} calls deprecated deprecated.callees.DeprecatedClass.methodOfDeprecatedClass()");
            return;
        }
        throw new AssertionError("This should throw an AssertionError to indicated that the architecture is wrong");
    }

    //with a configuration of a whitelist the test passes
    // (one of the two packages configuration would be sufficient to make the test pass)
    @ArchTest
    public static final ArchRule configureDeprecatedOpCalls = MyArchTestLibrary.checkDeprecated().packages("deprecated.callees").allowedToBeCalled().packages("deprecated.callers").allowToUseDeprecated();
}