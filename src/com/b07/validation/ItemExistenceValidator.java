package com.b07.validation;

import java.sql.SQLException;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.domain.Item;
import com.b07.exceptions.ValidationFailedException;

public class ItemExistenceValidator implements Validator {
  private int itemId;

  /**
   * Initializes itemId for ItemExistenceValidator.
   * 
   * @param itemId
   */
  public ItemExistenceValidator(int itemId) {
    this.itemId = itemId;
  }

  /**
   * Validates itemId is from database.
   * 
   * @throws ValidationFailedException
   */
  @Override
  public void validate() throws ValidationFailedException {
    try {
      Item item = DatabaseSelectHelper.getItem(itemId);
      if (item == null) {
        throw new ValidationFailedException("Item id, " + itemId + " not found. ");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new ValidationFailedException(e.getMessage());
    }

  }

}
