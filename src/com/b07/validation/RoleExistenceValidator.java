package com.b07.validation;

import java.sql.SQLException;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.ValidationFailedException;

public class RoleExistenceValidator implements Validator {
  private int roleId;

  /**
   * Initializes roleId for RoleExistenceValidator.
   * 
   * @param roleId
   */
  public RoleExistenceValidator(int roleId) {
    this.roleId = roleId;
  }

  /**
   * Validates roleId is from database.
   */
  @Override
  public void validate() throws ValidationFailedException {
    try {
      String roleName = DatabaseSelectHelper.getRoleName(roleId);
      if (roleName == null) {
        throw new ValidationFailedException("Role id, " + roleId + " not found. ");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new ValidationFailedException(e.getMessage());
    }

  }

}
