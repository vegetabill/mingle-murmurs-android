package com.thoughtworks.mingle.murmurs;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.thoughtworks.mingle.R;

public class MingleMurmursIndexActivity extends ListActivity {

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Uri murmurs_uri = Murmur.CONTENT_URI;

    // Make the query.
    Cursor cursor = managedQuery(murmurs_uri,
        null, // Which columns to return
        null, // Which rows to return (all rows)
        null, // Selection arguments (none)
        null);

    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.murmur_summary,
        cursor, Murmur.COLUMN_NAMES,
        new int[] { 0, R.id.author, R.id.createdAt, R.id.body });
    setListAdapter(adapter);
    ListView listView = getListView();
    listView.setTextFilterEnabled(true);

    getListView().setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position,
          long id) {
        Log.i("osito", "id: " + id);
        Intent showContent = new Intent(getApplicationContext(),
            MingleMurmursShowActivity.class);
        showContent.setData(Murmur.findById(id).getUri());
        startActivity(showContent);
      }
    });

  }
}