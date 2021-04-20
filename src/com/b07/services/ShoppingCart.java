package com.b07.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.b07.domain.Item;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InsufficientItemException;
import com.b07.exceptions.UnauthenticatedException;
import com.b07.exceptions.ValidationFailedException;
import com.b07.users.Customer;

public class ShoppingCart {

  private HashMap<Item, Integer> cartItemMap = new HashMap<>();
  private Customer customer;
  private BigDecimal total = new BigDecimal("0");
  private static final BigDecimal TAXRATE = new BigDecimal("1.13");

  /**
   * Initializes customer for ShoppingCart.
   * 
   * @param customer
   * @throws UnauthenticatedException
   */
  public ShoppingCart(Customer customer) throws UnauthenticatedException {
    if (!customer.isAuthenticated()) {
      throw new UnauthenticatedException("Customer is not logged in");
    }
    this.customer = customer;
  }

  /**
   * Gets item map for cart.
   * 
   * @return the cart's ItemMap
   */
  public HashMap<Item, Integer> getCartItemMap() {
    return cartItemMap;
  }

  /**
   * Adds item with given quantity to the cart.
   * 
   * @param item
   * @param quantity
   */
  public void addItem(Item item, int quantity) {
    if (quantity > 0) {
      Integer oldQuantity = cartItemMap.get(item);
      Integer net = quantity;
      if (oldQuantity != null) {
        net = oldQuantity + quantity;
      }
      String quantityStr = String.valueOf(quantity);
      cartItemMap.put(item, net);
      total = total.add(item.getPrice().multiply(new BigDecimal(quantityStr)));
    }
  }

  /**
   * Removes item from cart.
   * 
   * @param item
   * @param quantity
   */
  public void removeItem(Item item, int quantity) {
    if (quantity > 0) {
      Integer oldQuantity = cartItemMap.get(item);
      if (oldQuantity != null) {
        Integer net = oldQuantity - quantity;
        if (net <= 0) {
          net = 0;
          cartItemMap.remove(item);
        } else {
          cartItemMap.put(item, net);
        }
      }
      total = total.subtract(item.getPrice().multiply(new BigDecimal("" + quantity)));
      if (total.compareTo(new BigDecimal("0")) < 0) {
        total = new BigDecimal("0");
      }
    }
  }

  /**
   * Returns items in item map.
   * 
   * @return the item keys of cart's ItemMap
   */
  public List<Item> getItems() {
    return new ArrayList<>(cartItemMap.keySet());
  }

  /**
   * Gets customer associated with cart.
   * 
   * @return customer
   */
  public Customer getCustomer() {
    return this.customer;
  }

  /**
   * Gets total price of items in cart.
   * 
   * @return the total price
   */
  public BigDecimal getTotal() {
    return this.total;
  }

  /**
   * Checks out customer.
   * 
   * @throws InsufficientItemException
   * @throws ValidationFailedException
   * @return true if checkout successful, else false
   */
  public boolean checkOut() throws InsufficientItemException, ValidationFailedException {
    try {
      InventoryInterface inventoryInt = new InventoryInterface();
      inventoryInt.checkItemsInStock(cartItemMap);
      inventoryInt.removeInventory(cartItemMap);
      createSale(customer.getId());
      clearCart();
      // Delete account summary on checkout
      System.out.println("Thank you for shopping with us today! :-)");
      return true;
    } catch (SQLException | DatabaseInsertException e) {
      System.out.println("Could not check out, please try again");
    }
    return false;
  }

  /**
   * Clears all items in cart.
   */
  public void clearCart() {
    cartItemMap.clear();
    total = new BigDecimal("0");
  }

  /**
   * Calculates and returns total price after tax.
   * 
   * @return the total price after tax
   */
  public BigDecimal calcTotalAfterTax() {
    return total.multiply(TAXRATE).setScale(2, RoundingMode.HALF_EVEN);
  }

  /**
   * Creates a Sale.
   * 
   * @param custId
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public int createSale(int custId)
      throws DatabaseInsertException, SQLException, ValidationFailedException {
    SaleInterface saleInterface = new SaleInterface();
    int saleId = saleInterface.createSale(custId, calcTotalAfterTax());
    for (Item item : cartItemMap.keySet()) {
      saleInterface.createItemizedSale(saleId, item.getId(), cartItemMap.get(item));
    }
    return saleId;
  }

  /**
   * Restores the previous session's items in shopping cart, if available.
   * 
   * @throws SQLException
   */
  public void reloadShoppingCart() throws SQLException {
    AccountInterface acctInterface = new AccountInterface(customer);
    clearCart();
    Map<Item, Integer> itemMap = acctInterface.getAccountSummary();
    for (Item item : itemMap.keySet()) {
      addItem(item, itemMap.get(item));
    }
  }

  /**
   * Saves current items in cart into the database.
   * 
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public void save() throws DatabaseInsertException, SQLException, ValidationFailedException {
    AccountInterface acctInterface = new AccountInterface(customer);
    acctInterface.updateAccountSummary(cartItemMap);
  }

  public void updateTotal() {
    total = new BigDecimal("0");
    for (Item item : cartItemMap.keySet()) {
      total = total.add(new BigDecimal(cartItemMap.get(item).toString()));
    }
  }

}
