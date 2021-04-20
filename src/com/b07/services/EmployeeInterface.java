package com.b07.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.domain.Inventory;
import com.b07.domain.Item;
import com.b07.enums.Roles;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.UserCreateFailedException;
import com.b07.exceptions.ValidationFailedException;
import com.b07.users.Employee;
import com.b07.users.User;

public class EmployeeInterface {

  private Employee currentEmployee;
  private Inventory inventory;
  
  public EmployeeInterface() {};

  /**
   * Initializes employee and inventory for EmployeeInterface.
   * 
   * @param employee
   * @param inventory
   */
  public EmployeeInterface(Employee employee, Inventory inventory) {
    setCurrentEmployee(employee);
    this.inventory = inventory;
  }

  /**
   * Initializes only inventory for EmployeeInterface.
   * 
   * @param inventory
   */
  public EmployeeInterface(Inventory inventory) {
    this.inventory = inventory;
  }

  /**
   * Checks id EmployeeInterface has employee.
   * 
   * @return true if EmployeeInterface has employee, else false
   */
  public boolean hasCurrentEmployee() {
    return currentEmployee != null;
  }

  public void setCurrentEmployee(Employee currentEmployee) {
    if (currentEmployee.isAuthenticated()) {
      this.currentEmployee = currentEmployee;
    }
  }

  /**
   * Restocks quantity of an item.
   * 
   * @param item
   * @param quantity
   * @return boolean
   */
  public boolean restockInventory(Item item, int quantity) {
    try {
      DatabaseUpdateHelper.updateInventoryQuantity(quantity, item.getId());
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ValidationFailedException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Creates customer.
   * 
   * @param name
   * @param age
   * @param address
   * @param password
   * @return userId
   * @throws UserCreateFailedException
   */
  public int createCustomer(String name, int age, String address, String password) 
      throws UserCreateFailedException {
    try {
      int userId = DatabaseInsertHelper.insertNewUser(name, age, address, password);
      int roleId = DatabaseSelectHelper.getRoleIdByName(Roles.CUSTOMER.toString());
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
  public int createEmployee(String name, int age, String address, String password) 
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
  
  public static int getInputId() throws IOException, IllegalArgumentException {
    System.out.println("Select an id from the menu.");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    String inputId = bufferedReader.readLine();
    try {
      int id = Integer.parseInt(inputId);
      if (id <= 0) {
        throw new NumberFormatException();
      }
      return id;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("ID must be a number > 0.");
    }
  }
  
  public void displayCurrentCustomers() throws SQLException {
    int roleId = DatabaseSelectHelper.getRoleIdByName(Roles.CUSTOMER.toString());
    List<Integer> customerIds = DatabaseSelectHelper.getUsersByRole(roleId);
    List<User> customers = new ArrayList<>();
    System.out.println("ID\tName\tAge\tAddress");
    for (int custId : customerIds) {
      User customer = DatabaseSelectHelper.getUserDetails(custId);
      customers.add(customer);
      System.out.println(customer.getId() + "\t" + customer.getName() + "\t" + customer.getAge()
          + "\t" + customer.getAddress());
    }
  }

}
