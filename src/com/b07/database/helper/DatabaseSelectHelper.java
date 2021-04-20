package com.b07.database.helper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.b07.database.DatabaseSelector;
import com.b07.domain.Inventory;
import com.b07.domain.Item;
import com.b07.domain.Sale;
import com.b07.domain.SalesLog;
import com.b07.domain.impl.InventoryImpl;
import com.b07.domain.impl.ItemImpl;
import com.b07.domain.impl.SaleImpl;
import com.b07.domain.impl.SalesLogImpl;
import com.b07.exceptions.ValidationFailedException;
import com.b07.users.User;
import com.b07.users.UserFactory;
import com.b07.validation.ItemExistenceValidator;
import com.b07.validation.Validator;

public class DatabaseSelectHelper extends DatabaseSelector {

  /**
   * Gets all role ids.
   * 
   * @return ids
   * @throws SQLException
   */
  public static List<Integer> getRoleIds() throws SQLException {
    List<Integer> ids = new ArrayList<>();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getRoles(connection);) {
      while (results.next()) {
        ids.add(results.getInt("ID"));
      }
    }
    return ids;
  }

  /**
   * Gets role id associated with name.
   * 
   * @param name
   * @return results.getInt("ID")
   * @throws SQLException
   */
  public static Integer getRoleIdByName(String name) throws SQLException {
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      ResultSet results = DatabaseSelector.getRoles(connection);
      while (results.next()) {
        String roleName = results.getString("NAME");
        if (name.equalsIgnoreCase(roleName)) {
          return results.getInt("ID");
        }
      }
    }
    return -1;
  }

  /**
   * Gets role name associated with role id.
   * 
   * @param roleId
   * @return role
   * @throws SQLException
   */
  public static String getRoleName(int roleId) throws SQLException {
    String role = null;
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      role = DatabaseSelector.getRole(roleId, connection);
      connection.close();
    }
    return role;
  }

  public static int getUserRoleId(int userId) throws SQLException {
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      int roleId = DatabaseSelector.getUserRole(userId, connection);
      return roleId;
    }
  }

  /**
   * Gets list of user ids associated with role id.
   * 
   * @param roleId
   * @return userIds
   * @throws SQLException
   */
  public static List<Integer> getUsersByRole(int roleId) throws SQLException {
    List<Integer> userIds = new ArrayList<>();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getUsersByRole(roleId, connection)) {
      while (results.next()) {
        userIds.add(results.getInt("USERID"));
      }
    }
    return userIds;
  }

  /**
   * Gets users in database.
   * 
   * @return users
   * @throws SQLException
   */
  public static List<User> getUsersDetails() throws SQLException {
    List<User> users = new ArrayList<>();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getUsersDetails(connection)) {

      while (results.next()) {
        User user = mapResultSetToUser(results);
        users.add(user);
      }
    }
    return users;
  }

  /**
   * Gets user associated with Result Set.
   * 
   * @param rs
   * @return user
   * @throws SQLException
   */
  private static User mapResultSetToUser(ResultSet rs) throws SQLException {
    int id = rs.getInt("ID");
    int roleId = getUserRoleId(id);
    String roleName = getRoleName(roleId);
    User user = UserFactory.buildUser(roleName);
    user.setAddress(rs.getString("ADDRESS"));
    user.setAge(rs.getInt("AGE"));
    user.setId(id);
    user.setAuthenticated(false);
    user.setName(rs.getString("NAME"));
    return user;
  }

  /**
   * Gets user associated with user id.
   * 
   * @param userId
   * @return mapResultSetToUser(results)
   * @throws SQLException
   */
  public static User getUserDetails(int userId) throws SQLException {
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getUserDetails(userId, connection)) {
      while (results.next()) {
        return mapResultSetToUser(results);
      }
    }
    return null;
  }

  /**
   * Gets user name associated with user id.
   * 
   * @param userId
   * @return results.getString("NAME")
   * @throws SQLException
   */
  public static String getUserName(int userId) throws SQLException {

    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getUserDetails(userId, connection)) {
      while (results.next()) {
        return results.getString("NAME");
      }
    }
    return null;
  }

  /**
   * Gets password associated with user id.
   * 
   * @param userId
   * @return password
   * @throws SQLException
   */
  public static String getPassword(int userId) throws SQLException {
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      String password = DatabaseSelector.getPassword(userId, connection);
      return password;
    }
  }

  /**
   * Gets all items.
   * 
   * @return iems
   * @throws SQLException
   */
  public static List<Item> getAllItems() throws SQLException {

    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getAllItems(connection);) {
      List<Item> items = new ArrayList<>();
      while (results.next()) {
        Item item = new ItemImpl();
        mapResultSetToItem(results, item);
        items.add(item);
      }
      return items;
    }
  }

  /**
   * Sets item details associated with result set.
   * 
   * @param results
   * @param item
   * @throws SQLException
   */
  private static void mapResultSetToItem(ResultSet results, Item item) throws SQLException {

    item.setId(results.getInt("ID"));
    item.setName(results.getString("NAME"));
    item.setPrice(new BigDecimal(results.getString("PRICE")));
  }

  /**
   * Gets item associated with item id.
   * 
   * @param itemId
   * @return item
   * @throws SQLException
   */
  public static Item getItem(int itemId) throws SQLException {
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getItem(itemId, connection);) {
      while (results.next()) {
        Item item = new ItemImpl();
        mapResultSetToItem(results, item);
        return item;
      }
    }
    return null;
  }

  /**
   * Gets inventory.
   * 
   * @return inventory
   * @throws SQLException
   */
  public static Inventory getInventory() throws SQLException {
    Inventory inventory = new InventoryImpl();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getInventory(connection);) {
      while (results.next()) {
        Item item = getItem(results.getInt("ITEMID"));
        int quantity = results.getInt("QUANTITY");
        inventory.updateMap(item, quantity);
      }
    }
    return inventory;
  }

  /**
   * Gets inventory quantity associated with item id.
   * 
   * @param itemId
   * @return quantity
   * @throws SQLException
   */
  public static int getInventoryQuantity(int itemId) 
      throws SQLException, ValidationFailedException {
    Validator validator = new ItemExistenceValidator(itemId);
    validator.validate();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      int quantity = DatabaseSelector.getInventoryQuantity(itemId, connection);
      return quantity;
    }
  }

  /**
   * Gets log of sales.
   * 
   * @return salesLog
   * @throws SQLException
   */
  public static SalesLog getSales() throws SQLException {
    SalesLog salesLog = new SalesLogImpl();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getSales(connection);) {
      while (results.next()) {
        Sale sale = new SaleImpl();
        mapResultSetToSale(results, sale);
        salesLog.addSale(sale);
      }
      return salesLog;
    }
  }

  /**
   * Gets sale associated with sale id.
   * 
   * @param saleId
   * @return sale
   * @throws SQLException
   */
  public static Sale getSaleById(int saleId) throws SQLException {

    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getSaleById(saleId, connection);) {
      Sale sale = null;
      while (results.next()) {
        sale = new SaleImpl();
        mapResultSetToSale(results, sale);
      }
      return sale;
    }
  }

  /**
   * Sets sale information associated with result set.
   * 
   * @param rs
   * @param sale
   * @throws SQLException
   */
  private static void mapResultSetToSale(ResultSet rs, Sale sale) throws SQLException {
    sale.setId(rs.getInt("ID"));
    User user = getUserDetails(rs.getInt("USERID"));
    sale.setUser(user);
    sale.setTotalPrice(new BigDecimal(rs.getString("TOTALPRICE")));
  }

  /**
   * Gets all sales associated with user id.
   * 
   * @param userId
   * @return sales
   * @throws SQLException
   */
  public static List<Sale> getSalesToUser(int userId) throws SQLException {
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelectHelper.getSalesToUser(userId, connection);) {
      List<Sale> sales = new ArrayList<>();
      while (results.next()) {
        Sale sale = new SaleImpl();
        mapResultSetToSale(results, sale);
        sales.add(sale);
      }
      return sales;
    }
  }

  /**
   * Gets itemized sale associated with sale id.
   * 
   * @param saleId
   * @return sale
   * @throws SQLException
   */
  public static Sale getItemizedSaleById(int saleId) throws SQLException {
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getItemizedSaleById(saleId, connection);) {
      while (results.next()) {
        Sale sale = new SaleImpl();
        mapResultSetToItemizedSale(results, sale);
        return sale;
      }
      return null;
    }
  }

  /**
   * Sets sale information associated with result set.
   * 
   * @param rs
   * @param sale
   * @throws SQLException
   */
  private static void mapResultSetToItemizedSale(ResultSet rs, Sale sale) throws SQLException {
    sale.setId(rs.getInt("SALEID"));
    Item item = getItem(rs.getInt("ITEMID"));
    Integer quantity = rs.getInt("QUANTITY");
    sale.getItemMap().put(item, quantity);
  }

  /**
   * Gets log of itemized sales.
   * 
   * @return salesLog
   * @throws SQLException
   */
  public static SalesLog getItemizedSales() throws SQLException {
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getItemizedSales(connection);) {
      SalesLog salesLog = new SalesLogImpl();
      ArrayList<Integer> saleIdList = new ArrayList<>();
      Sale sale = null;
      while (results.next()) {
        int newSaleId = results.getInt("SALEID");
        if (!saleIdList.contains(newSaleId)) {
          sale = getSaleById(newSaleId);
          salesLog.addSale(sale);
          saleIdList.add(newSaleId);
        }
        mapResultSetToItemizedSale(results, sale);
        }
      return salesLog;
    }
  } 
  
  /**
   * Gets map of restored cart associated with account id.
   * 
   * @param accountId
   * @return cartHistory
   * @throws SQLException
   */
  public static Map<Item, Integer> getAccountDetails(int accountId) throws SQLException {
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getAccountDetails(accountId, connection)) {
      Map<Item, Integer> cartHistory = new HashMap<>();
      while (results.next()) {
        int itemId = results.getInt("ITEMID");
        Item item = getItem(itemId);
        int quantity = results.getInt("QUANTITY");
        cartHistory.put(item, quantity);
      }
      return cartHistory;
    }
  }
  
  /**
   * Checks whether given account id exists in ACCOUNT table.
   * 
   * @param accountId
   * @return
   * @throws SQLException
   */
  public static boolean accountExists(int accountId) throws SQLException {
    String sql = "SELECT * FROM ACCOUNT WHERE ID = ?;";
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, accountId);
      ResultSet results = preparedStatement.executeQuery();
      return results.next();
    }
  }
  
  /**
   * Gets list of active accounts associated with given user id.
   * 
   * @param userId
   * @return
   * @throws SQLException
   */
  public static List<Integer> getActiveAccounts(int userId) throws SQLException {
    List<Integer> accounts = new ArrayList<>();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getUserActiveAccounts(userId, connection)) {
      while (results.next()) {
        int accountId = results.getInt("ID");
        accounts.add(accountId);
      }
      return accounts;
    }
  }
  
  /**
   * Gets account id associated with given user id.
   * 
   * @param userId
   * @return
   * @throws SQLException
   */
  public static List<Integer> getInactiveAccounts(int userId) throws SQLException {
    List<Integer> accounts = new ArrayList<>();
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
        ResultSet results = DatabaseSelector.getUserInactiveAccounts(userId, connection)) {
      while (results.next()) {
        int accountId = results.getInt("ID");
        accounts.add(accountId);
      }
      return accounts;
    }
  }
  
  public static Item lookupItemByName(List<Item> items, String itemName) {
    for (Item item : items) {
      if (item.getName().equalsIgnoreCase(itemName)) {
        return item;
      }
    }
    return null;
  }
  
}
