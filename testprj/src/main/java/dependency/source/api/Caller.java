package dependency.source.api;

import dependency.bar.api.OtherDependency;
import dependency.foo.api.Dependency;

public class Caller {

  Dependency dependencyInFoo = new Dependency();
  OtherDependency dependenyInBar = new OtherDependency();

  public void callingFunction() {
    dependencyInFoo.doSomething();
    dependenyInBar.doSomething();
  }
}
