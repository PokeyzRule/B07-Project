package com.b07.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.b07.exceptions.DataExportException;
import com.b07.exporter.DbRecordExporter;
import com.b07.exporter.DbRecordExporterFactory;

public class DbExporter {

  public static final String SERIALIZED_DATABASE_NAME = "database_copy.ser";

  public static void serialize(Object obj, String filename) {
    try (FileOutputStream file = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(file);) {
      out.writeObject(obj);
      out.flush();
    } catch (IOException e) {
      System.out.println("Could not serialize class: check logs.");
      e.printStackTrace();
    }
  }


  public void exportData(String filename) throws DataExportException {
    try {
      System.out.println("Export filename=" + filename + " started.");
      List<DbRecordExporter> exporterList = DbRecordExporterFactory.getDbRecordExporters();
      List<Serializable> data = new ArrayList<>();
      for (DbRecordExporter recordExporter : exporterList) {
        data.addAll(recordExporter.exportRecords());
      }
      serialize(data, SERIALIZED_DATABASE_NAME);
      System.out.println("Export filename=" + filename + " finished export " + data.size() + " records.");
    } catch (DataExportException e) {
      e.printStackTrace();
      throw new DataExportException("Cannot export data:" + e.getMessage());
    }
  }
}
