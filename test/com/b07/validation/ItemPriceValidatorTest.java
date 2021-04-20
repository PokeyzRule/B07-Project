package com.b07.validation;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.b07.exceptions.ValidationFailedException;

public class ItemPriceValidatorTest {

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();
  
  @Test
  public void testValidator_null() throws ValidationFailedException {
    ItemPriceValidator valid = new ItemPriceValidator(null);
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("Price cannot be null and must be > 0");
    valid.validate();
  }
  
  @Test
  public void testValidator_negative() throws ValidationFailedException {
    ItemPriceValidator valid = new ItemPriceValidator(BigDecimal.TEN.negate());
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("Price cannot be null and must be > 0");
    valid.validate();
  }
  
  @Test
  public void testValidator_positive() throws ValidationFailedException {
    ItemPriceValidator valid = new ItemPriceValidator(BigDecimal.TEN);
    ExpectedException.none();
    valid.validate();
  }
}
