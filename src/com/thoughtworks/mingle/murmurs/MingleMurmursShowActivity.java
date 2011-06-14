package com.thoughtworks.mingle.murmurs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.thoughtworks.mingle.R;

public class MingleMurmursShowActivity extends Activity {

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.murmur_details);

    Intent launchingIntent = getIntent();
    Murmur murmur = Murmur.findByUri(launchingIntent.getData());
    TextView author = (TextView) findViewById(R.id.author);
    author.setText(murmur.getAuthor());
    TextView createdAt = (TextView) findViewById(R.id.createdAt);
    createdAt.setText(murmur.getCreatedAtFormatted());
    TextView body = (TextView) findViewById(R.id.body);
    body.setText(murmur.getBody());
  }

}
