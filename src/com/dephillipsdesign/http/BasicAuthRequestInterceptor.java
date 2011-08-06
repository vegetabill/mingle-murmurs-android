package com.dephillipsdesign.http;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

public class BasicAuthRequestInterceptor implements HttpRequestInterceptor {
  
  public BasicAuthRequestInterceptor(DefaultHttpClient client, URI uri, String username, String password) {
    client.getCredentialsProvider().setCredentials(
        new AuthScope(uri.getHost(), uri.getPort()),
        new UsernamePasswordCredentials(username, password));
    
    client.addRequestInterceptor(this, 0);
  }

  public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
    AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
    CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
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
  
}
