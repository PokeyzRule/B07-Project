package com.b07.importer;

import java.util.ArrayList;
import java.util.List;

public class DbRecordImporterFactory {

  public static List<DbRecordImporter> getDbRecordImporters() {
    List<DbRecordImporter> importers = new ArrayList<>();
    importers.add(new RolesImporter());
    importers.add(new UserImporter());
    importers.add(new ItemImporter());
    importers.add(new InventoryImporter());
    importers.add(new AccountImporter());
    importers.add(new SaleImporter());

    return importers;
  }
}
