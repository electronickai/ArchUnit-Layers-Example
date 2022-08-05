package deprecated.callers;

import deprecated.callees.ClassContainingDeprecatedMethod;
import deprecated.callees.DeprecatedClass;

@Deprecated
public class DeprecatedClassCallerOfDeprecatedMethod {

  ClassContainingDeprecatedMethod testClass = new ClassContainingDeprecatedMethod();

//  @Deprecated
  public void callDeprecatedMethod() {
    DeprecatedClass.methodOfDeprecatedClass();
    testClass.regularMethod();
    testClass.deprecatedMethod();
  }
}
