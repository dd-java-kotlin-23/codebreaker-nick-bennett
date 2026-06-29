package edu.cnm.deepdive.codebreaker.cli.di;

import edu.cnm.deepdive.codebreaker.cli.viewmodel.CodebreakerViewModel;

public final class CliDependencies {

  private CliDependencies() {
  }

  public static CodebreakerViewModel codebreakerViewModel() {
    return DaggerCliComponent.create().codebreakerViewModel();
  }

}
