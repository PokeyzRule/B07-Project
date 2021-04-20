package com.b07.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import com.b07.users.User;

public interface Sale extends PrimaryKey {

  /**
   * Returns the user associated with the sale.
   * 
   * @return the user associated with the sale.
   */
  public User getUser();

  /**
   * Sets the user to be associated with the sale.
   * 
   * @param user the user associated with this sale.
   */
  public void setUser(User user);

  /**
   * Returns the total price of the sale.
   * 
   * @return the total price of the sale.
   */
  public BigDecimal getTotalPrice();

  /**
   * Sets the total of the sale.
   * 
   * @param price the total price of the sale.
   */
  public void setTotalPrice(BigDecimal price);

  /**
   * Returns the map of what items and the quantity of items that were sold in the sale.
   * 
   * @return the map of what items and the quantity of items that were sold in the sale.
   */
  public HashMap<Item, Integer> getItemMap();

  /**
   * Sets the map of what items and the quantity of items that were sold in the sale.
   * 
   * @param map the map of what items and the quantity of items that were sold in the sale.
   */
  public void setItemMap(HashMap<Item, Integer> map);

}
