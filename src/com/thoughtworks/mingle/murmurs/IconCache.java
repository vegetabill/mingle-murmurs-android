package com.thoughtworks.mingle.murmurs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.os.AsyncTask;
import android.util.Log;

import com.dephillipsdesign.http.HttpClientUtil;
import com.dephillipsdesign.io.IOUtils;

public class IconCache {

  private static final Map<String, String> nameToLocalIconPath = new ConcurrentHashMap<String, String>(
      new HashMap<String, String>());

  private static void setIconPath(String name, String iconPath) {
    if (!nameToLocalIconPath.containsKey(name)) {
      Log.d(IconCache.class.getName(), "Storing icon for " + name + " from: " + iconPath);
      nameToLocalIconPath.put(name, iconPath);
      // queueDownload(name, iconPath);
    }

  }

  public static String getIconPath(String name) {
    return nameToLocalIconPath.get(name);
  }

  public static void cacheAuthor(Author author) {
    setIconPath(author.getName(), author.getIconPath());
  }

  private static void queueDownload(String name, String iconPath) {
    IconDownloader iconDownloader = new IconDownloader();
    iconDownloader.execute(name, iconPath);
  }

  public static class IconDownloader extends AsyncTask<String, Void, Void> {
    protected Void doInBackground(String... params) {
      String name = params[0];
      String iconPath = params[1];
      try {
        iconPath = Settings.getMingleHost() + iconPath;
        InputStream inputStream = HttpClientUtil.openInputStream(iconPath);
        String[] pathParts = iconPath.split("/");
        String simpleFilename = pathParts[pathParts.length - 1];
        File iconFile = new File(Settings.getLocalStoragePath(), simpleFilename);
        OutputStream outputStream = new FileOutputStream(iconFile);
        IOUtils.copyStream(inputStream, outputStream);
        nameToLocalIconPath.put(name, "file://" + iconFile.getAbsolutePath());
        Log.d(IconCache.class.getName(),
            "Successfully stored icon for " + name + " at " + iconFile);
      } catch (Exception e) {
        Log.e(IconCache.class.getName(), "Unable to download icon for " + name + " from " + iconPath, e);
      }
      return null;
    }
  }

}
