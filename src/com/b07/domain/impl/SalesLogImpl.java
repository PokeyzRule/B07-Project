package com.b07.domain.impl;

import java.util.ArrayList;
import java.util.List;
import com.b07.domain.Sale;
import com.b07.domain.SalesLog;

public class SalesLogImpl implements SalesLog {

  private List<Sale> sales = new ArrayList<>();

  @Override
  public List<Sale> getSales() {
    return sales;
  }

  @Override
  public void addSale(Sale sale) {
    if (sale != null) {
      sales.add(sale);
    }
  }

  @Override
  public int getTotalSales() {
    return sales.size();
  }
}
