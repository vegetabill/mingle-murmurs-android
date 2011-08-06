package com.thoughtworks.mingle.murmurs;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.thoughtworks.mingle.R;

public class MingleMurmursShowActivity extends Activity {

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.murmur_details);
    Intent launchingIntent = getIntent();
    String simpleUri = launchingIntent.getData().toString();
    Log.d(MingleMurmursShowActivity.class.getName(), "simpleUri: " + simpleUri);
    String id = simpleUri.split("\\.")[0];
    Log.d(MingleMurmursShowActivity.class.getName(), "id: " + id);
    String whereClause = "id=" + id;
    Cursor cursor = managedQuery(MurmurContentProvider.CONTENT_URI,
        null, // Which columns to return (all)
        whereClause, // Which rows to return
        null, // Selection arguments (none)
        null);
    
    cursor.moveToFirst();
    
    TextView author = (TextView) findViewById(R.id.author);
    author.setText(cursor.getString(0));
    TextView createdAt = (TextView) findViewById(R.id.createdAt);
    createdAt.setText(cursor.getString(1));
    TextView body = (TextView) findViewById(R.id.body);
    body.setText(cursor.getString(2));
  }

}
