package com.thoughtworks.mingle.murmurs;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class Application extends android.app.Application {
  
  public static Context APPLICATION_CONTEXT;
  
  public void onCreate() {
    super.onCreate();
    APPLICATION_CONTEXT = this;
    
    //Defaults for testing - TODO: remove
    Editor editor = Settings.getPreferences().edit();
    editor.putString("login", "bill");
    editor.putString("password", "p");
    editor.putString("host", "http://10.0.2.2:4001");
    editor.putString("projectIdentifier", "bearbot");
    editor.commit();
  }
}
