package edu.cnm.deepdive.codebreaker.cli.di;

import dagger.Component;
import edu.cnm.deepdive.codebreaker.cli.viewmodel.CodebreakerViewModel;
import edu.cnm.deepdive.codebreaker.client.di.ClientModule;
import edu.cnm.deepdive.codebreaker.service.ServicesModule;
import jakarta.inject.Singleton;

@Singleton
@Component(modules = {
    ClientModule.class,
    ServicesModule.class
})
public interface CliComponent {

  CodebreakerViewModel codebreakerViewModel();

}
