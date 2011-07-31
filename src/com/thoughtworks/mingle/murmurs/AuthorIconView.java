package com.thoughtworks.mingle.murmurs;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.dephillipsdesign.http.HttpClientUtil;

public class AuthorIconView extends ImageView {

  public AuthorIconView(Context context) {
    super(context);
  }

  public AuthorIconView(Context context, AttributeSet attributes) {
    this(context, attributes, 0);
  }

  public AuthorIconView(Context context, AttributeSet attributes, int style) {
    super(context, attributes, style);
  }

  private static final Map<String, Bitmap> uriToBitmap = new ConcurrentHashMap<String, Bitmap>(
      new HashMap<String, Bitmap>());

  public void setImageURI(final Uri uri) {
    Log.d(AuthorIconView.class.getName(), "Setting uri: " + uri);
    IconViewDownloader iconDownloader = new IconViewDownloader(new Handler() {
      public void handleMessage(Message msg) {
        Log.d(IconCache.class.getName(),
            "Setting icon from populated hash for " + uri.toString());
        setImageBitmap(uriToBitmap.get(uri.toString()));
      }
    });
    iconDownloader.execute(uri.toString());
  }

  class IconViewDownloader extends AsyncTask<String, Void, Void> {

    private final Handler handler;

    public IconViewDownloader(Handler handler) {
      this.handler = handler;
    }

    protected Void doInBackground(String... params) {
      String uri = params[0];
      try {
        String iconPath = Settings.getMingleHost() + uri;
        InputStream stream = HttpClientUtil.openInputStream(iconPath);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        uriToBitmap.put(uri, bitmap);
        Log.d(IconCache.class.getName(),
            "Stored icon from " + iconPath + " into bitmap " + bitmap.getWidth() + "x" + bitmap.getHeight());
        handler.sendEmptyMessage(0);
      } catch (Exception e) {
        Log.e(IconCache.class.getName(), "Unable to download icon from " + uri, e);
      }
      return null;
    }
  }

}
