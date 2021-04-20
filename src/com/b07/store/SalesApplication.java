package com.b07.store;

import java.sql.Connection;
import java.sql.SQLException;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.enums.Roles;
import com.b07.exceptions.ConnectionFailedException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.UserCreateFailedException;
import com.b07.exceptions.ValidationFailedException;
import com.b07.services.AdminInterface;
import com.b07.services.InventoryInterface;
import com.b07.ui.screen.AdminScreen;
import com.b07.ui.screen.MainScreen;
import com.b07.ui.screen.UIScreen;

public class SalesApplication {

  private UIScreen launchingScreen;

  /**
   * Constructor for SalesApplication with the specified UI screen to display at launch.
   * 
   * @param launchingScreen the UI screen to be displayed at launch.
   */
  public SalesApplication(UIScreen launchingScreen) {
    this.launchingScreen = launchingScreen;
  }

  /**
   * Displays the launching screen that is set.
   */
  public void launch() {
    launchingScreen.show();
  }

  /**
   * Initializes the database with all the required tables, inserts the roles into the ROLES table,
   * creates the first admin and employee accounts, and initializes the inventory.
   * 
   * @param connection the connection to the database.
   */
  public static void initializeDb(Connection connection) {
    int customerRoleid;
    try {
      DatabaseDriverExtender.initialize(connection);
      System.out.println("Database tables created. ");

      int adminRoleId = DatabaseInsertHelper.insertRole(Roles.ADMIN.toString());
      System.out.println("ADMIN Role created, assigned RoleId=" + adminRoleId);
      customerRoleid = DatabaseInsertHelper.insertRole(Roles.CUSTOMER.toString());
      System.out.println("CUSTOMER Role created, assigned RoleId=" + customerRoleid);
      int empRoleId = DatabaseInsertHelper.insertRole(Roles.EMPLOYEE.toString());
      System.out.println("EMPLOYEE Role created, assigned RoleId=" + empRoleId);

      int adminUserId = AdminInterface.createAdmin("admin", 19, "address", "admin");
      System.out.println("Admin user: admin created, assigned id=" + adminUserId);
      int empUserId = AdminInterface.createEmployee("simon", 19, "address", "simon");
      System.out.println("First user: simon created, assigned id=" + empUserId);

      InventoryInterface inventoryInt = new InventoryInterface();
      inventoryInt.initializeInventory();
    } catch (DatabaseInsertException | SQLException | ValidationFailedException
        | UserCreateFailedException e) {
      System.out.println("Could not initialize database, see logs");
      e.printStackTrace();
    } catch (ConnectionFailedException e) {
      System.out.println("Could not establish connection, see logs");
      e.printStackTrace();
    }
  }


  /**
   * This is the main method to run your entire program! Follow the "Pulling it together"
   * instructions to finish this off.
   * 
   * The main method does different things depending on what is entered in the command line:
   * 
   * -1: first run only, initializes all required elements.
   * 
   * 1: launch in admin mode, login required.
   * 
   * anything else (including nothing): launch in employee or customer mode, login required.
   * 
   * @param argv the mode to launch the application in, -1 for the first run for initialization, 1
   *        to launch in admin mode, anything else or nothing to log in as an employee or customer.
   */
  public static void main(String[] argv) {

    Connection connection = DatabaseDriverExtender.connectOrCreateDataBase();
    if (connection == null) {
      System.out.print("NOOO");
    } else {
      System.out.println("Connection is good\n");
    }
    try {
      // TODO Check what is in argv
      // If it is -1
      /*
       * TODO This is for the first run only! Add this code:
       * DatabaseDriverExtender.initialize(connection); Then add code to create your first account,
       * an administrator with a password Once this is done, create an employee account as well.
       * 
       */
      if (argv.length == 1) {
        if (argv[0].equals("-1")) {
          // System.out.println("Expected one of the following: -1 (db initialization), 1
          // (Admin mode)");
          initializeDb(connection);
        } else if (argv[0].equals("1")) {
          SalesApplication app = new SalesApplication(new AdminScreen());
          app.launch();
        }
      } else {
        SalesApplication app = new SalesApplication(new MainScreen());
        app.launch();
      }

      // If it is 1
      /*
       * TODO In admin mode, the user must first login with a valid admin account This will allow
       * the user to promote employees to admins. Currently, this is all an admin can do.
       */
      // If anything else - including nothing
      /*
       * TODO Create a context menu, where the user is prompted with: 1 - Employee Login 2 -
       * Customer Login 0 - Exit Enter Selection:
       */



      // If the user entered 1
      /*
       * TODO Create a context menu for the Employee interface Prompt the employee for their id and
       * password Attempt to authenticate them. If the Id is not that of an employee or password is
       * incorrect, end the session If the Id is an employee, and the password is correct, create an
       * EmployeeInterface object then give them the following options: 1. authenticate new employee
       * 2. Make new User 3. Make new account 4. Make new Employee 5. Restock Inventory 6. Exit
       * 
       * Continue to loop through as appropriate, ending once you get an exit code (9)
       */
      // If the user entered 2
      /*
       * TODO create a context menu for the customer Shopping cart Prompt the customer for their id
       * and password Attempt to authenticate them If the authentication fails or they are not a
       * customer, repeat If they get authenticated and are a customer, give them this menu: 1. List
       * current items in cart 2. Add a quantity of an item to the cart 3. Check total price of
       * items in the cart 4. Remove a quantity of an item from the cart 5. check out 6. Exit
       * 
       * When checking out, be sure to display the customers total, and ask them if they wish to
       * continue shopping for a new order
       * 
       * For each of these, loop through and continue prompting for the information needed Continue
       * showing the context menu, until the user gives a 6 as input.
       */
      // If the user entered 0
      /*
       * TODO Exit condition
       */
      // If the user entered anything else:
      /*
       * TODO Re-prompt the user
       */

    } catch (Exception e) {
      // TODO Improve this!
      System.out.println("Could not start the application, problem is: " + e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (Exception e) {
        System.out.println("Looks like it was closed already :)");
      }
    }

  }
}
