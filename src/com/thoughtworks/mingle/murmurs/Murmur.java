package com.thoughtworks.mingle.murmurs;

import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import android.util.Log;

import com.dephillipsdesign.http.HttpClientUtil;
import com.ocpsoft.pretty.time.PrettyTime;
import com.thoughtworks.mingle.murmurs.Murmur.Stream.Origin;

public class Murmur {

  private static final PrettyTime prettyTime = new PrettyTime();

  private Integer id;
  private String body;
  private Date created_at;
  private String jabber_user_name;
  private boolean is_truncated;
  private Stream stream;
  private Author author;
  
  /**
   * For xstream
   */
  public Murmur() {}
  
  /**
   * For posting to Mingle
   * @param body
   */
  public Murmur(String body) {
    this.body = body;
  }

  /**
   * Container class to allow the XML doc to be mapped easily
   */
  public static class Murmurs {

    private List<Murmur> murmurs;

    public List<Murmur> getMurmurs() {
      return murmurs;
    }
  }

  public static class Stream {
    private String type;
    private Origin origin;

    public static class Origin {
      private String url;
      private Integer number;
    }
  }

  public static final String[] SUMMARY_COLUMN_NAMES = { "_ID", "TAGLINE", "BODY", "ICON_PATH" };

  public static final String[] DETAIL_COLUMN_NAMES = { "_ID", "AUTHOR", "CREATED_AT", "BODY", "ICON_PATH" };

  public static void cache(Murmur murmur) {
    IconCache.cacheAuthor(murmur.author);
  }

  public int getId() {
    return id;
  }

  public String getAuthor() {
    return author.getName();
  }

  public String getShortBody() {
    if (body.length() > 128) {
      return body.substring(0, 128);
    }
    return body;
  }

  public String getBody() {
    return body;
  }

  public Date getCreatedAt() {
    return created_at;
  }

  public String getCreatedAtFormatted() {
    return prettyTime.format(this.created_at);
  }

  public String getIconPathUri() {
    return author.getIconPathUri();
  }

  public String toString() {
    return String.format("%s: %s", this.author, this.body);
  }

  public String getTagline() {
    return String.format("-%s (%s)", getAuthor(), getCreatedAtFormatted());
  }

  public static Uri constructUri(long id) {
    return Uri.parse(id + ".murmur");
  }

  public static int getIdFromUri(String uri) {
    return Integer.parseInt(uri.split("\\.")[0]);
  }

  public void saveAsNew() {
    String postUrl = Settings.getProjectPath() + "/murmurs.xml";
    InputStream newMurmurXmlStream = HttpClientUtil.postRequest(postUrl, Collections.singletonMap("murmur[body]", getBody().trim()));
    Murmur newMurmur = new MurmursLoader().loadOneFromXml(newMurmurXmlStream);
    this.author = newMurmur.author;
    this.body = newMurmur.body;
    this.created_at = newMurmur.created_at;
    this.id = newMurmur.id;
    this.is_truncated = newMurmur.is_truncated;
    this.jabber_user_name = newMurmur.jabber_user_name;
    this.stream = newMurmur.stream;
  }
}
