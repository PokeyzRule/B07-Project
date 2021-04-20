package com.b07.database.helper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import com.b07.database.DatabaseUpdater;
import com.b07.domain.Item;
import com.b07.exceptions.ValidationFailedException;
import com.b07.validation.AccountExistenceValidator;
import com.b07.validation.AddressValidator;
import com.b07.validation.InventoryValidator;
import com.b07.validation.ItemExistenceValidator;
import com.b07.validation.ItemNameValidator;
import com.b07.validation.ItemPriceValidator;
import com.b07.validation.RoleExistenceValidator;
import com.b07.validation.RoleNameValidator;
import com.b07.validation.UserExistenceValidator;
import com.b07.validation.Validator;

public class DatabaseUpdateHelper extends DatabaseUpdater {

  /**
   * Updates role name in database associated with id.
   * 
   * @param name
   * @param id
   * @return complete
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static boolean updateRoleName(String name, int id) throws SQLException, 
    ValidationFailedException {
    Validator roleValidator = new RoleExistenceValidator(id);
    roleValidator.validate();
    Validator nameValidator = new RoleNameValidator(name);
    nameValidator.validate();

    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      boolean complete = DatabaseUpdater.updateRoleName(name, id, connection);
      return complete;
    }
  }

  /**
   * Updates name in database associated with user id.
   * 
   * @param name
   * @param userId
   * @return complete
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static boolean updateUserName(String name, int userId) throws SQLException, 
    ValidationFailedException {
    Validator validator = new UserExistenceValidator(userId);
    validator.validate();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      boolean complete = DatabaseUpdater.updateUserName(name, userId, connection);
      return complete;
    }
  }

  /**
   * Updates age in database associated with user id.
   * 
   * @param age
   * @param userId
   * @return complete
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static boolean updateUserAge(int age, int userId) throws SQLException, 
    ValidationFailedException {
    Validator validator = new UserExistenceValidator(userId);
    validator.validate();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      boolean complete = DatabaseUpdater.updateUserAge(age, userId, connection);
      return complete;
    }
  }

  /**
   * Updates address in database associated with user id.
   * 
   * @param address
   * @param userId
   * @return complete
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static boolean updateUserAddress(String address, int userId) 
      throws SQLException, ValidationFailedException {
    Validator userValidator = new UserExistenceValidator(userId);
    userValidator.validate();
    Validator addressValidator = new AddressValidator(address);
    addressValidator.validate();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      boolean complete = DatabaseUpdater.updateUserAddress(address, userId, connection);
      return complete;
    }
  }

  /**
   * Updates role id in database associated with user id.
   * 
   * @param roleId
   * @param userId
   * @return complete
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static boolean updateUserRole(int roleId, int userId) 
      throws SQLException, ValidationFailedException {
    Validator userValidator = new UserExistenceValidator(userId);
    userValidator.validate();
    Validator roleValidator = new RoleExistenceValidator(roleId);
    roleValidator.validate();

    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      boolean complete = DatabaseUpdater.updateUserRole(roleId, userId, connection);
      return complete;
    }
  }

  /**
   * Updates name in database associated with item id.
   * 
   * @param name
   * @param itemId
   * @return complete
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static boolean updateItemName(String name, int itemId) 
      throws SQLException, ValidationFailedException {
    Validator itemValidator = new ItemExistenceValidator(itemId);
    itemValidator.validate();
    Validator nameValidator = new ItemNameValidator(name);
    nameValidator.validate();

    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      boolean complete = DatabaseUpdater.updateItemName(name, itemId, connection);
      return complete;
    }
  }

  /**
   * Updates price in database associated with item id.
   * 
   * @param price
   * @param itemId
   * @return complete
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static boolean updateItemPrice(BigDecimal price, int itemId) throws SQLException, 
    ValidationFailedException {
    Validator itemValidator = new ItemExistenceValidator(itemId);
    itemValidator.validate();
    Validator priceValidator = new ItemPriceValidator(price);
    priceValidator.validate();

    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      boolean complete = DatabaseUpdater.updateItemPrice(price, itemId, connection);
      return complete;
    }
  }

  /**
   * Updates quantity in database associated with item id.
   * 
   * @param quantity
   * @param itemId
   * @return complete
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static boolean updateInventoryQuantity(int quantity, int itemId)
      throws SQLException, ValidationFailedException {
    Validator validator = new InventoryValidator(itemId, quantity);
    validator.validate();

    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      boolean complete = DatabaseUpdater.updateInventoryQuantity(quantity, itemId, connection);
      return complete;
    }
  }
  
  public static void updateAccountStatus(int accountId) throws SQLException, ValidationFailedException {
    Validator validator = new AccountExistenceValidator(accountId);
    validator.validate();
    Map<Item, Integer> cartMap = DatabaseSelectHelper.getAccountDetails(accountId);
    if (cartMap.isEmpty()) {
      System.out.println("Account summary does not exist.");
      return;
    }
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      DatabaseUpdater.updateAccountStatus(accountId, false, connection);
    }
  }
  
}
