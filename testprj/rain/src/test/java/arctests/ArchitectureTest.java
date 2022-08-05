package arctests;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import hamburg.kaischmidt.myarctestlib.arctests.MyArchTestLibrary;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = "architecture..")
public class ArchitectureTest {

  @ArchTest
  public static final ArchRule shouldBeOnionArchitecture =
      onionArchitecture()
          .domainModels("architecture.domain.model..")
          .domainServices("architecture.domain.service..")
          .applicationServices("architecture.application..")
          .adapter("persistence", "architecture.adapter.db..");

  @ArchTest
  public static final ArchRule shouldBeHexagonalArchitecture =
      MyArchTestLibrary.hexagonalArchitecture()
          .domain("architecture.domain.model")
          .commons("architecture.domain.service")
          .applicationServices("architecture.application")
          .adapter("persistence", "architecture.adapter.db");
}
