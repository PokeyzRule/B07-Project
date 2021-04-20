package com.b07.validation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.b07.exceptions.ValidationFailedException;

public class RoleNameValidatorTest {

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();
  
  @Test
  public void testValidator_validRole() throws ValidationFailedException {
    RoleNameValidator valid = new RoleNameValidator("ADMIN");
    ExpectedException.none();
    valid.validate();
  }

  @Test
  public void testValidator_invalidRole() throws ValidationFailedException {
    RoleNameValidator valid = new RoleNameValidator("nifnidn");
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("nifnidn is not valid role name.");
    valid.validate();
  }
  
  @Test
  public void testValidator_emptyRole() throws ValidationFailedException {
    RoleNameValidator valid = new RoleNameValidator("");
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage(" is not valid role name.");
    valid.validate();
  }
}
