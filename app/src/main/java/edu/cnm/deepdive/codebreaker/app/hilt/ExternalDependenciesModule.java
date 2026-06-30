package edu.cnm.deepdive.codebreaker.app.hilt;

import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.codebreaker.client.di.ClientModule;
import edu.cnm.deepdive.codebreaker.service.ServicesModule;

@Module(includes = {ClientModule.class, ServicesModule.class})
@InstallIn(SingletonComponent.class)
public interface ExternalDependenciesModule {

}
