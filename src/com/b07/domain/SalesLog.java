package com.b07.domain;

import java.util.List;

public interface SalesLog {

  /**
   * Returns the list of sales made.
   * 
   * @return the list of sales made.
   */
  public List<Sale> getSales();

  /**
   * Adds a sale to the sales log.
   * 
   * @param sale the sale to add to the sales log.
   */
  public void addSale(Sale sale);

  /**
   * Returns the total number of sales made.
   * 
   * @return the total number of sales made.
   */
  public int getTotalSales();

}
