package com.b07.services;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.domain.Item;
import com.b07.domain.Sale;
import com.b07.domain.SalesLog;
import com.b07.enums.Roles;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.UnauthenticatedException;
import com.b07.exceptions.UserCreateFailedException;
import com.b07.exceptions.ValidationFailedException;
import com.b07.users.Admin;
import com.b07.users.Employee;

public class AdminInterface {
  private Admin admin;

  /**
   * Initializes admin for AdminInterface.
   * 
   * @param admin
   * @throws UnauthenticatedException
   */
  public AdminInterface(Admin admin) throws UnauthenticatedException {
    setAdmin(admin);
  }

  /**
   * Gets admin.
   * 
   * @return the admin
   */
  public Admin getAdmin() {
    return admin;
  }

  /**
   * Sets admin.
   * 
   * @param admin
   * @throws UnauthenticatedException
   */
  public void setAdmin(Admin admin) throws UnauthenticatedException {
    if (admin.isAuthenticated()) {
      this.admin = admin;
    } else {
      throw new UnauthenticatedException("Admin User is not logged in");

    }
  }

  /**
   * Creates admin.
   * 
   * @param name
   * @param age
   * @param address
   * @param password
   * @return userId
   * @throws UserCreateFailedException
   */
  public static int createAdmin(String name, int age, String address, String password)
      throws UserCreateFailedException {
    try {
      int userId = DatabaseInsertHelper.insertNewUser(name, age, address, password);
      int roleId = DatabaseSelectHelper.getRoleIdByName(Roles.ADMIN.toString());
      DatabaseInsertHelper.insertUserRole(userId, roleId);
      return userId;
    } catch (DatabaseInsertException | SQLException | ValidationFailedException e) {
      throw new UserCreateFailedException(e.getMessage(), e);
    }
  }

  /**
   * Creates employee.
   * 
   * @param name
   * @param age
   * @param address
   * @param password
   * @return userId
   * @throws UserCreateFailedException
   */
  public static int createEmployee(String name, int age, String address, String password)
      throws UserCreateFailedException {
    try {
      int userId = DatabaseInsertHelper.insertNewUser(name, age, address, password);
      int roleId = DatabaseSelectHelper.getRoleIdByName(Roles.EMPLOYEE.toString());
      DatabaseInsertHelper.insertUserRole(userId, roleId);
      return userId;
    } catch (DatabaseInsertException | SQLException | ValidationFailedException e) {
      throw new UserCreateFailedException(e.getMessage(), e);
    }
  }

  /**
   * Takes user who is currently an employee and promote to admin.
   * 
   * @param employee
   * @return boolean
   */
  public boolean promoteEmployee(Employee employee) {
    try {
      int roleId = DatabaseSelectHelper.getRoleIdByName(Roles.ADMIN.toString());
      DatabaseUpdateHelper.updateUserRole(roleId, employee.getId());
      return true;
    } catch (SQLException | ValidationFailedException e) {
      e.printStackTrace();
    }
    return false;
  }

  public List<Integer> getInactiveAccounts(int custId) throws SQLException {
    List<Integer> inactiveAccounts = DatabaseSelectHelper.getInactiveAccounts(custId);
    return inactiveAccounts;
  }

  public List<Integer> getActiveAccounts(int custId) throws SQLException {
    List<Integer> activeAccounts = DatabaseSelectHelper.getActiveAccounts(custId);
    return activeAccounts;
  }

}
