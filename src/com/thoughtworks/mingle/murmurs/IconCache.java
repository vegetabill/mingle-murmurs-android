package com.thoughtworks.mingle.murmurs;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.dephillipsdesign.http.HttpClientUtil;
import com.dephillipsdesign.io.IOUtils;

public class IconCache {

  private static final Map<String, String> nameToRemoteIconPath = new ConcurrentHashMap<String, String>(
      new HashMap<String, String>());

  private static final Map<String, String> remotePathToLocalPath = new ConcurrentHashMap<String, String>(
      new HashMap<String, String>());

  public static String getRemoteIconPath(String name) {
    return nameToRemoteIconPath.get(name);
  }

  public static String getCachedIconPath(Uri remoteUri) {
    return remotePathToLocalPath.get(remoteUri.toString());
  }

  public static void cacheAuthor(Author author) {
    if (!nameToRemoteIconPath.containsKey(author.getName())) {
      Log.d(IconCache.class.getName(), "Recording icon for " + author.getName() + " from: " + author.getIconPath());
      nameToRemoteIconPath.put(author.getName(), author.getIconPath());
    }
  }

  private static File getTempFolder() {
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      File externalRoot = Environment.getExternalStorageDirectory();
      File tempDir = new File(externalRoot, ".mingle-murmurs/icon-cache");
      tempDir.mkdirs();
      return tempDir;
    }
    throw new RuntimeException("Unable to save icons to temp folder");
  }

  private static void storeIcon(String iconPath) {
    synchronized (remotePathToLocalPath) {
      if (!remotePathToLocalPath.containsKey(iconPath)) {
        String uri = Settings.getMingleHost() + iconPath;
        InputStream stream = HttpClientUtil.openInputStream(uri);
        String[] pathSegments = iconPath.split("/");
        String relativePath = pathSegments[pathSegments.length - 1];
        File tempFile = new File(getTempFolder(), relativePath);
        IOUtils.writeStream(stream, tempFile);
        remotePathToLocalPath.put(iconPath, tempFile.getAbsolutePath());
        Log.d(IconCache.class.getName(), "Stored icon from " + iconPath + " to " + tempFile);
      } else {
        Log.d(IconCache.class.getName(), "Skipped storing icon from " + iconPath + " since it was already stored");
      }
    }
  }

  public static class IconViewDownloader extends AsyncTask<String, Void, Void> {

    private final Handler handler;

    public IconViewDownloader(Handler handler) {
      this.handler = handler;
    }

    protected Void doInBackground(String... params) {
      String uri = params[0];
      try {
        storeIcon(uri);
        handler.sendEmptyMessage(0);
      } catch (Exception e) {
        Log.e(IconCache.class.getName(), "Unable to download icon from " + uri, e);
      }
      return null;
    }
  }

}
