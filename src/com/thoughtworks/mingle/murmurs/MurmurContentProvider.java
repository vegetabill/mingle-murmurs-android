package com.thoughtworks.mingle.murmurs;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.dephillipsdesign.http.HttpClientUtil;

public class MurmurContentProvider extends ContentProvider {

  private static final boolean EMULATOR = true;
  public static final Uri CONTENT_URI = Uri.parse("content://com.thoughtworks.mingle.murmurs");

  private InputStream openRemoteUri() {
    try {
      String host = EMULATOR ? "10.0.2.2" : "192.168.1.66";
      String uri = "http://" + host + ":8080/api/v2/projects/bearbot/murmurs.xml";
      Log.i("murmurs", uri);
      return HttpClientUtil.openInputStream(uri, "admin", "p");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Cursor query(Uri uri, String[] columns, String where_clause, String[] selection, String order_by) {
    MatrixCursor cursor = new MatrixCursor(Murmur.COLUMN_NAMES);

    List<Murmur> murmurs = Murmur.loadFromXml(openRemoteUri());
    Collections.sort(murmurs, new Comparator<Murmur>() {
      public int compare(Murmur m1, Murmur m2) {
        return Integer.valueOf(m2.getId()).compareTo(Integer.valueOf(m1.getId()));
      }
    });
    for (Murmur m : murmurs) {
      cursor.addRow(new Object[] { m.getId(), m.getAuthor(), m.getCreatedAtFormatted(), m.getBody() });
    }

    return cursor;
  }

  public String getType(Uri uri) {
    return "vnd.android.cursor.dir/vnd.com.thoughtworks.mingle.murmur";
  }

  public int delete(Uri arg0, String arg1, String[] arg2) {
    throw new UnsupportedOperationException();
  }

  public Uri insert(Uri arg0, ContentValues arg1) {
    throw new UnsupportedOperationException();
  }

  public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
    throw new UnsupportedOperationException();
  }

  public boolean onCreate() {
    return true;
  }

}
