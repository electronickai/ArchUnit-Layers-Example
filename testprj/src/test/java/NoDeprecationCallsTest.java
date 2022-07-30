import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import hamburg.kaischmidt.myarctestlib.arctests.MyArchTestLibrary;

@AnalyzeClasses(packages = "deprecated..")
public class NoDeprecationCallsTest {

  //This test fails because the deprecated methods are called from the non deprecated "CallerOfDeprecatedMethod"
  @ArchTest
  public static final ArchRule doNotUseDeprecatedOps = MyArchTestLibrary.DO_NOT_USE_DEPRECATED_OPS;

  @ArchTest
  public static final ArchRule configureDeprecatedOpCalls =
      MyArchTestLibrary.checkDeprecated()
          .packages("..in.migration..").allowedToBeCalled()
          .packages("..my.deprecation.calls").allowToUseDeprecated();
}
