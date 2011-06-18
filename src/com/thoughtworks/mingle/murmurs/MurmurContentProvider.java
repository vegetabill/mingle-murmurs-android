package com.thoughtworks.mingle.murmurs;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

public class MurmurContentProvider extends ContentProvider {

  public static final Uri CONTENT_URI = Uri.parse("content://com.thoughtworks.mingle.murmurs");

  public Cursor query(Uri uri, String[] columns, String where_clause, String[] selection, String order_by) {
    MatrixCursor cursor = new MatrixCursor(Murmur.COLUMN_NAMES);
    List<Murmur> murmurs = Murmur.loadFromXml();
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
