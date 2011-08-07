package com.thoughtworks.mingle.murmurs;

import java.net.URI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

public class Settings {

  public static String getLogin() {
    return getPreferences().getString("login", null);
  }

  public static String getPassword() {
    return getPreferences().getString("password", null);
  }

  public static String getMingleHost() {
    return getPreferences().getString("host", null);
  }

  public static String getProjectPath() {
    return getMingleHost() + "/api/v2/projects/" + getProjectIdentifier();
  }
  
  public static String getProjectIdentifier() {
    return getPreferences().getString("projectIdentifier", null);
  }

  public static int getPort() {
    return URI.create(getMingleHost()).getPort();
  }

  public static String getLocalStoragePath() {
    return Environment.getExternalStorageDirectory().getAbsolutePath();
  }
  
  static SharedPreferences getPreferences() {
    return Application.APPLICATION_CONTEXT.getSharedPreferences(Settings.class.getCanonicalName(), Context.MODE_PRIVATE);
  }

}
