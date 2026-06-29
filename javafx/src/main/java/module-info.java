module edu.cnm.deepdive.codebreaker.javafx {

  requires dagger;
  requires java.compiler;
  requires jakarta.inject;
  requires javafx.controls;
  requires javafx.fxml;
  requires edu.cnm.deepdive.codebreaker.client;
  requires edu.cnm.deepdive.codebreaker.services;

  exports edu.cnm.deepdive.codebreaker.javafx to javafx.graphics;
  exports edu.cnm.deepdive.codebreaker.javafx.di;
  exports edu.cnm.deepdive.codebreaker.javafx.controller to dagger;
  exports edu.cnm.deepdive.codebreaker.javafx.viewmodel to dagger;

  opens edu.cnm.deepdive.codebreaker.javafx to javafx.fxml;
  opens edu.cnm.deepdive.codebreaker.javafx.controller to javafx.fxml;
  opens edu.cnm.deepdive.codebreaker.javafx.adapter to javafx.fxml;

}