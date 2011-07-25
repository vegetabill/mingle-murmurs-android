package com.thoughtworks.mingle.murmurs;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.dephillipsdesign.http.HttpClientUtil;

public class IconCache {

  private static final Map<String, String> nameToIconPath = new ConcurrentHashMap<String, String>(
      new HashMap<String, String>());
  private static final Map<String, Bitmap> nameToBitmap = new ConcurrentHashMap<String, Bitmap>(
      new HashMap<String, Bitmap>());

  private static void setIconPath(String name, String iconPath) {
    if (!nameToIconPath.containsKey(name)) {
      Log.d(IconCache.class.getName(), "Storing icon for " + name + " from: " + iconPath);
      nameToIconPath.put(name, iconPath);
      queueDownload(name, iconPath);
    }

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
        final Bitmap bitmap = BitmapFactory.decodeStream(HttpClientUtil.openInputStream(iconPath));
        IconCache.nameToBitmap.put(name, bitmap);
        Log.d(IconCache.class.getName(),
            "Successfully stored icon for " + name + ", dimensions: " + bitmap.getWidth() + "x" + bitmap.getHeight());
      } catch (Exception e) {
        Log.e(IconCache.class.getName(), "Unable to download icon for " + name + " from " + iconPath, e);
      }
      return null;
    }
  }

}
