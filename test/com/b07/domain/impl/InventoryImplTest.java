package com.b07.domain.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import com.b07.domain.Inventory;
import com.b07.domain.Item;

public class InventoryImplTest {

  Inventory inventory;
  Item item;
  Item item2;

  @Before
  public void setUp() throws Exception {
    inventory = new InventoryImpl();
  }

  @Test
  public void testUpdateMap_newItem() {
    Map<Item, Integer> itemMap = new HashMap<>();
    item = new ItemImpl("testItem");
    item.setId(1);
    itemMap.put(item, 1);
    inventory.setItemMap(itemMap);

    item2 = new ItemImpl("testItem2");
    item2.setId(2);
    inventory.updateMap(item2, 2);
    Map<Item, Integer> resultMap = inventory.getItemMap();

    assertNotNull(resultMap.get(item2));
  }

  @Test
  public void testUpdateMap_existingItem() {
    Map<Item, Integer> itemMap = new HashMap<>();
    item = new ItemImpl("testItem");
    item.setId(1);
    itemMap.put(item, 1);
    inventory.setItemMap(itemMap);

    inventory.updateMap(item, 99);
    Map<Item, Integer> resultMap = inventory.getItemMap();
    assertTrue(99 == resultMap.get(item));
  }

  @Test
  public void testUpdateTotalItems_emptyMap() {
    inventory.setTotalItems(-1);

    Map<Item, Integer> itemMap = new HashMap<>();
    // Calls private method updateTotalItems() in setItemMap().
    inventory.setItemMap(itemMap);

    assertTrue(0 == inventory.getTotalItems());
  }

  @Test
  public void testUpdateTotalItems_nonEmptyMap() {
    inventory.setTotalItems(-1);

    Map<Item, Integer> itemMap = new HashMap<>();
    item = new ItemImpl("testItem");
    item.setId(1);
    item2 = new ItemImpl("testItem2");
    item2.setId(2);
    itemMap.put(item, 1);
    itemMap.put(item2, 2);
    // Calls private method updateTotalItems() in setItemMap().
    inventory.setItemMap(itemMap);

    assertTrue(3 == inventory.getTotalItems());
  }

}
