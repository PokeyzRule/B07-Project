package com.b07.validation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.b07.exceptions.ValidationFailedException;

public class ItemExistenceValidatorTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();
  
  @Test
  public void testValidator_nullItem() throws ValidationFailedException {
    ItemExistenceValidator iValid = new ItemExistenceValidator(6);
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("Item id, 6 not found.");
    iValid.validate();
  }
  
  @Test
  public void testValidator_validItem() throws ValidationFailedException {
    ItemExistenceValidator iValid = new ItemExistenceValidator(3);
    ExpectedException.none();
    iValid.validate();
  }

}
