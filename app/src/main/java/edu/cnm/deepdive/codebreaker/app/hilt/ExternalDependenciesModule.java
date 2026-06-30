package edu.cnm.deepdive.codebreaker.app.hilt;

import dagger.Module;
import edu.cnm.deepdive.codebreaker.client.di.ClientModule;
import edu.cnm.deepdive.codebreaker.service.ServicesModule;

@Module(includes = {ClientModule.class, ServicesModule.class})
public interface ExternalDependenciesModule {

}
