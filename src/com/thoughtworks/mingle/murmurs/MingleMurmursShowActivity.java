package com.thoughtworks.mingle.murmurs;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtworks.mingle.R;

public class MingleMurmursShowActivity extends Activity {

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.murmur_details);
    Intent launchingIntent = getIntent();
    int id = Murmur.getIdFromUri(launchingIntent.getData().toString());
    String whereClause = "id=" + id;
    Cursor cursor = managedQuery(MurmurContentProvider.CONTENT_URI,
        null, // Which columns to return (all)
        whereClause, // Which rows to return
        null, // Selection arguments (none)
        null);
    
    cursor.moveToFirst();
    
    TextView author = (TextView) findViewById(R.id.author);
    author.setText(cursor.getString(1));
    TextView createdAt = (TextView) findViewById(R.id.createdAt);
    createdAt.setText(cursor.getString(2));
    TextView body = (TextView) findViewById(R.id.body);
    body.setText(cursor.getString(3));
    ImageView icon = (ImageView) findViewById(R.id.icon);
    icon.setImageURI(Uri.parse(cursor.getString(4)));
  }

}
