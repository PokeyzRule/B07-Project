package com.b07.validation;

import java.sql.SQLException;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.ValidationFailedException;

public class UserExistenceValidator implements Validator {
  private int userId;

  /**
   * Initializes userId for UserExistenceValidator.
   * 
   * @param userId
   */
  public UserExistenceValidator(int userId) {
    this.userId = userId;
  }

  /**
   * Validates userId is from database.
   * 
   * @throws ValidationFailedException
   */
  @Override
  public void validate() throws ValidationFailedException {
    try {
      String name = DatabaseSelectHelper.getUserName(userId);
      if (name == null) {
        throw new ValidationFailedException("User id, " + userId + " not found. ");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new ValidationFailedException(e.getMessage());
    }

  }

}
