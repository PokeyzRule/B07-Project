package com.b07.ui.screen;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.b07.exceptions.UnauthenticatedException;
import com.b07.services.AccountInterface;
import com.b07.services.EmployeeInterface;
import com.b07.services.ShoppingCart;
import com.b07.ui.CustomerAccountMenu;
import com.b07.ui.Menu;
import com.b07.ui.MenuPrompter;
import com.b07.users.Customer;

public class CustomerAccountScreen {

  private Customer customer;
  private Menu menu = new CustomerAccountMenu();
  private AccountInterface accountInterface = null;

  public CustomerAccountScreen(Customer customer) {
    this.customer = customer;
    this.accountInterface = new AccountInterface(customer);
  }

  /**
   * Shows customer account screen.
   */
  public Customer show() {
    try {
      String selected = MenuPrompter.promptForSelection(menu);
      while (!"0".equalsIgnoreCase(selected)) {
        if ("1".equalsIgnoreCase(selected)) {
          registerForAccount(customer);
          break;
        } else if ("2".equalsIgnoreCase(selected)) {
          System.out.println(
              "Proceeding to shop without account - you will not be able to save your cart.");
          break;
        } else if ("3".equalsIgnoreCase(selected)) {
          if (customer.hasAccount()) {
            chooseExistingAccount();
            return customer; 
          } else {
            System.out.println("This customer does not have any accounts.");
            System.out.println("Please register for an account.");
          }
        } else if ("4".equalsIgnoreCase(selected)) {
          if (customer.hasAccount()) {
            deactivateAccount(customer);
          } else {
            System.out.println("This customer does not have any account to deactivate :-).");
          }
        } else {
          System.out.println("That is not a valid option");
        }
        selected = MenuPrompter.promptForSelection(menu);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private void chooseExistingAccount() {
    System.out.println("Current accounts for customer with id: " + customer.getId());
    try {
      int accountId = chooseAccountId();
      boolean exists = accountInterface.accountExists(accountId);
      if (exists) {
        customer.setCurrentAcctId(accountId);
      }
    } catch (UnauthenticatedException | SQLException | IllegalArgumentException | IOException e) {
      System.out.println("Could not load the account:" + e.getMessage());
      System.out.println("Proceeding to shop without account.");
    }

  }

  /**
   * Registers the given customer with an account through employee login.
   * 
   * @param customer
   */
  private void registerForAccount(Customer customer) {
    System.out.println("Please wait while the next available employee creates an account for you.");
    System.out.println("Once this is done, please log in again.");
    UIScreen employeeScreen = new EmployeeScreen();
    employeeScreen.show();
  }

  /**
   * Deactivates account which customer chooses.
   * 
   * @param customer
   */
  private void deactivateAccount(Customer customer) {
    try {
      int accountId = chooseAccountId();
      if (customer.getCurrentAcctId() == accountId) {
        customer.setCurrentAcctId(0);
      }
      accountInterface.deactivateAccount(accountId);
      int indexToRemove = customer.getAccounts().indexOf(accountId);
      customer.getAccounts().remove(indexToRemove);
    } catch (UnauthenticatedException | SQLException | IllegalArgumentException | IOException e) {
      System.out.println("Could not deactivate account: " + e.getMessage());
    }
  }

  public void printAccounts() throws UnauthenticatedException, SQLException {
    for (int accountId : customer.getAccounts()) {
      System.out.println("Items in cart with account id:" + accountId);
      customer.setCurrentAcctId(accountId);
      ShoppingCart tmpCart = new ShoppingCart(customer);
      tmpCart.reloadShoppingCart();
      CustomerScreen customerScreen = new CustomerScreen();
      customerScreen.displayItemsInCart(tmpCart);
      System.out.println();
    }
  }

  private int chooseAccountId() throws IOException, UnauthenticatedException, SQLException, IllegalArgumentException {
    printAccounts();
    return EmployeeInterface.getInputId();
  }
}
