package com.b07.security;

import java.sql.SQLException;
import com.b07.database.helper.DatabaseSelectHelper;

public class Authenticator {

  /**
   * Attempts to authenticate the user using the unhashed password with the hashed password stored
   * in the database.
   * 
   * @param userId the user id of the user to authenticate.
   * @param password the password to compare the hashed password with.
   * @return true if password matched in the database, false otherwise.
   */
  public static final boolean authenticate(int userId, String password) {
    try {
      String dbPassword = DatabaseSelectHelper.getPassword(userId);
      return PasswordHelpers.comparePassword(dbPassword, password);
    } catch (SQLException e) {
      System.out.println("Password could not be resolved.");
    }
    return false;
  }

}
