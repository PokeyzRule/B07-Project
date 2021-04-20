package com.b07.ui;

public class CustomerMenu implements Menu {

  /**
   * Display menu for customer options.
   */
  public void displayMenu() {
    System.out.println("1 - List current items in cart");
    System.out.println("2 - Add a quantity of an item to the cart");
    System.out.println("3 - Check total price of items in the cart");
    System.out.println("4 - Remove a quantity of an item from the cart");
    System.out.println("5 - Check out");
    System.out.println("6 - Exit");
    System.out.println("7 - Restore shopping cart");
    System.out.print("Enter selection:");
  }
}
