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
  private Date last_login_at;
  private boolean activated;
  private boolean admin;
  private String version_control_user_name;
  private String jabber_user_name;

  public String getName() {
    return name;
  }

}
