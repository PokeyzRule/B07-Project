package com.b07.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Account extends PrimaryKeyObject implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1380427939354775025L;
  private int userId;
  private transient boolean active;
  private Map<Item, Integer> itemMap = new HashMap<>();

  public Account() {}

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Map<Item, Integer> getItemMap() {
    return itemMap;
  }

  public void setItemMap(Map<Item, Integer> itemMap) {
    this.itemMap = itemMap;
  }

  
  

}
