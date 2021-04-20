package com.b07.validation;

import org.junit.Rule;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.b07.exceptions.ValidationFailedException;

public class AddressValidatorTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();
  
  @Test
  public void testValidator_null() throws ValidationFailedException {
    AddressValidator aValid = new AddressValidator("");
    exceptionRule.none();
    aValid.validate();
  }
  
  @Test
  public void testValidator_tooLong() throws ValidationFailedException {
    AddressValidator aValid = new AddressValidator("He says Vivaldi trained as a priest"
        + " but spent much of his career focused on music as a performer, composer");
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("Address length exceeds 100.");
    aValid.validate();
  }
  
  @SuppressWarnings("static-access")
  @Test
  public void testValidator_alphNumValid() throws ValidationFailedException {
    AddressValidator aValid = new AddressValidator("a1b2c3");
    exceptionRule.none();
    aValid.validate();
  }

}
