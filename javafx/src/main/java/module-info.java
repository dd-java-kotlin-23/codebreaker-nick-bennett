module edu.cnm.deepdive.codebreaker.javafx {

  requires javafx.controls;
  requires javafx.fxml;
  requires edu.cnm.deepdive.codebreaker.services;

  exports edu.cnm.deepdive.codebreaker.javafx to javafx.graphics;

  opens edu.cnm.deepdive.codebreaker.javafx to javafx.fxml;
  opens edu.cnm.deepdive.codebreaker.javafx.controller to javafx.fxml;

}