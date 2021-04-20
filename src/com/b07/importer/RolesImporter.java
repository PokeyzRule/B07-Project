package com.b07.importer;

import java.sql.SQLException;
import java.util.List;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.DataImportException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.ValidationFailedException;

public class RolesImporter implements DbRecordImporter {

  @Override
  public void importRecords(List<Object> data) throws DataImportException {
    System.out.println("RolesImporter starting...");
    int count = 0;
    try {
      for (Object entity : data) {
        if (entity instanceof String) {
          count++;
          int id = DatabaseSelectHelper.getRoleIdByName(entity.toString());
          if (id < 0) {
            DatabaseInsertHelper.insertRole(entity.toString());
          }
        }
      }

    } catch (ValidationFailedException | DatabaseInsertException | SQLException e) {
      throw new DataImportException("Cannot import Role :" + e.getMessage());
    }
    System.out.println("RolesImporter finished import with " + count + " records.");
  }
}
