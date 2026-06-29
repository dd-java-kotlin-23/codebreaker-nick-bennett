package edu.cnm.deepdive.codebreaker.javafx.di;

import dagger.Component;
import edu.cnm.deepdive.codebreaker.client.di.ClientModule;
import edu.cnm.deepdive.codebreaker.javafx.controller.MainController;
import edu.cnm.deepdive.codebreaker.service.ServicesModule;
import jakarta.inject.Singleton;

@Singleton
@Component(modules = {
    ClientModule.class,
    ServicesModule.class
})
public interface JavaFxComponent {

  MainController mainController();

}
