package de.test.core;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import hamburg.kaischmidt.myarctestlib.arctests.AddMethodsMustAccessSetCondition;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.codeUnits;

@AnalyzeClasses(packages = "de.test.core..")
public class AddMethodsMustAccessSetTest {

  @ArchTest
  static final ArchRule addMethodsMustAccessSet = codeUnits().should(new AddMethodsMustAccessSetCondition());

}
