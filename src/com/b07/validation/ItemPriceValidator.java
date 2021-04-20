package com.b07.validation;

import java.math.BigDecimal;
import com.b07.exceptions.ValidationFailedException;

public class ItemPriceValidator implements Validator {
  private BigDecimal price;

  /**
   * Initializes price for ItemPriceValidator.
   * 
   * @param price
   */
  public ItemPriceValidator(BigDecimal price) {
    this.price = price;
  }

  /**
   * Validates price of item.
   * 
   * @throws ValidationFailedException
   */
  @Override
  public void validate() throws ValidationFailedException {
    StringBuilder sb = new StringBuilder();
    if (price == null || price.compareTo(new BigDecimal("0")) < 0) {
      sb.append("Price cannot be null and must be > 0");
    }
    if (sb.length() > 0) {
      throw new ValidationFailedException(sb.toString());

    }
  }

}
