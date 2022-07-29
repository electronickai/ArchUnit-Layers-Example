package hamburg.kaischmidt.myarctestlib.arctests;

import com.tngtech.archunit.PublicAPI;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.base.Optional;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
import com.tngtech.archunit.lang.Priority;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.thirdparty.com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.tngtech.archunit.PublicAPI.Usage.ACCESS;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.equivalentTo;
import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.name;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.thirdparty.com.google.common.collect.Lists.newArrayList;
import static java.lang.System.lineSeparator;

/**
 * @author Thomas Ruhroth (msg system ag) 2022
 *
 *         Code taken von ArchUnit 0.23.1 which has Apache2 Licence.
 *         Apache2 Licene allows reliciencing, which is used here.
 *         Adapted to have additinal commons package
 * @see <a href="https://github.com/TNG/ArchUnit/blob/main/archunit/src/main/java/
 * com/tngtech/archunit/library/Architectures.java">OnionArchitecture</a>
 */
public final class HexagonalArchitecture implements ArchRule {
    private static final String DOMAIN_LAYER = "domain";
    private static final String APPLICATION_SERVICE_LAYER = "application service";
    private static final String COMMONS = "commons";
    private static final String ADAPTER_LAYER = "adapter";

    private final Optional<String> overriddenDescription;
    private String codeRoot = "";
    private String[] domainPackageIdentifiers = new String[0];
    private String[] applicationPackageIdentifiers = new String[0];
    private String[] commonPackageIdentitiers = new String[0];
    private Map<String, String[]> adapterPackageIdentifiers = new LinkedHashMap<>();
    private boolean optionalLayers = false;
    private List<IgnoredDependency> ignoredDependencies = new ArrayList<>();

    public HexagonalArchitecture() {
        overriddenDescription = Optional.empty();
    }

    private HexagonalArchitecture(String codeRoot,
        String[] domainPackageIdentifiers,
        Map<String, String[]> adapterPackageIdentifiers,
        String[] applicationPackageIdentifiers,
        String[] commonPackageIdentitiers,
        List<IgnoredDependency> ignoredDependencies,
        Optional<String> overriddenDescription) {
        this.codeRoot = codeRoot;
        this.domainPackageIdentifiers = domainPackageIdentifiers;
        this.applicationPackageIdentifiers = applicationPackageIdentifiers;
        this.adapterPackageIdentifiers = adapterPackageIdentifiers;
        this.commonPackageIdentitiers = commonPackageIdentitiers;
        this.ignoredDependencies = ignoredDependencies;
        this.overriddenDescription = overriddenDescription;
    }

    @PublicAPI(usage = ACCESS)
    public HexagonalArchitecture codeRoot(String packageIdentifier) {
        codeRoot = packageIdentifier;
        return this;
    }

    @PublicAPI(usage = ACCESS)
    public HexagonalArchitecture domain(String... packageIdentifiers) {
        domainPackageIdentifiers = packageIdentifiers;
        return this;
    }

    @PublicAPI(usage = ACCESS)
    public HexagonalArchitecture applicationServices(String... packageIdentifiers) {
        applicationPackageIdentifiers = packageIdentifiers;
        return this;
    }

    @PublicAPI(usage = ACCESS)
    public HexagonalArchitecture adapter(String name, String... packageIdentifiers) {
        adapterPackageIdentifiers.put(name, packageIdentifiers);
        return this;
    }

    @PublicAPI(usage = ACCESS)
    public HexagonalArchitecture commons(String... packageIdentifiers) {
        commonPackageIdentitiers = packageIdentifiers;
        return this;
    }

    /**
     * @param optionalLayers
     *            Whether the different parts of the Onion Architecture (domain models, domain services, ...) should
     *            be allowed to be empty.
     *            If set to {@code false} the {@link HexagonalArchitecture HexagonalArchitecture} will
     *            fail if any such layer does not contain any class.
     */
    @PublicAPI(usage = ACCESS)
    public HexagonalArchitecture withOptionalLayers(boolean optionalLayers) {
        this.optionalLayers = optionalLayers;
        return this;
    }

    @PublicAPI(usage = ACCESS)
    public HexagonalArchitecture ignoreDependency(Class<?> origin, Class<?> target) {
        return ignoreDependency(equivalentTo(origin), equivalentTo(target));
    }

    @PublicAPI(usage = ACCESS)
    public HexagonalArchitecture ignoreDependency(String origin, String target) {
        return ignoreDependency(name(origin), name(target));
    }

    @PublicAPI(usage = ACCESS)
    public HexagonalArchitecture ignoreDependency(DescribedPredicate<? super JavaClass> origin,
        DescribedPredicate<? super JavaClass> target) {
        this.ignoredDependencies.add(new IgnoredDependency(origin, target));
        return this;
    }

    private String[] collect(Set<String> adapters, String... others) {
        Set<String> set = new HashSet<>();
        for (String adapterName : adapters) {
            set.add(getAdapterLayer(adapterName));
        }
        Collections.addAll(set, others);
        return set.toArray(new String[0]);
    }

    private Architectures.LayeredArchitecture layeredArchitectureDelegate() {
        Architectures.LayeredArchitecture layeredArchitectureDelegate = Architectures.layeredArchitecture()
            .layer(DOMAIN_LAYER).definedBy(domainPackageIdentifiers)
            .layer(APPLICATION_SERVICE_LAYER).definedBy(applicationPackageIdentifiers)
            .layer(COMMONS).definedBy(commonPackageIdentitiers);

        for (Map.Entry<String, String[]> adapter : adapterPackageIdentifiers.entrySet()) {
            String adapterLayer = getAdapterLayer(adapter.getKey());
            layeredArchitectureDelegate = layeredArchitectureDelegate
                .layer(adapterLayer).definedBy(adapter.getValue())
                .whereLayer(adapterLayer).mayNotBeAccessedByAnyLayer();
        }

        layeredArchitectureDelegate = layeredArchitectureDelegate
            .whereLayer(DOMAIN_LAYER)
            .mayOnlyBeAccessedByLayers(collect(adapterPackageIdentifiers.keySet(), COMMONS, APPLICATION_SERVICE_LAYER))
            .whereLayer(APPLICATION_SERVICE_LAYER)
            .mayOnlyBeAccessedByLayers(collect(adapterPackageIdentifiers.keySet()))
            .whereLayer(COMMONS)
            .mayOnlyBeAccessedByLayers(
                collect(adapterPackageIdentifiers.keySet(), DOMAIN_LAYER, APPLICATION_SERVICE_LAYER))
            .withOptionalLayers(optionalLayers);

        for (IgnoredDependency ignoredDependency : this.ignoredDependencies) {
            layeredArchitectureDelegate = ignoredDependency.ignoreFor(layeredArchitectureDelegate);
        }
        return layeredArchitectureDelegate.as(getDescription());
    }

    private String getAdapterLayer(String name) {
        return String.format(Locale.getDefault(), "%s %s", name, ADAPTER_LAYER);
    }

    @Override
    public EvaluationResult evaluate(JavaClasses classes) {
        EvaluationResult result = new EvaluationResult(this, Priority.MEDIUM);
        result.add(layeredArchitectureDelegate().evaluate(classes));
        if (!codeRoot.isEmpty()) {
            result.add(classes().that().resideInAPackage(codeRoot).should().resideInAnyPackage(getFullPackageSet())
                .evaluate(classes));
        }
        return result;
    }

    @Override
    public void check(JavaClasses classes) {
        Assertions.check(this, classes);
    }

    private String[] getFullPackageSet() {
        List<String> list = new ArrayList<>(adapterPackageIdentifiers.size() * 2);
        list.addAll(Arrays.asList(domainPackageIdentifiers));
        list.addAll(Arrays.asList(commonPackageIdentitiers));
        list.addAll(Arrays.asList(applicationPackageIdentifiers));
        for (String[] packagesIdentifierers : adapterPackageIdentifiers.values()) {
            list.addAll(Arrays.asList(packagesIdentifierers));
        }
        return list.toArray(new String[0]);

    }

    @Override
    public ArchRule because(String reason) {
        return Factory.withBecause(this, reason);
    }

    /**
     * This method is equivalent to calling {@link #withOptionalLayers(boolean)}, which should be preferred in this
     * context
     * as the meaning is easier to understand.
     */
    @Override
    public ArchRule allowEmptyShould(boolean allowEmptyShould) {
        return withOptionalLayers(allowEmptyShould);
    }

    @Override
    public HexagonalArchitecture as(String newDescription) {
        return new HexagonalArchitecture(codeRoot, domainPackageIdentifiers,
            adapterPackageIdentifiers, applicationPackageIdentifiers, commonPackageIdentitiers, ignoredDependencies,
            Optional.of(newDescription));
    }

    @Override
    public String getDescription() {
        if (overriddenDescription.isPresent()) {
            return overriddenDescription.get();
        }

        List<String> lines
            = newArrayList("Onion architecture consisting of" + (optionalLayers ? " (optional)" : ""));
        if (domainPackageIdentifiers.length > 0) {
            lines.add(String.format(Locale.getDefault(), "domain ('%s')",
                Joiner.on("', '").join(domainPackageIdentifiers)));
        }
        if (commonPackageIdentitiers.length > 0) {
            lines.add(
                String.format(Locale.getDefault(), "commons ('%s')",
                    Joiner.on("', '").join(commonPackageIdentitiers)));
        }
        if (applicationPackageIdentifiers.length > 0) {
            lines.add(String.format(Locale.getDefault(), "application services ('%s')",
                Joiner.on("', '").join(applicationPackageIdentifiers)));
        }
        for (Map.Entry<String, String[]> adapter : adapterPackageIdentifiers.entrySet()) {
            lines.add(
                String.format(Locale.getDefault(), "adapter '%s' ('%s')", adapter.getKey(),
                    Joiner.on("', '").join(adapter.getValue())));
        }
        return Joiner.on(lineSeparator()).join(lines);
    }

    /**
     * @author Thomas Ruhroth (msg system ag) 2022
     */

    private static class IgnoredDependency {
        private final DescribedPredicate<? super JavaClass> origin;
        private final DescribedPredicate<? super JavaClass> target;

        IgnoredDependency(DescribedPredicate<? super JavaClass> origin,
            DescribedPredicate<? super JavaClass> target) {
            this.origin = origin;
            this.target = target;
        }

        Architectures.LayeredArchitecture ignoreFor(Architectures.LayeredArchitecture layeredArchitecture) {
            return layeredArchitecture.ignoreDependency(origin, target);
        }
    }
}
