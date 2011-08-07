package com.thoughtworks.mingle.murmurs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thoughtworks.mingle.R;

public class MingleMurmursCreateActivity extends Activity {

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.new_murmur);
    
    Button button = (Button) findViewById(R.id.saveButton);
    button.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        try {
         EditText murmurBodyInput = (EditText) findViewById(R.id.newMurmurBody);
         Murmur murmur = new Murmur(murmurBodyInput.getText().toString());
         murmur.saveAsNew();
         finish();
        } catch (Exception e) {
          Toast.makeText(getBaseContext(), "Problem saving murmur.  Try again later.", Toast.LENGTH_SHORT);
          throw new RuntimeException(e);
        } 
      }
    });
  }
  
}
