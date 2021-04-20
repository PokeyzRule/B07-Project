package com.b07.validation;

import java.math.BigDecimal;
import com.b07.exceptions.ValidationFailedException;

public class ItemValidator implements Validator {
  private BigDecimal price;
  private String name;

  /**
   * Initializes with name and price for ItemValidator.
   * 
   * @param name
   * @param price
   */
  public ItemValidator(String name, BigDecimal price) {
    this.name = name;
    this.price = price;
  }

  /**
   * Validates name and price of item.
   * 
   * @throws ValidationFailedException
   */
  @Override
  public void validate() throws ValidationFailedException {
    StringBuilder sb = new StringBuilder();
    try {
      Validator priceValidator = new ItemPriceValidator(price);
      priceValidator.validate();
    } catch (ValidationFailedException e) {
      sb.append(e.getMessage());
    }
    try {
      Validator nameValidator = new ItemNameValidator(name);
      nameValidator.validate();
    } catch (ValidationFailedException e) {
      sb.append(e.getMessage());
    }
    if (sb.length() > 0) {
      throw new ValidationFailedException(sb.toString());

    }
  }

}
