package com.b07.validation;


import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.b07.exceptions.ValidationFailedException;

public class SalesValidatorTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();
  
  @Test
  public void testValidator_null() throws ValidationFailedException {
    SalesValidator valid = new SalesValidator(1, null);
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("totalPrice cannot be null and must be > 0 ");
    valid.validate();
  }
  
  @Test
  public void testValidator_negative() throws ValidationFailedException {
    SalesValidator valid = new SalesValidator(1, BigDecimal.TEN.negate());
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("totalPrice cannot be null and must be > 0 ");
    valid.validate();
  }
  
  @Test
  public void testValidator_zero() throws ValidationFailedException {
    SalesValidator valid = new SalesValidator(1, BigDecimal.ZERO);
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("totalPrice cannot be null and must be > 0 ");
    valid.validate();
  }
  
  @Test
  public void testValidator_positive() throws ValidationFailedException {
    SalesValidator valid = new SalesValidator(1, BigDecimal.TEN);
    ExpectedException.none();
    valid.validate();
  }

}
