package com.thoughtworks.mingle.murmurs;

import java.util.Date;

public class Author {

  private String url;
  private int id;
  private String name;
  private String login;
  private String email;
  private boolean light;
  private String icon_path;
  private String icon_url;
  private Date last_login_at;
  private boolean activated;
  private boolean admin;
  private String version_control_user_name;
  private String jabber_user_name;

  public String getName() {
    return name;
  }
  
  public String getIconUrl() {
	  return icon_url;	  	
  }

  public String getIconPath() {
    return icon_path;
  }

  public String getIconPathUri() {
    return IconCache.getRemoteIconPath(name);
  }

}
