package com.b07.validation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.b07.exceptions.ValidationFailedException;

public class ItemNameValidatorTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();
  
  @Test
  public void testValidator_null() throws ValidationFailedException {
    ItemNameValidator valid = new ItemNameValidator(null);
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("Item name cannot be null and length "
        + "must be less than 64 characters.");
    valid.validate();
  }
  
  @Test
  public void testValidator_tooLong() throws ValidationFailedException {
    ItemNameValidator valid = new ItemNameValidator("He says Vivaldi trained as a priest"
        + " but spent much of his career focused on music as a performer, composer");
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("Item name cannot be null and length "
        + "must be less than 64 characters.");
    valid.validate();
  }
  
}
