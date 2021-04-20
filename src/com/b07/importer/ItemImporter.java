package com.b07.importer;

import java.sql.SQLException;
import java.util.List;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.domain.Item;
import com.b07.exceptions.DataImportException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.ValidationFailedException;

public class ItemImporter implements DbRecordImporter {

  @Override
  public void importRecords(List<Object> data) throws DataImportException {
    System.out.println("ItemImporter starting...");
    int count = 0;
    try {
      List<Item> existingItems = DatabaseSelectHelper.getAllItems();
      for (Object entity : data) {
        if (entity instanceof Item) {
          Item inItem = (Item) entity;
          count++;
          Item existItem = DatabaseSelectHelper.lookupItemByName(existingItems, inItem.getName());
          if (existItem == null) {
            DatabaseInsertHelper.insertItem(inItem.getName(), inItem.getPrice());
          } else if (!inItem.getPrice().equals(existItem.getPrice())) {
            DatabaseUpdateHelper.updateItemPrice(inItem.getPrice(), existItem.getId());
          }
        }
      }
    } catch (ValidationFailedException | DatabaseInsertException | SQLException e) {
      throw new DataImportException("Cannot import Item: " + e.getMessage());
    }
    System.out.println("ItemImporter finished import with " + count + " records.");
  }

}
