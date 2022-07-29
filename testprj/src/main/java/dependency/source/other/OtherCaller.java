package dependency.source.other;

import dependency.bar.api.OtherDependency;

public class OtherCaller {

  OtherDependency dependenyInBar = new OtherDependency();

  public void callingFunction() {
    dependenyInBar.doSomething();
  }
}
