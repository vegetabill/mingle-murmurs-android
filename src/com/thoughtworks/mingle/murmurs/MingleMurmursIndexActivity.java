package com.thoughtworks.mingle.murmurs;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.thoughtworks.mingle.R;

public class MingleMurmursIndexActivity extends ListActivity {

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    SimpleAdapter simpleAdapter = new SimpleAdapter(this, Murmur.MURMURS_DATA,
        R.layout.murmur_summary,
        new String[] { "AUTHOR", "BODY", "CREATED_AT" }, new int[] {
            R.id.author, R.id.body, R.id.createdAt });
    setListAdapter(simpleAdapter);

    ListView listView = getListView();
    listView.setTextFilterEnabled(true);

    Toast
        .makeText(MingleMurmursIndexActivity.this, "osito", Toast.LENGTH_SHORT);

    getListView().setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position,
          long id) {
        Intent showContent = new Intent(getApplicationContext(),
            MingleMurmursShowActivity.class);
        showContent.setData(Murmur.findById(id).getUri());
        startActivity(showContent);
      }
    });

  }
}