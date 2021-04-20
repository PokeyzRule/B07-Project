package com.b07.exporter;

import java.util.List;
import com.b07.exceptions.DataExportException;

public interface DbRecordExporter<T> {

  List<T> exportRecords() throws DataExportException;
}
