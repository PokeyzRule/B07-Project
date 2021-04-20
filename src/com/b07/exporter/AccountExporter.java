package com.b07.exporter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.domain.Account;
import com.b07.domain.Item;
import com.b07.exceptions.DataExportException;
import com.b07.users.User;

public class AccountExporter implements DbRecordExporter<Account> {

  @Override
  public List<Account> exportRecords() throws DataExportException {
    List<Account> accounts = new ArrayList<>();
    try {
      for (User user : DatabaseSelectHelper.getUsersDetails()) {
        Account account = new Account();
        account.setUserId(user.getId());
        boolean hasAcount = false;
        for (Integer acctId : DatabaseSelectHelper.getActiveAccounts(user.getId())) {
          Map<Item, Integer> acctSummary = DatabaseSelectHelper.getAccountDetails(acctId);
          account.setActive(true);
          account.setItemMap(acctSummary);
          hasAcount = true;
        }
        for (Integer acctId : DatabaseSelectHelper.getInactiveAccounts(user.getId())) {
          Map<Item, Integer> acctSummary = DatabaseSelectHelper.getAccountDetails(acctId);
          account.setActive(false);
          account.setItemMap(acctSummary);
          hasAcount = true;
        }
        if (hasAcount) {
          accounts.add(account);
        }
      }
    } catch (SQLException e) {
      throw new DataExportException("Accounts could not be exported.");
    }
    System.out.println("AccountExporter exported: " + accounts.size());
    return accounts;
  }
}
