package com.b07.importer;

import java.sql.SQLException;
import java.util.List;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.DataImportException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.ValidationFailedException;
import com.b07.users.User;

public class UserImporter implements DbRecordImporter {

  @Override
  public void importRecords(List<Object> data) throws DataImportException {
    System.out.println("UserImporter starting...");
    int count = 0;
    try {
      for (Object entity : data) {
        if (entity instanceof User) {
          User inUser = (User) entity;
          count++;
          User user = DatabaseSelectHelper.getUserDetails(inUser.getId());
          if (user == null) {
            DatabaseInsertHelper.insertNewUserWithHashedPassword(inUser.getName(), inUser.getAge(),
                inUser.getAddress(), inUser.getHashedPassword());
          } else {
            DatabaseUpdateHelper.updateUserAddress(inUser.getAddress(), inUser.getId());
            DatabaseUpdateHelper.updateUserAge(inUser.getAge(), inUser.getId());
            DatabaseUpdateHelper.updateUserName(inUser.getName(), inUser.getId());
            DatabaseUpdateHelper.updateUserRole(inUser.getRoleId(), inUser.getId());
          }
        }
      }
    } catch (ValidationFailedException | DatabaseInsertException | SQLException e) {
      throw new DataImportException("Cannot import user: " + e.getMessage());
    }
    System.out.println("UserImporter finished import with " + count + " records.");
  }
}
