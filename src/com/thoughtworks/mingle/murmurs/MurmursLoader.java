package com.thoughtworks.mingle.murmurs;

import java.io.InputStream;
import java.util.List;

import com.thoughtworks.mingle.murmurs.Murmur.Stream.Origin;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class MurmursLoader {

  private static final DateConverter DATE_CONVERTER = new DateConverter("yyyy-MM-dd'T'HH:mm:ss'Z'",
      new String[] { "yyyy-MM-dd'T'HH:mm:ssZ" });
  
  private void addCommonAliases(XStream xstream) {
    xstream.alias("murmur", Murmur.class);
    xstream.alias("author", Author.class);
    xstream.alias("stream", Murmur.Stream.class);
    xstream.alias("origin", Origin.class);
    xstream.registerLocalConverter(Murmur.class, "created_at",
        MurmursLoader.DATE_CONVERTER);
    xstream.registerLocalConverter(Author.class, "last_login_at",
        MurmursLoader.DATE_CONVERTER);
  }

  public Murmur loadOneFromXml(InputStream inputStream) {
    XStream xstream = new XStream(new DomDriver());
    addCommonAliases(xstream);
    return (Murmur) xstream.fromXML(inputStream);
  }
  
  public List<Murmur> loadMultipleFromXml(InputStream inputStream) {
    XStream xstream = new XStream(new DomDriver());
    xstream.addImplicitCollection(Murmur.Murmurs.class, "murmurs", Murmur.class);
    xstream.alias("murmurs", Murmur.Murmurs.class);
    addCommonAliases(xstream);
    Murmur.Murmurs murmurs = (Murmur.Murmurs) xstream.fromXML(inputStream);
    for (Murmur m : murmurs.getMurmurs()) {
      Murmur.cache(m);
    }
    return murmurs.getMurmurs();
  }

}
