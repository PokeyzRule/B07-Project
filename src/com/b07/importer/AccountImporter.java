package com.b07.importer;

import java.sql.SQLException;
import java.util.List;
import com.b07.database.helper.DatabaseDeleteHelper;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.domain.Account;
import com.b07.domain.Item;
import com.b07.exceptions.DataImportException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.ValidationFailedException;

public class AccountImporter implements DbRecordImporter {

  @Override
  public void importRecords(List<Object> data) throws DataImportException {
    System.out.println("AccountImporter starting...");
    int count = 0;
    try {
      List<Item> existingItems = DatabaseSelectHelper.getAllItems();
      for (Object entity : data) {
        if (entity instanceof Account) {
          Account inAcct = (Account) entity;
          count++;
          boolean acctExists = DatabaseSelectHelper.accountExists(inAcct.getId());
          int acctIdToUse = -1;
          if (!acctExists) {
            acctIdToUse = DatabaseInsertHelper.insertAccount(inAcct.getUserId());
          } else {
            acctIdToUse = inAcct.getId();
            DatabaseDeleteHelper.deleteAccountSummaries(acctIdToUse); // no update of Acct Summary
          }
          for (Item inItem : inAcct.getItemMap().keySet()) {
            Item existItem = DatabaseSelectHelper.lookupItemByName(existingItems, inItem.getName());
            int itemId = -1;
            if (existItem == null) { // should not happen
              itemId = DatabaseInsertHelper.insertItem(inItem.getName(), inItem.getPrice());
            } else {
              itemId = existItem.getId();
            }
            DatabaseInsertHelper.insertAccountLine(acctIdToUse, itemId, inAcct.getItemMap().get(inItem));
          }
        }
      }
    } catch (ValidationFailedException | DatabaseInsertException | SQLException e) {
      throw new DataImportException("Cannot import Account: " + e.getMessage());
    }
    System.out.println("AccountImporter finished import with " + count + " records.");
  }
}
