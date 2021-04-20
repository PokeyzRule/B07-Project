package com.b07.exporter;

import java.sql.SQLException;
import java.util.List;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.domain.Item;
import com.b07.exceptions.DataExportException;

public class ItemExporter implements DbRecordExporter<Item> {

  @Override
  public List<Item> exportRecords() throws DataExportException {
    try {
      List<Item> result = DatabaseSelectHelper.getAllItems();
      System.out.println("ItemExporter exported:" + result.size());
      return result;
    } catch (SQLException e) {
      throw new DataExportException("Could not export items.");
    }
  }
}
