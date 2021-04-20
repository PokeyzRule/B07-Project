package com.b07.ui;

public class AdminMenu implements Menu {

  /**
   * Displays menu for admin options.
   */
  public void displayMenu() {
    System.out.println("1 - Promote Employee.");
    System.out.println("2 - View books.");
    System.out.println("3 - Display inactive accounts for customer.");
    System.out.println("4 - Display active accounts for customer.");
    System.out.println("5 - Export current database.");
    System.out.println("6 - Import database (From set location).");
    System.out.println("0 - Exit.");
    System.out.print("Enter selection:");
  }

}
