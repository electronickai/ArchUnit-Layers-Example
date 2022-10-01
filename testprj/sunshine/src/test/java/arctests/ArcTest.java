package arctests;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import hamburg.kaischmidt.myarctestlib.arctests.MyArchTestLibrary;

@AnalyzeClasses(
        packages = "hamburg.kaischmidt.sunshine.."
)
public class ArcTest {
    @ArchTest
    static final ArchRule ENSURE_HEXAGONAL_ARCH = MyArchTestLibrary
            .hexagonalArchitecture()
            .codeRoot("hamburg.kaischmidt.sunshine..")
            .domain("hamburg.kaischmidt.sunshine.domain..")
            .commons("hamburg.kaischmidt.sunshine.common..")
            .applicationServices("hamburg.kaischmidt.sunshine.service..")
            .adapter("inadapter","hamburg.kaischmidt.sunshine.adapter.inadapter..")
            .adapter("outadapter", "hamburg.kaischmidt.sunshine.adapter.outadapter..")
            .adapter("inoutadapter", "hamburg.kaischmidt.sunshine.adapter.inoutadapter..")
            .withOptionalLayers(false);

    @ArchTest
    static final ArchRule DO_NOT_CALL_DEPRECATED = MyArchTestLibrary.DO_NOT_USE_DEPRECATED_OPS;

}
