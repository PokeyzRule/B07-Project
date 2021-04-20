package com.b07.ui;

public class CustomerAccountMenu implements Menu {

  /**
   * Display menu for customer options.
   */
  public void displayMenu() {
    System.out.println("1 - Register for an account.");
    System.out.println("2 - Shop without an account.");
    System.out.println("3 - Choose an existing account.");
    System.out.println("4 - Deactivate an account.");
    System.out.println("0 - Exit");
    System.out.print("Enter your selection:");
  }
}
