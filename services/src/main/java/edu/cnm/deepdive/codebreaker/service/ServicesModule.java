package edu.cnm.deepdive.codebreaker.service;

import dagger.Module;
import dagger.Provides;
import edu.cnm.deepdive.codebreaker.client.service.CodebreakerProxy;
import jakarta.inject.Singleton;

@Module
public final class ServicesModule {

  ServicesModule() {
  }

  @Provides
  @Singleton
  public CodebreakerService provideCodebreakerService(CodebreakerProxy proxy) {
    return new CodebreakerServiceImpl(proxy);
  }

}
