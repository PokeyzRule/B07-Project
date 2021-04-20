package com.b07.exporter;

import java.sql.SQLException;
import java.util.List;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.DataExportException;
import com.b07.users.User;

public class UserExporter implements DbRecordExporter<User> {

  @Override
  public List<User> exportRecords() throws DataExportException {
    try {
      List<User> users = DatabaseSelectHelper.getUsersDetails();
      for (User user : users) {
        user.setHashedPassword(DatabaseSelectHelper.getPassword(user.getId()));
      }
      System.out.println("UserExporter exported with " + users.size());
      return users;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataExportException("Could not export users.");
    }
  }
}
