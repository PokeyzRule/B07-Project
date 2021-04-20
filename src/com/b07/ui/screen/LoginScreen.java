package com.b07.ui.screen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.security.Authenticator;
import com.b07.users.User;

public class LoginScreen {

  /**
   * Shows login screen.
   * 
   * @return the logged in user
   * @throws IOException
   * @throws SQLException
   */
  public User show() throws IOException, SQLException, NumberFormatException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Enter User id:");
    String userIdStr = bufferedReader.readLine().trim();
    System.out.print("Enter password:");
    String password = bufferedReader.readLine().trim();
    User user = null;
    
    if (password == null || password.length() == 0) {
      System.out.println("Those are not valid inputs.");
      return null;
    }
    
    int userId = Integer.parseInt(userIdStr);
    boolean ok = Authenticator.authenticate(userId, password);
    if (ok) {
      user = DatabaseSelectHelper.getUserDetails(userId);
      if (user != null) {
        user.setAuthenticated(true);
        System.out.println("User with id:" + userId + " has been authenticated.");
      }
    } else {
      System.out.println("Either your user id or password is incorrect.");
    }
    return user;
  }

  
}
