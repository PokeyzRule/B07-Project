package com.b07.importer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.domain.Item;
import com.b07.domain.Sale;
import com.b07.exceptions.DataImportException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.ValidationFailedException;

public class SaleImporter implements DbRecordImporter {

  @Override
  public void importRecords(List<Object> data) throws DataImportException {
    System.out.println("SaleImporter starting...");
    int count = 0;
    try {
      List<Item> existingItems = DatabaseSelectHelper.getAllItems();
      for (Object entity : data) {
        if (entity instanceof Sale) {
          Sale inSale = (Sale) entity;
          count++;
          Sale existSale = DatabaseSelectHelper.getItemizedSaleById(inSale.getId());
          List<Item> itemsToUse = existingItems;
          int saleIdToUse = -1;
          if (existSale == null) {
            saleIdToUse = DatabaseInsertHelper.insertSale(inSale.getId(), inSale.getTotalPrice());

          } else {
            itemsToUse = new ArrayList<>(existSale.getItemMap().keySet());
            saleIdToUse = inSale.getId();
          }
// No upddate of ItemizedSale
          for (Item inItem : inSale.getItemMap().keySet()) {
            Item existItem = DatabaseSelectHelper.lookupItemByName(itemsToUse, inItem.getName());
            int itemId = -1;
            if (existItem == null) {
              Item itemDefinition = DatabaseSelectHelper.lookupItemByName(existingItems, inItem.getName());
              itemId = itemDefinition.getId();
            } else {
              itemId = existItem.getId();
            }
            DatabaseInsertHelper.insertItemizedSale(saleIdToUse, itemId, inSale.getItemMap().get(inItem));
          }
        }
      }
    } catch (ValidationFailedException | DatabaseInsertException | SQLException e) {
      throw new DataImportException("Cannot import Sale: " + e.getMessage());
    }
    System.out.println("SaleImporter finished import with " + count + " records.");
  }
}
