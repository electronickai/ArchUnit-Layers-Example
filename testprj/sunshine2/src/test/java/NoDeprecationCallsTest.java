import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import hamburg.kaischmidt.myarctestlib.arctests.MyArchTestLibrary;

@AnalyzeClasses(packages = "deprecated..")
public class NoDeprecationCallsTest {

  //This test fails because the deprecated methods are called from the non deprecated "CallerOfDeprecatedMethod"
  @ArchTest
  public static final ArchRule doNotUseDeprecatedOps = MyArchTestLibrary.DO_NOT_USE_DEPRECATED_OPS;

  //This would be an alternative way for the same functionality
  @ArchTest
  public static final ArchRule doNotUseDeprecatedOpsAlternative = MyArchTestLibrary.checkDeprecated();

  //with a configuration of a whitelist the test passes
  // (one of the two packages configuration would be sufficient to make the test pass)
  @ArchTest
  public static final ArchRule configureDeprecatedOpCalls =
      MyArchTestLibrary.checkDeprecated()
          .packages("deprecated.callees").allowedToBeCalled()
          .packages("deprecated.callers").allowToUseDeprecated();
}
