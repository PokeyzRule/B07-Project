package com.b07.importer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.domain.Inventory;
import com.b07.domain.Item;
import com.b07.exceptions.DataImportException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.ValidationFailedException;

public class InventoryImporter implements DbRecordImporter {

  @Override
  public void importRecords(List<Object> data) throws DataImportException {
    System.out.println("InventoryImporter starting...");
    int count = 0;
    try {
      List<Item> existingItems = DatabaseSelectHelper.getAllItems();
      for (Object entity : data) {
        if (entity instanceof Inventory) {
          Inventory inInventory = (Inventory) entity;
          count++;
          Inventory existInv = DatabaseSelectHelper.getInventory();
          Map<Item, Integer> existingItemMap = inInventory.getItemMap();
          if (existingItemMap == null) {
            inInventory.setItemMap(new HashMap<Item, Integer>());
            existingItemMap = new HashMap<Item, Integer>();
          }
          if (existInv == null) {
            for (Item inItem : inInventory.getItemMap().keySet()) {
              Item itemDefinition = DatabaseSelectHelper
                  .lookupItemByName(existingItems, inItem.getName());
              int itemId = -1;
              if (itemDefinition == null) { // should not happen
                itemId = DatabaseInsertHelper.insertItem(inItem.getName(), inItem.getPrice());
              } else {
                itemId = itemDefinition.getId();
              }
              DatabaseInsertHelper.insertInventory(itemId, inInventory.getItemMap().get(inItem));
            }
          } else {
            List<Item> existInvItems = new ArrayList<>(existingItemMap.keySet());
            for (Item inItem : inInventory.getItemMap().keySet()) {
              Item existItem = DatabaseSelectHelper
                  .lookupItemByName(existInvItems, inItem.getName());
              int itemId = -1;
              if (existItem == null) {
                Item itemDefinition = DatabaseSelectHelper
                    .lookupItemByName(existingItems, inItem.getName());
                itemId = itemDefinition.getId();
              } else {
                itemId = existItem.getId();
              }
              DatabaseUpdateHelper.updateInventoryQuantity(inInventory.getItemMap().get(inItem), itemId);
            }
          }
        }
      }
    } catch (ValidationFailedException | DatabaseInsertException | SQLException | NullPointerException e) {
      e.printStackTrace();
      throw new DataImportException("Cannot import Inventory: " + e.getMessage());
    }
    System.out.println("InventoryImporter finished import with " + count + " records.");
  }
}
