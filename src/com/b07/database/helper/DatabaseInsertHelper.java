package com.b07.database.helper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.b07.database.DatabaseInserter;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.ValidationFailedException;
import com.b07.security.PasswordHelpers;
import com.b07.validation.AccountExistenceValidator;
import com.b07.validation.AddressValidator;
import com.b07.validation.InventoryValidator;
import com.b07.validation.ItemValidator;
import com.b07.validation.ItemizedSalesValidator;
import com.b07.validation.RoleExistenceValidator;
import com.b07.validation.RoleNameValidator;
import com.b07.validation.SalesValidator;
import com.b07.validation.UserExistenceValidator;
import com.b07.validation.Validator;

public class DatabaseInsertHelper extends DatabaseInserter {

  /**
   * Inserts a role name into the database.
   * 
   * @param name
   * @return roleId
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static int insertRole(String name)
      throws DatabaseInsertException, SQLException, ValidationFailedException {
    Validator validator = new RoleNameValidator(name);
    validator.validate();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      int roleId = DatabaseInserter.insertRole(name, connection);
      return roleId;
    }
  }

  /**
   * Inserts a user into the database.
   * 
   * @param name
   * @param age
   * @param address
   * @param password
   * @return userId
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static int insertNewUser(String name, int age, String address, String password)
      throws DatabaseInsertException, SQLException, ValidationFailedException {
    Validator validator = new AddressValidator(address);
    validator.validate();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      int userId = DatabaseInserter.insertNewUser(name, age, address, password, connection);
      return userId;
    }
  }

  /**
   * Inserts a user role into the database.
   * 
   * @param userId
   * @param roleId
   * @return userRoleId
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static int insertUserRole(int userId, int roleId)
      throws DatabaseInsertException, SQLException, ValidationFailedException {
    Validator userValidator = new UserExistenceValidator(userId);
    userValidator.validate();
    Validator roleValidator = new RoleExistenceValidator(roleId);
    roleValidator.validate();

    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      int userRoleId = DatabaseInserter.insertUserRole(userId, roleId, connection);
      return userRoleId;
    }
  }

  /**
   * Inserts an item into the database.
   * 
   * @param name
   * @param price
   * @return item
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static int insertItem(String name, BigDecimal price)
      throws DatabaseInsertException, SQLException, ValidationFailedException {
    Validator validator = new ItemValidator(name, price);
    validator.validate();

    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      int itemId = DatabaseInserter.insertItem(name, price, connection);
      return itemId;
    }
  }

  /**
   * Inserts an inventory into the database.
   * 
   * @param itemId
   * @param quantity
   * @return inventoryId
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static int insertInventory(int itemId, int quantity)
      throws DatabaseInsertException, SQLException, ValidationFailedException {
    Validator validator = new InventoryValidator(itemId, quantity);
    validator.validate();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      int inventoryId = DatabaseInserter.insertInventory(itemId, quantity, connection);
      return inventoryId;
    }
  }

  /**
   * Inserts a sale into the database.
   * 
   * @param userId
   * @param totalPrice
   * @return saleId
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static int insertSale(int userId, BigDecimal totalPrice)
      throws DatabaseInsertException, SQLException, ValidationFailedException {
    Validator validator = new SalesValidator(userId, totalPrice);
    validator.validate();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      int saleId = DatabaseInserter.insertSale(userId, totalPrice, connection);
      return saleId;
    }
  }

  /**
   * Inserts an itemized sale into the database.
   * 
   * @param saleId
   * @param itemId
   * @param quantity
   * @return itemizedId
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static int insertItemizedSale(int saleId, int itemId, int quantity)
      throws DatabaseInsertException, SQLException, ValidationFailedException {
    Validator validator = new ItemizedSalesValidator(saleId, itemId, quantity);
    validator.validate();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      int itemizedId = DatabaseInserter.insertItemizedSale(saleId, itemId, quantity, connection);
      return itemizedId;
    }
  }

  /**
   * Inserts account record with given userId into db.
   * 
   * @param userId
   * @return
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static int insertAccount(int userId)
      throws DatabaseInsertException, SQLException, ValidationFailedException {
    Validator validator = new UserExistenceValidator(userId);
    validator.validate();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      return DatabaseInserter.insertAccount(userId, true, connection);
    }
  }

  /**
   * Inserts account line with given account id, item id and quantity.
   * 
   * @param accountId
   * @param itemId
   * @param quantity
   * @return
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException 
   */
  public static int insertAccountLine(int accountId, int itemId, int quantity)
      throws DatabaseInsertException, SQLException, ValidationFailedException {
    Validator validator = new AccountExistenceValidator(accountId);
    validator.validate();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      int accountLine = DatabaseInserter.insertAccountLine(accountId, itemId, quantity, connection);
      return accountLine;
    }
  }

  /**
   * Inserts a user into the database.
   *
   * @param name
   * @param age
   * @param address
   * @param hashedPassword
   * @return userId
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public static int insertNewUserWithHashedPassword(String name, int age, String address, String hashedPassword)
          throws DatabaseInsertException, SQLException, ValidationFailedException {
      Validator validator = new AddressValidator(address);
      validator.validate();
      int userId = 0;
      try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
        userId = Math.toIntExact(insertUser(name, age, address, connection));
        insertHashedPassword(hashedPassword, userId, connection);
      }
      return userId;
  }

  private static boolean insertHashedPassword(String hashedPassword, int userId, Connection connection) {
    String sql = "INSERT INTO USERPW(USERID, PASSWORD) VALUES(?,?);";
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, userId);
      preparedStatement.setString(2, hashedPassword);
      preparedStatement.executeUpdate();
      preparedStatement.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
  
  private static int insertUser(String name, int age, String address,
      Connection connection) {
  String sql = "INSERT INTO USERS(NAME, AGE, ADDRESS) VALUES(?,?,?);";
  try {
    PreparedStatement preparedStatement = connection.prepareStatement(sql, 
        Statement.RETURN_GENERATED_KEYS);
    preparedStatement.setString(1, name);
    preparedStatement.setInt(2, age);
    preparedStatement.setString(3, address);
    int id = preparedStatement.executeUpdate();
    if (id > 0) {
      ResultSet uniqueKey = preparedStatement.getGeneratedKeys();
      if (uniqueKey.next()) {
        int returnValue = uniqueKey.getInt(1);
        uniqueKey.close();
        preparedStatement.close();
        return returnValue;
      }
    }
  } catch (Exception e) {
    e.printStackTrace();
  }
  return -1;
}
}
