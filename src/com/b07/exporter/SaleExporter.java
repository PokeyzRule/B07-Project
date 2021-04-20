package com.b07.exporter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.domain.Sale;
import com.b07.domain.SalesLog;
import com.b07.exceptions.DataExportException;

public class SaleExporter implements DbRecordExporter<Sale> {

  @Override
  public List<Sale> exportRecords() throws DataExportException {
    List<Sale> sales = new ArrayList<>();
    try {
      SalesLog salesLog = DatabaseSelectHelper.getSales();
      sales = salesLog.getSales();
      for (Sale sale : sales) {
        Sale saleItems = DatabaseSelectHelper.getItemizedSaleById(sale.getId());
        sale.setItemMap(saleItems.getItemMap());
      }
      return sales;
    } catch (SQLException e) {
      throw new DataExportException("Could not export sales.");
    }
  }
}
