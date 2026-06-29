package edu.cnm.deepdive.codebreaker.app;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class CodebreakerApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    // TODO: 6/29/26 Add other initialization, as required.
  }

}
