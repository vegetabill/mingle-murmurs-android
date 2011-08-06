package com.dephillipsdesign.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

import com.thoughtworks.mingle.murmurs.Settings;

public class HttpClientUtil {

  private static final boolean DEBUG = false;

  public static InputStream openInputStream(String url) {
    return openInputStream(url, Settings.getUsername(), Settings.getPassword());
  }

  private static InputStream openInputStream(String url, String username, String password) {
    try {
      URI uri = URI.create(url);
      DefaultHttpClient client = new DefaultHttpClient();
      new BasicAuthRequestInterceptor(client, uri, username, password);
      HttpGet request = new HttpGet(new URI(url));
      

      HttpResponse response = client.execute(request);
      InputStream is = new BufferedInputStream(response.getEntity().getContent(), 4048);
      if (DEBUG) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8092);
        String s = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((s = reader.readLine()) != null) {
          Log.d(HttpClientUtil.class.getCanonicalName(), s);
          baos.write(s.getBytes());
        }
        return new ByteArrayInputStream(baos.toByteArray());
      } else {
        return is;
      }
    } catch (Exception e) {
      throw new RuntimeException("Error loading resource " + url, e);
    }
  }
}
