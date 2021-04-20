package com.b07.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import com.b07.database.helper.DatabaseDriverHelper;
import com.b07.exceptions.ConnectionFailedException;
import com.b07.exceptions.DataExportException;
import com.b07.exceptions.DataImportException;
import com.b07.importer.DbRecordImporter;
import com.b07.importer.DbRecordImporterFactory;

public class DbImporter {

  private static final String DATABASE_BACKUP_NAME = "inventorymgmt_bk.ser";

  public Object deserialize(String filename) throws IOException, ClassNotFoundException {
    try (FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream objectInputStream = new ObjectInputStream(fis)) {
      return objectInputStream.readObject();
    }
  }

  public void backup() throws DataExportException {
    System.out.println("DbImporter backup");
    DbExporter exporter = new DbExporter();
    exporter.exportData(DATABASE_BACKUP_NAME);
  }

  public void restore() throws DataImportException {
    System.out.println("DbImporter restore.");
    reInitializeDb();
    try {
      List<Object> data = (List<Object>) deserialize(DATABASE_BACKUP_NAME);
      List<DbRecordImporter> importers = DbRecordImporterFactory.getDbRecordImporters();
      for (DbRecordImporter importer : importers) {
        importer.importRecords(data);
      }
    } catch (IOException | ClassNotFoundException e) {
      throw new DataImportException("Cannot restore data:" + e.getMessage());
    }
  }

  public void importData(String filename) throws DataImportException {
    try {
      List<Object> data = (List<Object>) deserialize(filename);
      System.out
          .println("ImportData " + filename + "Finished deserialize" + data.size() + " records.");
      List<DbRecordImporter> importers = DbRecordImporterFactory.getDbRecordImporters();
      backup();
      reInitializeDb();
      for (DbRecordImporter importer : importers) {
        try {
          importer.importRecords(data);
        } catch (DataImportException e) {
          restore();
          throw e;
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      throw new DataImportException("Cannot import data:" + e.getMessage());
    } catch (DataExportException e) {
      e.printStackTrace();
      throw new DataImportException("Cannot backup data:" + e.getMessage());
    }
    System.out.println("ImportData " + filename + " finished.");
  }
  
  private void reInitializeDb() {
    try {
      DatabaseDriverHelper.reInitializeDb();
    } catch (ConnectionFailedException e) {
      System.out.println("Could not connect to the database.");
    }
  }
}
