package com.b07.validation;

import java.sql.SQLException;
import java.util.Map;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.domain.Item;
import com.b07.exceptions.ValidationFailedException;

public class AccountExistenceValidator implements Validator {
  private int accountId;

  /**
   * Initializes userId for UserExistenceValidator.
   * 
   * @param accountId
   */
  public AccountExistenceValidator(int accountId) {
    this.accountId = accountId;
  }

  /**
   * Validates userId is from database.
   * 
   * @throws ValidationFailedException
   */
  @Override
  public void validate() throws ValidationFailedException {
    try {
      boolean exists = DatabaseSelectHelper.accountExists(accountId);
      if (!exists) {
        throw new ValidationFailedException("Account id, " + accountId + " not found.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new ValidationFailedException(e.getMessage());
    }
  }

}
