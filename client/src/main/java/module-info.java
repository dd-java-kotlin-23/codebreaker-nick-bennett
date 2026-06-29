module edu.cnm.deepdive.codebreaker.client {

  requires dagger;
  requires jakarta.inject;
  requires java.compiler;
  requires kotlin.stdlib;
  requires kotlinx.coroutines.core;
  requires kotlinx.coroutines.jdk8;
  requires retrofit2;
  requires retrofit2.converter.moshi;
  requires com.squareup.moshi;
  requires okhttp3;
  requires okhttp3.logging;

  exports edu.cnm.deepdive.codebreaker.client.dto;
  exports edu.cnm.deepdive.codebreaker.client.di;
  exports edu.cnm.deepdive.codebreaker.client.service;

  opens edu.cnm.deepdive.codebreaker.client.dto to com.squareup.moshi;

}
