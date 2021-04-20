package com.b07.validation;

import com.b07.exceptions.ValidationFailedException;

public class InventoryValidator implements Validator {
  private int itemId;
  private int quantity;

  /**
   * Initializes itemId and quantity for InventoryValidator.
   * 
   * @param itemId
   * @param quantity
   */
  public InventoryValidator(int itemId, int quantity) {
    this.itemId = itemId;
    this.quantity = quantity;
  }

  /**
   * Validates inventory fields as specified in handout.
   * 
   * @throws ValidationFailedException
   */
  @Override
  public void validate() throws ValidationFailedException {
    StringBuilder sb = new StringBuilder();
    try {
      if (quantity < 0) {
        throw new ValidationFailedException("Inventory quantity must be >= 0.");
      }
      Validator itemValidator = new ItemExistenceValidator(itemId);
      itemValidator.validate();
    } catch (ValidationFailedException e) {
      sb.append(e.getMessage());
    }
    if (sb.length() > 0) {
      throw new ValidationFailedException(sb.toString());
    }
  }

}
