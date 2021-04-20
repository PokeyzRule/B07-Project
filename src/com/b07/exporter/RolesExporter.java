package com.b07.exporter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.DataExportException;

public class RolesExporter implements DbRecordExporter<String> {

  @Override
  public List<String> exportRecords() throws DataExportException {
    List<String> entities = new ArrayList<>();
    try {
      for (Integer roleId : DatabaseSelectHelper.getRoleIds()) {
        entities.add(DatabaseSelectHelper.getRoleName(roleId));
      }
    } catch (SQLException e) {
      throw new DataExportException("Could not export roles");
    }
    System.out.println("RolesExporter exported:" + entities.size());
    return entities;
  }
}
