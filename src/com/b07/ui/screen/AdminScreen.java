package com.b07.ui.screen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.domain.Item;
import com.b07.domain.Sale;
import com.b07.domain.SalesLog;
import com.b07.exceptions.DataExportException;
import com.b07.exceptions.DataImportException;
import com.b07.exceptions.UnauthenticatedException;
import com.b07.services.AdminInterface;
import com.b07.services.DbExporter;
import com.b07.services.DbImporter;
import com.b07.services.EmployeeInterface;
import com.b07.ui.AdminMenu;
import com.b07.ui.Menu;
import com.b07.ui.MenuPrompter;
import com.b07.users.Admin;
import com.b07.users.Employee;
import com.b07.users.User;

public class AdminScreen implements UIScreen {
  private AdminInterface adminInterface = null;
  private Menu menu = new AdminMenu();
  private EmployeeInterface empInterface = new EmployeeInterface();

  /**
   * Shows admin screen.
   */
  public void show() {
    try {
      LoginScreen login = new LoginScreen();
      User user = login.show();
      if (user == null) {
        throw new UnauthenticatedException("Either your id or password is incorrect.");
      }
      if (user instanceof Admin) {
        Admin admin = (Admin) user;
        adminInterface = new AdminInterface(admin);
        String selected = MenuPrompter.promptForSelection(menu);
        while (!selected.equalsIgnoreCase("0")) {
          if ("1".equalsIgnoreCase(selected)) {
            showAllEmployees();
            promoteEmployee();
          } else if ("2".equalsIgnoreCase(selected)) {
            viewBooks();
          } else if ("3".equalsIgnoreCase(selected)) {
            displayInactiveAccounts();
          } else if ("4".equalsIgnoreCase(selected)) {
            displayActiveAccounts();
          } else if ("5".equalsIgnoreCase(selected)) {
            exportDb();
          } else if ("6".equalsIgnoreCase(selected)) {
            importDb();
          } else {
            System.out.println("That is not a valid option.");
          }
          selected = MenuPrompter.promptForSelection(menu);
        }
      }
    } catch (UnauthenticatedException e) {
      System.out.println("Could not log the user in: " + e.getMessage());
    } catch (NumberFormatException e) {
      System.out.println("Id must be a number > 0.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void importDb() throws DataImportException {
    DbImporter importer = new DbImporter();
    importer.importData(DbExporter.SERIALIZED_DATABASE_NAME);
  }

  private void exportDb() throws DataExportException {
    DbExporter exporter = new DbExporter();
    exporter.exportData(DbExporter.SERIALIZED_DATABASE_NAME);
  }

  private void displayActiveAccounts() throws SQLException, IllegalArgumentException, IOException {
    empInterface.displayCurrentCustomers();
    int custId = EmployeeInterface.getInputId();
    List<Integer> activeAccounts = adminInterface.getActiveAccounts(custId);
    System.out.println("Active accounts for customer with id:" + custId);
    System.out.println(activeAccounts.toString());
  }

  private void displayInactiveAccounts()
      throws SQLException, IllegalArgumentException, IOException {
    empInterface.displayCurrentCustomers();
    int custId = EmployeeInterface.getInputId();
    List<Integer> inactiveAccounts = adminInterface.getInactiveAccounts(custId);
    System.out.println("Inactive accounts for customer with id:" + custId);
    System.out.println(inactiveAccounts.toString());
  }

  /**
   * Prints all employees.
   * 
   * @throws SQLException
   */
  private void showAllEmployees() throws SQLException {
    List<User> users = DatabaseSelectHelper.getUsersDetails();
    System.out.println("\tCurrent Employees");

    System.out.println("Employee Id\tName");

    for (User user : users) {
      if (user instanceof Employee) {
        System.out.println(user.getId() + "\t\t" + user.getName());
      }
    }
  }

  /**
   * Allows admin to promote employee to admin.
   * 
   * @throws IOException
   * @throws SQLException
   */
  private void promoteEmployee() throws SQLException {
    int userId = getUserId();
    if (userId > 0) {
      User user = DatabaseSelectHelper.getUserDetails(userId);
      if (user instanceof Employee) {
        boolean ok = adminInterface.promoteEmployee((Employee) user);
        if (ok) {
          System.out.println("Employee " + user.getName() + " has been promoted to Admin.");
        } else {
          System.out.println("Employee could not be promoted to Admin.");
        }
      }
    }
  }

  private int getUserId() {
    try {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
      System.out.print("Enter Employee id to Promote:");
      String userIdStr = bufferedReader.readLine();
      int userId = Integer.parseInt(userIdStr);
      if (userId <= 0) {
        throw new NumberFormatException();
      }
      return userId;
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NumberFormatException e) {
      System.out.println("Id must be a number > 0");
    }
    return -1;
  }

  public void viewBooks() throws SQLException {
    System.out.println("\nRecords:");
    SalesLog log = DatabaseSelectHelper.getItemizedSales();
    List<Sale> sales = log.getSales();
    showCustomerLog(sales);
    showItemRecords(sales);
    System.out.println("End of records.\n");
  }

  private void showItemRecords(List<Sale> sales) {
    Map<Item, Integer> itemMap = new HashMap<>();
    BigDecimal total = new BigDecimal("0");
    for (Sale sale : sales) {
      Map<Item, Integer> saleItemMap = sale.getItemMap();
      for (Item item : saleItemMap.keySet()) {
        int quantity = saleItemMap.get(item);
        if (itemMap.containsKey(item)) {
          quantity = itemMap.get(item) + quantity;
        }
        itemMap.put(item, quantity);
      }
      total = total.add(sale.getTotalPrice());
    }
    showItemLog(itemMap);
    System.out.println("TOTAL SALES: " + total.toString());
  }

  private void showItemLog(Map<Item, Integer> itemMap) {
    for (Item item : itemMap.keySet()) {
      System.out.println("Number " + item.getName() + " sold: " + itemMap.get(item));
    }
  }

  private void showCustomerLog(List<Sale> sales) {
    for (Sale sale : sales) {
      System.out.println("Customer: " + sale.getUser().getName());
      System.out.println("Purchase Number: " + sale.getId());
      System.out.println("Total Purchase Price: " + sale.getTotalPrice());

      Map<Item, Integer> saleItemMap = sale.getItemMap();
      System.out.print("Itemized Breakdown: ");
      for (Item item : saleItemMap.keySet()) {
        System.out.println(item.getName() + ": " + saleItemMap.get(item));
        System.out.print("\t\t");
      }
      System.out.println();
      System.out.println("-------------------------------------------------------------");
    }
  }
}
