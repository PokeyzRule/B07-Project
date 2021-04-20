package com.b07.validation;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.b07.exceptions.ValidationFailedException;

public class ItemizedSalesValidatorTest {

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();
  
  @Test
  public void testValidator_nullSaleValidQuantity() throws ValidationFailedException {
    ItemizedSalesValidator iValid = new ItemizedSalesValidator(6, 1, 1);
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("Sales id, 6 not found.");
    iValid.validate();
  }
  
  @Test
  public void testValidator_invalidSaleQuanitity() throws ValidationFailedException {
    ItemizedSalesValidator iValid = new ItemizedSalesValidator(6, 1, -1);
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("Sales id, 6 not found. Quantity must be > 0. ");
    iValid.validate();
  }
  
  @Test
  public void testValidator_validSaleInvalidQuanitity() throws ValidationFailedException {
    ItemizedSalesValidator iValid = new ItemizedSalesValidator(1, 1, -1);
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("Quantity must be > 0. ");
    iValid.validate();
  }
  
  @Test
  public void testValidator_validSaleQuanitity() throws ValidationFailedException {
    ItemizedSalesValidator iValid = new ItemizedSalesValidator(1, 1, 1);
    exceptionRule.expect(ValidationFailedException.class);
    ExpectedException.none();
    iValid.validate();
  }
  
}
