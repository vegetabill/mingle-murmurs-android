package com.dephillipsdesign.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

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
      client.getCredentialsProvider().setCredentials(
          new AuthScope(uri.getHost(), uri.getPort()),
          new UsernamePasswordCredentials(username, password));

      HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
          AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
          CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(
                    ClientContext.CREDS_PROVIDER);
          HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);

          if (authState.getAuthScheme() == null) {
            AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
            Credentials creds = credsProvider.getCredentials(authScope);
            if (creds != null) {
              authState.setAuthScheme(new BasicScheme());
              authState.setCredentials(creds);
            }
          }
        }
      };

      HttpGet request = new HttpGet(new URI(url));
      client.addRequestInterceptor(preemptiveAuth, 0);

      HttpResponse response = client.execute(request);
      InputStream is = response.getEntity().getContent();
      if (DEBUG) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
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
      throw new RuntimeException(e);
    }
  }
}
