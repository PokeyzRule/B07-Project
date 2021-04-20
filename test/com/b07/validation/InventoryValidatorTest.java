package com.b07.validation;

import org.junit.Rule;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.b07.exceptions.ValidationFailedException;

public class InventoryValidatorTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();
  
  @Test
  public void testValidator_negQuantity() throws ValidationFailedException {
    InventoryValidator iValid = new InventoryValidator(1, -5);
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("Inventory quantity must be >= 0.");
    iValid.validate();
  }
  
  @Test
  public void testValidator_posQuantityInvalidItem() 
      throws ValidationFailedException {
    InventoryValidator iValid = new InventoryValidator(6, 5);
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("Item id, 6 not found.");
    iValid.validate();
  }
  
  @Test
  public void testValidator_zeroQuantity() throws ValidationFailedException {
    InventoryValidator iValid = new InventoryValidator(1, 0);
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("Inventory quantity must be >= 0.");
    iValid.validate();
  }
  
  @Test
  public void testValidator_bothValid() throws ValidationFailedException {
    InventoryValidator iValid = new InventoryValidator(1, 5);
    ExpectedException.none();
    iValid.validate();
  }
 
  @Test
  public void testValidator_invalidItem() throws ValidationFailedException {
    InventoryValidator iValid = new InventoryValidator(6, 5);
    exceptionRule.expect(ValidationFailedException.class);
    exceptionRule.expectMessage("Item id, 6 not found.");
    iValid.validate();
  }

  @Test
  public void testValidator_validItem() throws ValidationFailedException {
    InventoryValidator iValid = new InventoryValidator(1, 5);
    ExpectedException.none();
    iValid.validate();
  }
}
