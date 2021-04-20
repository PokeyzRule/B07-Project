package com.b07.services;

import java.math.BigDecimal;
import java.sql.SQLException;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.ValidationFailedException;

public class SaleInterface {

  /**
   * Creates itemized sale.
   * 
   * @param saleId
   * @param itemId
   * @param quantity
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public void createItemizedSale(int saleId, int itemId, int quantity)
      throws DatabaseInsertException, SQLException, ValidationFailedException {
    DatabaseInsertHelper.insertItemizedSale(saleId, itemId, quantity);
  }

  /**
   * Creates sale.
   * 
   * @param custId
   * @param totalPriceAfterTax
   * @return saleId
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public int createSale(int custId, BigDecimal totalPriceAfterTax)
      throws DatabaseInsertException, SQLException, ValidationFailedException {
    return DatabaseInsertHelper.insertSale(custId, totalPriceAfterTax);
  }

}
