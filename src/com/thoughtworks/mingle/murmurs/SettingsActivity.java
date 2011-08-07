package com.thoughtworks.mingle.murmurs;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.thoughtworks.mingle.R;

public class SettingsActivity extends Activity {

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.settings);
    
    final SharedPreferences preferences = getSharedPreferences(Settings.class.getCanonicalName(), MODE_PRIVATE);
    final EditText hostnameField = (EditText) findViewById(R.id.hostname);
    hostnameField.setText(Settings.getMingleHost());
    final EditText projectIdentifierField = (EditText) findViewById(R.id.projectIdentifier);
    projectIdentifierField.setText(Settings.getProjectIdentifier());
    final EditText loginField = (EditText) findViewById(R.id.login);
    loginField.setText(Settings.getLogin());
    
    findViewById(R.id.saveButton).setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        Editor editor = preferences.edit();
        
        String newHostname = hostnameField.getText().toString().trim();
        if (newHostname.endsWith("/")) {
          newHostname = newHostname.replaceAll("/$", "");
        }
        editor.putString("host", newHostname);
        
        editor.putString("projectIdentifier", projectIdentifierField.getText().toString().trim());
        editor.putString("login", loginField.getText().toString().trim());

        final EditText passwordField = (EditText) findViewById(R.id.password);
        String newPassword = passwordField.getText().toString().trim();
        if (newPassword.length() > 0) {
          editor.putString("password", newPassword);
        }
        
        editor.commit();
        finish();
      }
    });
  }
  
}
