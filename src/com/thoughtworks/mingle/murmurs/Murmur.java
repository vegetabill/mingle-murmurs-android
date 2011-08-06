package com.thoughtworks.mingle.murmurs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ocpsoft.pretty.time.PrettyTime;
import com.thoughtworks.mingle.murmurs.Murmur.Stream.Origin;

public class Murmur {
  private static final PrettyTime prettyTime = new PrettyTime();

  private static final Map<Integer, Murmur> CACHE = new HashMap<Integer, Murmur>();

  private Integer id;
  private String body;
  private Date created_at;
  private String jabber_user_name;
  private boolean is_truncated;
  private Stream stream;
  private Author author;

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

  public static final String[] COLUMN_NAMES = { "_ID", "TAGLINE", "BODY", "ICON_PATH" };

  public static void cache(Murmur murmur) {
    CACHE.put(murmur.getId(), murmur);
    IconCache.cacheAuthor(murmur.author);
  }

  public int getId() {
    return id;
  }

  public String getAuthor() {
    return author.getName();
  }

  public String getShortBody() {
    if (body.length() > 32) {
      return body.substring(0, 32);
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

  public String getUri() {
    return id + ".murmur";
  }

  public String getIconPathUri() {
    return author.getIconPathUri();
  }

  public String toString() {
    return String.format("%s: %s", this.author, this.body);
  }

  public static Murmur findById(long id) {
    Integer _id = Integer.valueOf((int) id);
    return CACHE.get(_id);
  }

  public static Murmur findByUri(String data) {
    String[] urlParts = data.toString().split("\\.");
    return findById(Integer.parseInt(urlParts[0]));
  }

  public String getTagline() {
    return String.format("-%s (%s)", getAuthor(), getCreatedAtFormatted());
  }

}
