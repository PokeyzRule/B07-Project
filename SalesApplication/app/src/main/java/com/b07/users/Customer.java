package com.b07.users;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {

  /**
   * 
   */
  private static final long serialVersionUID = 7806427654311147614L;
  
  private transient List<Integer> accounts = new ArrayList<>();
  private transient int currentAcctId;
  
  public Customer() {};
  
  public Customer(int id, String name, int age, String address, boolean authenticated) {
    super(id, name, age, address, authenticated);
  }

  public Customer(int id, String name, int age, String address) {
    super(id, name, age, address);
  }

  public boolean hasAccount() {
    return accounts.size() > 0;
  }

  public List<Integer> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<Integer> accounts) {
    this.accounts = accounts;
  }

  public int getCurrentAcctId() {
    return currentAcctId;
  }

  public void setCurrentAcctId(int currentAcctId) {
    this.currentAcctId = currentAcctId;
  }

  
  
  
  
}
