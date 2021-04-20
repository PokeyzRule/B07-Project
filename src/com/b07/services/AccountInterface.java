package com.b07.services;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import com.b07.database.helper.DatabaseDeleteHelper;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.domain.Item;
import com.b07.exceptions.AccountCreateFailedException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.ValidationFailedException;
import com.b07.users.Customer;

public class AccountInterface {

  private Customer customer;

  public AccountInterface(Customer customer) {
    this.customer = customer;
  }

  public int createAccount() throws AccountCreateFailedException {
    try {
      return DatabaseInsertHelper.insertAccount(customer.getId());
    } catch (DatabaseInsertException | SQLException | ValidationFailedException e) {
      throw new AccountCreateFailedException(
          "The requested account could not be created: " + e.getMessage());
    }
  }

  public Map<Item, Integer> getAccountSummary() throws SQLException {
    return DatabaseSelectHelper.getAccountDetails(customer.getCurrentAcctId());
  }

  public static List<Integer> getAccounts(int userId) throws SQLException {
    return DatabaseSelectHelper.getActiveAccounts(userId);
  }

  public void updateAccountSummary(Map<Item, Integer> cartItemMap)
      throws DatabaseInsertException, SQLException, ValidationFailedException {
    int acctId = customer.getCurrentAcctId();
    clearAccountSummary(acctId);
    for (Item item : cartItemMap.keySet()) {
      DatabaseInsertHelper.insertAccountLine(acctId, item.getId(), cartItemMap.get(item));
    }
  }

  private void clearAccountSummary(int accountId) throws SQLException, DatabaseInsertException, ValidationFailedException {
    //check if the account line exists - if it does exist then update the row
    Map<Item, Integer> existingCartMap = DatabaseSelectHelper.getAccountDetails(accountId);
    if (!existingCartMap.isEmpty()) {
      DatabaseDeleteHelper.deleteAccountSummaries(accountId);
    }
  }

  public void deactivateAccount(int accountId) {
    try {
      DatabaseUpdateHelper.updateAccountStatus(accountId);
    } catch (SQLException | ValidationFailedException e) {
      System.out.println("Could not update Account: " + e.getMessage());
    }
  }
  
  public boolean accountExists(int accountId) throws SQLException {
    return DatabaseSelectHelper.accountExists(accountId);
  }
}
