package com.b07.ui.screen;

import java.io.IOException;
import com.b07.ui.MainMenu;
import com.b07.ui.MenuPrompter;

public class MainScreen implements UIScreen {
  MainMenu menu = new MainMenu();

  /**
   * Shows initial menu.
   */
  @Override
  public void show() {
    try {
      String selected = MenuPrompter.promptForSelection(menu);
      while (!"0".equalsIgnoreCase(selected)) {
        if ("1".equalsIgnoreCase(selected)) {
          showEmployeeScreen();
          break;
        } else if ("2".equalsIgnoreCase(selected)) {
          showCustomerScreen();
          break;
        } else {
          System.out.println("That is not a valid option");
        }
        selected = MenuPrompter.promptForSelection(menu);
      }
      System.out.println("Bye");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Shows employee screen.
   */
  private void showEmployeeScreen() {
    UIScreen screen = new EmployeeScreen();
    screen.show();
  }

  /**
   * Shows customer screen.
   */
  private void showCustomerScreen() {
    UIScreen screen = new CustomerScreen();
    screen.show();
  }

}
