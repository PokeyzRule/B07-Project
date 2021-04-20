package com.b07.ui;

public class MainMenu implements Menu {

  /**
   * Displays initial login menu.
   */
  public void displayMenu() {
    System.out.println("1 - Employee Login");
    System.out.println("2 - Customer Login");
    System.out.println("0 - Exit");
    System.out.print("Enter selection:");
  }

}
