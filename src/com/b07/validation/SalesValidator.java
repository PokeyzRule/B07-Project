package com.b07.validation;

import java.math.BigDecimal;
import com.b07.exceptions.ValidationFailedException;

public class SalesValidator implements Validator {
  private int userId;
  private BigDecimal totalPrice;

  /**
   * Initializes userId and totalPrice for SalesValidator.
   * 
   * @param userId
   * @param totalPrice
   */
  public SalesValidator(int userId, BigDecimal totalPrice) {
    this.userId = userId;
    this.totalPrice = totalPrice;
  }

  /**
   * Validates totalPrice is in valid format.
   * 
   * @throws ValidationFailedException
   */
  @Override
  public void validate() throws ValidationFailedException {
    StringBuilder sb = new StringBuilder();
    try {
      if (totalPrice == null || totalPrice.compareTo(new BigDecimal("0")) <= 0) {
        sb.append("totalPrice cannot be null and must be > 0 ");
      }
      Validator validator = new UserExistenceValidator(userId);
      validator.validate();

    } catch (ValidationFailedException e) {
      sb.append(e.getMessage());
    }
    if (sb.length() > 0) {
      throw new ValidationFailedException(sb.toString());
    }

  }

}
