package com.b07.ui;

public class CustomerSelectItemMenu implements Menu {

  /**
   * Displays menu for items customers can choose.
   */
  public void displayMenu() {
    System.out.println("Please select an item to add to your cart");
    System.out.println("1 - Fishing Rod.");
    System.out.println("2 - Hockey Stick");
    System.out.println("3 - Skates");
    System.out.println("4 - Running Shoes");
    System.out.println("5 - Protein Bar");
    System.out.println("0 - Cancel");
    System.out.print("Enter selection:");
  }

}
