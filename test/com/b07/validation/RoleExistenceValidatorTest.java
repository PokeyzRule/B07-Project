package com.b07.validation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.b07.exceptions.ValidationFailedException;

public class RoleExistenceValidatorTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();
  
  @Test
  public void testValidator_invalidItem() throws ValidationFailedException {
    RoleExistenceValidator valid = new RoleExistenceValidator(6);
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("ResultSet closed");
    valid.validate();
  }

  @Test
  public void testValidator_validItem() throws ValidationFailedException {
    RoleExistenceValidator valid = new RoleExistenceValidator(1);
    ExpectedException.none();
    valid.validate();
  }
}
