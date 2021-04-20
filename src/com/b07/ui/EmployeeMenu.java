package com.b07.ui;

public class EmployeeMenu implements Menu {

  /**
   * Displays menu for employee options.
   */
  public void displayMenu() {
    System.out.println("1 - Authenticate new employee");
    System.out.println("2 - Make new User");
    System.out.println("3 - Make new account");
    System.out.println("4 - Make new Employee");
    System.out.println("5 - Restock Inventory");
    System.out.println("6 - Exit");
    System.out.print("Enter selection:");
  }

}
