package com.rajukhunt.websocket.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class GroupBean implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -115722320456957335L;
  private Long id;
  private String title;
  private Long createdBy;
  private UserBean creator;
  private Long userId;
  private Boolean userExsist;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
  }

  public UserBean getCreator() {
    return creator;
  }

  public void setCreator(UserBean creator) {
    this.creator = creator;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Boolean getUserExsist() {
    return userExsist;
  }

  public void setUserExsist(Boolean userExsist) {
    this.userExsist = userExsist;
  }

}
