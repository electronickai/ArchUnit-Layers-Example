package deprecated.callers;

import deprecated.callees.ClassContainingDeprecatedMethod;
import deprecated.callees.DeprecatedClass;

public class CallerOfDeprecatedMethod {

  ClassContainingDeprecatedMethod testClass = new ClassContainingDeprecatedMethod();

  public void callDeprecatedMethod() {
    DeprecatedClass.methodOfDeprecatedClass();
    testClass.regularMethod();
    testClass.deprecatedMethod();
  }
}
