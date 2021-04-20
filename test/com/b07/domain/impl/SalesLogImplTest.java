package com.b07.domain.impl;

import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.b07.domain.Sale;
import com.b07.domain.SalesLog;

public class SalesLogImplTest {

  SalesLog salesLog;
  Sale sale;

  @Before
  public void setUp() throws Exception {
    salesLog = new SalesLogImpl();
    sale = new SaleImpl();
    sale.setId(1);
  }

  @Test
  public void testAddSale() {
    salesLog.addSale(sale);
    
    List <Sale> salesList = salesLog.getSales();
    assertTrue(salesList.contains(sale));
  }
  
  @Test
  public void testGetTotalSales() {
    salesLog.addSale(sale);
    salesLog.addSale(sale);
    salesLog.addSale(sale);

    assertTrue(3 == salesLog.getTotalSales());
  }

}
