package com.thoughtworks.mingle.murmurs;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ocpsoft.pretty.time.PrettyTime;
import com.thoughtworks.mingle.murmurs.Murmur.Stream.Origin;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Murmur {
  private static final DateConverter DATE_CONVERTER = new DateConverter("yyyy-MM-dd'T'HH:mm:ss'Z'",
      new String[] { "yyyy-MM-dd'T'HH:mm:ssZ" });

  // murmurs resource xml - attributes
  // id: Integer; read only, the system generated id for a murmur. This id is
  // globally unique.
  // author: Resource; details of the user who has created a murmur. If the
  // murmur was posted using Jabber, this will be only available if the user has
  // entered their Jabber user name on their Mingle profile page.
  // body: String; the content of a murmur
  // created_at: Datetime; read only, date and time of a murmur creation.
  // jabber_user_name: String; if the murmur was posted using Jabber, this is
  // the name of the Jabber user who created the murmur.
  // is_truncated: Boolean; indicates if the murmur body is truncated. If the
  // body is truncated, you will need to request that specific murmur to see the
  // full murmur body.
  // stream: XML Element; read only, Where the murmur came from. The type
  // attribute will be either, "comment" or "default". If stream type is
  // "comment", additional information about where the comment was made will be
  // included.

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

  public static final String[] COLUMN_NAMES = { "_ID", "AUTHOR", "CREATED_AT", "BODY" };

  public static void cache(Murmur murmur) {
    CACHE.put(murmur.getId(), murmur);
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

  public String toString() {
    return String.format("%s: %s", this.author, this.body);
  }

  public Map<String, String> toMap() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("AUTHOR", this.author.getName());
    map.put("BODY", this.body);
    map.put("CREATED_AT", prettyTime.format(this.created_at));
    return map;
  }

  public static Murmur findById(long id) {
    Integer _id = Integer.valueOf((int) id);
    return CACHE.get(_id);
  }

  public static Murmur findByUri(String data) {
    String[] urlParts = data.toString().split("\\.");
    return findById(Integer.parseInt(urlParts[0]));
  }

  public static List<Murmur> loadFromXml(InputStream inputStream) {
    XStream xstream = new XStream(new DomDriver());
    xstream.addImplicitCollection(Murmurs.class, "murmurs", Murmur.class);
    xstream.alias("murmurs", Murmurs.class);
    xstream.alias("murmur", Murmur.class);
    xstream.alias("author", Author.class);
    xstream.alias("stream", Stream.class);
    xstream.alias("origin", Origin.class);
    xstream.registerLocalConverter(Murmur.class, "created_at",
        DATE_CONVERTER);
    xstream.registerLocalConverter(Author.class, "last_login_at",
        DATE_CONVERTER);
    Murmurs murmurs = (Murmurs) xstream.fromXML(inputStream);
    for (Murmur m : murmurs.getMurmurs()) {
      cache(m);
    }
    return murmurs.getMurmurs();
  }

  final static String TEST_XML = "<murmurs type=\"array\">\n" +
      "  <murmur>\n" +
      "    <id type=\"integer\">3</id>\n" +
      "    <author url=\"http://localhost:8080/api/v2/users/2.xml\">\n" +
      "      <id type=\"integer\">2</id>\n" +
      "      <name>xBilyGoatx</name>\n" +
      "      <login>bill</login>\n" +
      "      <email nil=\"true\" />\n" +
      "      <light type=\"boolean\">false</light>\n" +
      "      <icon_path>/user/icon/2/bill.JPG</icon_path>\n" +
      "    </author>\n" +
      "    <body>\n" +
      "      I created a patch for this bug but can't push it yet\n" +
      "    </body>\n" +
      "    <created_at type=\"datetime\">2011-06-18T05:02:03Z</created_at>\n" +
      "    <jabber_user_name nil=\"true\" />\n" +
      "    <is_truncated type=\"boolean\">false</is_truncated>\n" +
      "    <stream type=\"comment\">\n" +
      "      <origin url=\"http://localhost:8080/api/v2/projects/bearbot/cards/14.xml\">\n" +
      "        <number type=\"integer\">14</number>\n" +
      "      </origin>\n" +
      "    </stream>\n" +
      "  </murmur>\n" +
      "  <murmur>\n" +
      "    <id type=\"integer\">2</id>\n" +
      "    <author url=\"http://localhost:8080/api/v2/users/2.xml\">\n" +
      "      <id type=\"integer\">2</id>\n" +
      "      <name>xBilyGoatx</name>\n" +
      "      <login>bill</login>\n" +
      "      <email nil=\"true\" />\n" +
      "      <light type=\"boolean\">false</light>\n" +
      "      <icon_path>/user/icon/2/bill.JPG</icon_path>\n" +
      "    </author>\n" +
      "    <body>the build is BROKEN but i am on it</body>\n" +
      "    <created_at type=\"datetime\">2011-06-18T05:01:42Z</created_at>\n" +
      "    <jabber_user_name nil=\"true\" />\n" +
      "    <is_truncated type=\"boolean\">false</is_truncated>\n" +
      "    <stream type=\"default\"></stream>\n" +
      "  </murmur>\n" +
      "  <murmur>\n" +
      "    <id type=\"integer\">1</id>\n" +
      "    <author url=\"http://localhost:8080/api/v2/users/1.xml\">\n" +
      "      <id type=\"integer\">1</id>\n" +
      "      <name>Osito H. Bonito</name>\n" +
      "      <login>admin</login>\n" +
      "      <email nil=\"true\" />\n" +
      "      <light type=\"boolean\">false</light>\n" +
      "      <icon_path>/user/icon/1/beary.JPG</icon_path>\n" +
      "    </author>\n" +
      "    <body>woof!</body>\n" +
      "    <created_at type=\"datetime\">2011-06-18T04:56:56Z</created_at>\n" +
      "    <jabber_user_name nil=\"true\" />\n" +
      "    <is_truncated type=\"boolean\">false</is_truncated>\n" +
      "    <stream type=\"default\"></stream>\n" +
      "  </murmur>\n" +
      "</murmurs>";

}
