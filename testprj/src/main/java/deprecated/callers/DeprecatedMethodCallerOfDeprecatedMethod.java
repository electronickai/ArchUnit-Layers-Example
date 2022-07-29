package deprecated.callers;

import deprecated.callees.ClassContainingDeprecatedMethod;
import deprecated.callees.DeprecatedClass;

public class DeprecatedMethodCallerOfDeprecatedMethod {

  ClassContainingDeprecatedMethod testClass = new ClassContainingDeprecatedMethod();

  @Deprecated
  public void callDeprecatedMethod() {
    DeprecatedClass.methodOfDeprecatedClass();
    testClass.regularMethod();
    testClass.deprecatedMethod();
  }
}
