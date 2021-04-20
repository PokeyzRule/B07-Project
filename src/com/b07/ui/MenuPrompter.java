package com.b07.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MenuPrompter {

  /**
   * Prompts and returns user input Open close, method is closed and
   * menu.displayMenu is open.
   * 
   * @param menu
   * @return the user input
   * @throws IOException
   */
  public static String promptForSelection(Menu menu) throws IOException {
    menu.displayMenu();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    String input = bufferedReader.readLine();
    System.out.println("you selected:" + input);
    return input;
  }
}
