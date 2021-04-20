package com.b07.ui.screen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.domain.Item;
import com.b07.enums.Roles;
import com.b07.exceptions.AccountCreateFailedException;
import com.b07.exceptions.UserCreateFailedException;
import com.b07.services.AccountInterface;
import com.b07.services.EmployeeInterface;
import com.b07.services.InventoryInterface;
import com.b07.ui.EmployeeMenu;
import com.b07.ui.Menu;
import com.b07.ui.MenuPrompter;
import com.b07.users.Customer;
import com.b07.users.Employee;
import com.b07.users.User;

public class EmployeeScreen implements UIScreen {
  private EmployeeInterface empInterface = null;
  private Menu menu = new EmployeeMenu();

  /**
   * Shows employee screen.
   */
  public void show() {
    try {
      if (authenticate()) {
        String selected = MenuPrompter.promptForSelection(menu);
        while (!selected.equalsIgnoreCase("6")) {
          if ("1".equalsIgnoreCase(selected)) {
            if (!authenticate()) {
              break;
            }
          } else if ("2".equalsIgnoreCase(selected)) {
            makeNewUser();
          } else if ("3".equalsIgnoreCase(selected)) {
            Customer customer = getCustomer();
            if (customer != null) {
              int accountId = makeNewAccount(customer);
              if (accountId > 0) {
                System.out.println("Account has successfully been created with id: " + accountId);
              }
            } else {
              System.out.println("This customer already has an account.");
            }
          } else if ("4".equalsIgnoreCase(selected)) {
            makeNewEmployee();
          } else if ("5".equalsIgnoreCase(selected)) {
            restockInventory();
          } else {
            System.out.println("That is not a valid option");
          }
          selected = MenuPrompter.promptForSelection(menu);
        }
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Makes a new account.
   * 
   * @throws IOException
   */
  private void makeNewUser() throws IOException {
    System.out.println("creating new Customer.  * means Required field. ");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    System.out.print("Name*:");
    String name = bufferedReader.readLine();
    System.out.print("Age*:");
    String ageStr = bufferedReader.readLine();
    System.out.print("Address:");
    String address = bufferedReader.readLine();
    System.out.print("Password*:");
    String password = bufferedReader.readLine();

    try {
      int age = Integer.parseInt(ageStr);
      if (age <= 0) {
        throw new NumberFormatException("You are too young to be at this store by yourself!!!");
      }
      int id = empInterface.createCustomer(name, age, address, password);
      System.out.println("Customer " + name + " is created and assigned id, " + id);
    } catch (NumberFormatException | UserCreateFailedException e) {
      System.out.println("The customer could not be created due to: " + e.getMessage());
    }
  }

  /**
   * Makes a new account.
   * 
   * @throws IOException
   * @throws AccountCreateFailedException
   */
  private int makeNewAccount(Customer customer) throws IOException {
    int accountId = -1;
    try {
      if (customer == null) {
        throw new AccountCreateFailedException("The customer for selected id does not exist.");
      }
      AccountInterface acctInterface = new AccountInterface(customer);
      accountId = acctInterface.createAccount();
      customer.getAccounts().add(accountId);
    } catch (AccountCreateFailedException e) {
      System.out.println(e.getMessage());
    }
    return accountId;
  }

  /**
   * Makes new employee.
   * 
   * @throws IOException
   */
  private void makeNewEmployee() throws IOException {
    System.out.println("creating new Employee.  * means Required field. ");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    System.out.print("Name*:");
    String name = bufferedReader.readLine();
    System.out.print("Age*:");
    String ageStr = bufferedReader.readLine();
    System.out.print("Address:");
    String address = bufferedReader.readLine();
    System.out.print("Password*:");
    String password = bufferedReader.readLine();

    try {
      int age = Integer.parseInt(ageStr);
      if (age <= 0) {
        throw new NumberFormatException("You are too young to be working!!! Go back to sleep.");
      }
      int id = empInterface.createEmployee(name, Integer.parseInt(ageStr), address, password);
      System.out.println("Employee " + name + " is created and assigned id, " + id);
    } catch (NumberFormatException | UserCreateFailedException e) {
      System.out.println("The employee could not be created due to: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Restocks inventory.
   */
  private void restockInventory() {
    try {
      displayCurrentInventory();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
      System.out.print("Enter Item id to restock:");
      String itemIdStr = bufferedReader.readLine();
      System.out.print("Enter Quantity to restock:");
      String quantityStr = bufferedReader.readLine();
      int itemId = Integer.parseInt(itemIdStr);
      int quantity = Integer.parseInt(quantityStr);
      if (itemId <= 0) {
        throw new NumberFormatException();
      }

      Item item = DatabaseSelectHelper.getItem(Integer.parseInt(itemIdStr));
      empInterface.restockInventory(item, quantity);
    } catch (IOException | SQLException e) {
      System.out.println("Inventory could not be restocked");
      e.printStackTrace();
    } catch (NumberFormatException e) {
      System.out.println("Id and quantity must be a number > 0");
    }
  }

  /**
   * Prints current inventory.
   * 
   * @throws SQLException
   */
  private void displayCurrentInventory() throws SQLException {
    InventoryInterface inventoryInterface = new InventoryInterface();
    System.out.println("\t\tCurrent Inventory:");
    String header = String.format("%-5s%-30s%-5s", "Id", "Item Name", "Quantity");
    System.out.println(header);
    Map<Item, Integer> itemMap = inventoryInterface.getCurrentInventory().getItemMap();
    for (Item item : itemMap.keySet()) {
      String detail =
          String.format("%-5d%-30s%-5d", item.getId(), item.getName(), itemMap.get(item));
      System.out.println(detail);
    }
  }

  /**
   * Authenticates employee.
   * 
   * @return true if employee is authenticated, else false
   * @throws IOException
   * @throws SQLException
   */
  private boolean authenticate() throws IOException, SQLException {
    System.out.println("\nEmployee login:");
    LoginScreen login = new LoginScreen();
    User user = null;
    try {
      user = login.show();
      if (user instanceof Employee) {
        InventoryInterface inventoryInt = new InventoryInterface();
        Employee employee = (Employee) user;
        empInterface = new EmployeeInterface(employee, inventoryInt.getCurrentInventory());
        return true;
      }
    } catch (NumberFormatException e) {
      System.out.println("Id must be a number > 0");
    }
    return false;
  }

  public Customer getCustomer() throws SQLException {
    empInterface.displayCurrentCustomers();
    System.out.println("Please select a customer id to make an account for:");
    try {
      int custId = EmployeeInterface.getInputId();
      User user = DatabaseSelectHelper.getUserDetails(custId);
      if (user instanceof Customer) {
        Customer customer = (Customer) user;
        return customer;
      }
    } catch (IllegalArgumentException e) {
      System.out.println("That is not a valid customer id");
    } catch (SQLException | IOException e) {
      System.out.println("The customer could not be created, try again");
    }
    return null;
  }
  
}
