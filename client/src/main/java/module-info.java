module edu.cnm.deepdive.codebreaker.client {

  requires kotlin.stdlib;
  requires kotlinx.coroutines.core;
  requires retrofit2;
  requires retrofit2.converter.moshi;
  requires com.squareup.moshi;
  requires okhttp3;

  exports edu.cnm.deepdive.codebreaker.client.dto;
  exports edu.cnm.deepdive.codebreaker.client.service;

  opens edu.cnm.deepdive.codebreaker.client.dto to com.squareup.moshi;

}