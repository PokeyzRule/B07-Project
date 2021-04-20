package com.b07.exporter;

import java.util.ArrayList;
import java.util.List;

public class DbRecordExporterFactory {

  public static List<DbRecordExporter> getDbRecordExporters() {
    List<DbRecordExporter> exporterList = new ArrayList<>();
    exporterList.add(new RolesExporter());
    exporterList.add(new UserExporter());
    exporterList.add(new ItemExporter());
    exporterList.add(new InventoryExporter());
    exporterList.add(new AccountExporter());
    exporterList.add(new SaleExporter());

    return exporterList;
  }
}
