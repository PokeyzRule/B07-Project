package com.b07.exporter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.domain.Inventory;
import com.b07.exceptions.DataExportException;

public class InventoryExporter implements DbRecordExporter<Inventory> {

  @Override
  public List<Inventory> exportRecords() throws DataExportException {
    try {
      List<Inventory> result = new ArrayList<>();
      Inventory inventory = DatabaseSelectHelper.getInventory();
      if (inventory.getItemMap().size() > 0) {
        result.add(inventory);
      }
      System.out.println("InventoryExporter exported: " + result.size() + " item count="
          + inventory.getItemMap().size());
      return result;
    } catch (SQLException e) {
      throw new DataExportException("Could not export Inventory.");
    }
  }
}
