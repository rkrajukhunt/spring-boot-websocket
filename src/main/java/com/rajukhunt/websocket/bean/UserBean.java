package com.rajukhunt.websocket.bean;

import java.io.Serializable;

public class UserBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String email;
  private String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
