package com.b07.domain.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.b07.domain.Inventory;
import com.b07.domain.Item;

public class InventoryImpl implements Inventory, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5044295221452732865L;
  
  private transient Map<Item, Integer> itemMap = new HashMap<>();
  private transient int totalItems;

  public Map<Item, Integer> getItemMap() {
    return itemMap;
  }

  public void setItemMap(Map<Item, Integer> itemMap) {
    this.itemMap = itemMap;
    updateTotalItems();
  }

  public int getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(int totalItems) {
    this.totalItems = totalItems;
  }

  @Override
  public void updateMap(Item item, Integer quantity) {
    itemMap.put(item, quantity);
    totalItems = totalItems + quantity;
  }

  private void updateTotalItems() {
    totalItems = 0;
    for (Item item : itemMap.keySet()) {
      totalItems = totalItems + itemMap.get(item);
    }
  }

}
