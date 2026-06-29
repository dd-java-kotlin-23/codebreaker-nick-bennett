module edu.cnm.deepdive.codebreaker.services {

  requires dagger;
  requires edu.cnm.deepdive.codebreaker.client;
  requires java.compiler;
  requires jakarta.inject;

  exports edu.cnm.deepdive.codebreaker.model;
  exports edu.cnm.deepdive.codebreaker.service;
  exports edu.cnm.deepdive.codebreaker.exception;

}