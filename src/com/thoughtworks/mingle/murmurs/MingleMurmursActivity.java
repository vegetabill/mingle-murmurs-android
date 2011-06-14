package com.thoughtworks.mingle.murmurs;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.thoughtworks.mingle.R;

public class MingleMurmursActivity extends ListActivity {
	
	private ListView murmursListView;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.murmurs_list);
//        murmursListView = (ListView) findViewById(R.id.murmursList);
        
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, Murmur.MURMURS_DATA, R.layout.murmur_summary,
        		new String[] { "AUTHOR", "BODY", "CREATED_AT" },
        		new int[] { R.id.author, R.id.body, R.id.createdAt });
//        setListAdapter(new ArrayAdapter<Murmur>(this, R.layout.murmur_summary, Murmur.TEST_MURMURS));
        setListAdapter(simpleAdapter);
        
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        
        getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
			}
		});
        	
        
    }
}