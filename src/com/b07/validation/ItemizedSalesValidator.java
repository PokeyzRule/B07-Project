package com.b07.validation;

import java.sql.SQLException;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.domain.Sale;
import com.b07.exceptions.ValidationFailedException;

public class ItemizedSalesValidator implements Validator {
  private int salesId;
  private int itemId;
  private int quantity;

  /**
   * Initializes salesId, itemId, and quantity for ItemizedSalesValidator.
   * 
   * @param salesId
   * @param itemId
   * @param quantity
   */
  public ItemizedSalesValidator(int salesId, int itemId, int quantity) {
    this.salesId = salesId;
    this.itemId = itemId;
    this.quantity = quantity;
  }

  /**
   * Validates salesId and itemId are from database, and quantity.
   * 
   * @throws ValidationFailedException
   */
  @Override
  public void validate() throws ValidationFailedException {
    StringBuilder sb = new StringBuilder();
    try {
      Sale sale = DatabaseSelectHelper.getSaleById(salesId);
      if (sale == null) {
        sb.append("Sales id, " + salesId + " not found. ");
      }
      if (quantity < 1) {
        sb.append("Quantity must be > 0. ");
      }
      Validator itemValidator = new ItemExistenceValidator(itemId);
      itemValidator.validate();
    } catch (SQLException e) {
      e.printStackTrace();
      sb.append(e.getMessage());
    } catch (ValidationFailedException e) {
      sb.append(e.getMessage());
    }
    if (sb.length() > 0) {
      throw new ValidationFailedException(sb.toString());
    }

  }

}
