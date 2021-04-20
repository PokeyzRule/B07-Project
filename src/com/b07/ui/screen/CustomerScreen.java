package com.b07.ui.screen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import com.b07.domain.Inventory;
import com.b07.domain.Item;
import com.b07.domain.impl.InventoryImpl;
import com.b07.domain.impl.ItemImpl;
import com.b07.enums.ItemTypes;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InsufficientItemException;
import com.b07.exceptions.UnauthenticatedException;
import com.b07.exceptions.ValidationFailedException;
import com.b07.services.AccountInterface;
import com.b07.services.InventoryInterface;
import com.b07.services.ShoppingCart;
import com.b07.ui.CustomerMenu;
import com.b07.ui.CustomerSelectItemMenu;
import com.b07.ui.Menu;
import com.b07.ui.MenuPrompter;
import com.b07.users.Customer;
import com.b07.users.User;

public class CustomerScreen implements UIScreen {
  private Menu menu = new CustomerMenu();
  private ShoppingCart cart;
  private Inventory inventory = new InventoryImpl();

  /**
   * Shows customer screen.
   */
  public void show() {
    try {
      LoginScreen login = new LoginScreen();
      User user = login.show();
      if (user instanceof Customer) {
        Customer customer = (Customer) user;
        customer.setAccounts(AccountInterface.getAccounts(customer.getId()));
        CustomerAccountScreen accountScreen = new CustomerAccountScreen(customer);
        Customer custWithAccount = accountScreen.show();
        if (custWithAccount != null) {
          customer = custWithAccount;
        }
        cart = new ShoppingCart(customer);


        String selected = MenuPrompter.promptForSelection(menu);
        InventoryInterface inventoryInterface = new InventoryInterface();
        inventory = inventoryInterface.getCurrentInventory();

        while (!selected.equalsIgnoreCase("6")) {
          if ("1".equalsIgnoreCase(selected)) {
            displayItemsInCart();
          } else if ("2".equalsIgnoreCase(selected)) {
            addItemToCart();
          } else if ("3".equalsIgnoreCase(selected)) {
            checkTotalPrice();
          } else if ("4".equalsIgnoreCase(selected)) {
            removeItemFromCart();
          } else if ("5".equalsIgnoreCase(selected)) {
            boolean continueShopping = checkOut();
            if (!continueShopping) {
              break;
            }
          } else if ("7".equalsIgnoreCase(selected)) {
            reloadCart(customer);
          } else {
            System.out.println("That is not a valid option");
          }
          saveCart(customer);
          selected = MenuPrompter.promptForSelection(menu);
        }

      }
    } catch (NumberFormatException e) {
      System.out.println("Id must be a number > 0");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void saveCart(Customer customer) {
    if (customer.hasAccount()) {
      try {
        cart.save();
      } catch (DatabaseInsertException | SQLException | ValidationFailedException e) {
        System.out.println("Could not save cart: " + e.getMessage());
      }
    }
  }


  private void reloadCart(Customer customer) throws UnauthenticatedException {
    System.out.println("Current accounts for user:" + customer.getId());
    if (customer.hasAccount()) {
      try {
        // CustomerAccountScreen accountScreen = new CustomerAccountScreen(customer);
        // accountScreen.printAccounts(customer);
        // int accountId = EmployeeInterface.getInputId();
        // if (customer.getAccounts().contains(accountId)) {
        cart.reloadShoppingCart();
        // } else {
        // System.out.println("That is an inactive account!");
        // }
      } catch (SQLException | IllegalArgumentException e) {
        System.out.println("Could not reload the cart: " + e.getMessage());
      }
    } else {
      System.out.println("The customer does not have an account! Please register for one.");
    }
  }


  /**
   * Checks out customer.
   * 
   * @return true if checkout was successful, else false
   * @throws IOException
   */
  private boolean checkOut() throws IOException {
    boolean continueShopping = false;
    BigDecimal total = cart.calcTotalAfterTax();
    System.out.println("Your current total is: " + total.toString());
    System.out.println("Do you wish to continue shopping? (y/n):");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    String input = bufferedReader.readLine();

    if (input.equalsIgnoreCase("y")) {
      System.out.println("Cancelling checkout and returning to menu");
      continueShopping = true;
    } else if (input.equalsIgnoreCase("n")) {
      try {
        cart.checkOut();
      } catch (ValidationFailedException | InsufficientItemException e) {
        System.out.println(e.getMessage());
        System.out.println("Please try again");
      }
    } else {
      System.out.println("That is not a valid option, returning to menu");
    }
    return continueShopping;
  }

  /**
   * Removes quantity of item from cart.
   * 
   * @throws IOException
   */
  private void removeItemFromCart() throws IOException {
    Item itemToRemove = displayRemoveItemChoices();
    if (itemToRemove != null) {
      int quantity = Integer.valueOf(getQuantity());
      cart.removeItem(itemToRemove, quantity);
    }
  }

  /**
   * Prompts user input for item to remove.
   * 
   * @return the item to be removed
   * @throws IOException
   */
  private Item displayRemoveItemChoices() throws IOException {
    displayItemsInCart();
    System.out.println("Please enter item id to remove: ");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    String input = bufferedReader.readLine();
    try {
      Item item = new ItemImpl();
      int itemId = Integer.parseInt(input);
      if (itemId <= 0) {
        throw new NumberFormatException();
      }
      item.setId(itemId);
      List<Item> currentItems = cart.getItems();
      int index = currentItems.indexOf(item);
      return currentItems.get(index);
    } catch (NumberFormatException e) {
      System.out.println("ItemId must be a number > 0");
    }
    return null;
  }

  /**
   * Adds item to cart.
   * 
   * @throws IOException
   */
  private void addItemToCart() throws IOException {
    String itemName = displayAddItemChoices();
    if (itemName.isEmpty() || itemName.length() == 0) {
      return;
    }
    int quantity = Integer.valueOf(getQuantity());
    Map<Item, Integer> itemMap = inventory.getItemMap();
    Item itemToAdd = new ItemImpl();
    for (Item item : itemMap.keySet()) {
      if (item.getName().equals(itemName)) {
        itemToAdd = item;
      }
    }
    System.out.print("Attempting to add item with id: " + itemToAdd.getId());
    System.out.println(" with quantity: " + quantity);
    cart.addItem(itemToAdd, quantity);
  }

  /**
   * Prompts user input for item to add.
   * 
   * @return the name of item to be added
   * @throws IOException
   */
  private String displayAddItemChoices() throws IOException {
    String selected = MenuPrompter.promptForSelection(new CustomerSelectItemMenu());
    return getItemChoice(selected);
  }

  /**
   * Prints total price of items in cart.
   */
  private void checkTotalPrice() {
    System.out.println("Total price in cart is: " + cart.getTotal().toString());
  }

  /**
   * Prints items in cart.
   */
  private void displayItemsInCart() {
    System.out.println("Current item \tItemId \tQuantity \tItem total");
    Map<Item, Integer> cartItemMap = cart.getCartItemMap();
    for (Item item : cartItemMap.keySet()) {
      int quantity = cartItemMap.get(item);
      String quantityStr = String.valueOf(quantity);
      BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(quantityStr));
      System.out
          .println(item.getName() + "\t" + item.getId() + "\t" + quantity + "\t\t" + itemTotal);
    }
  }

  public void displayItemsInCart(ShoppingCart cart) {
    System.out.println("Current item \tItemId \tQuantity \tItem total");
    Map<Item, Integer> cartItemMap = cart.getCartItemMap();
    for (Item item : cartItemMap.keySet()) {
      int quantity = cartItemMap.get(item);
      String quantityStr = String.valueOf(quantity);
      BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(quantityStr));
      System.out
          .println(item.getName() + "\t" + item.getId() + "\t" + quantity + "\t\t" + itemTotal);
    }
  }

  /**
   * Returns itemName based on user input.
   * 
   * @param selected
   * @return the item name
   */
  private String getItemChoice(String selected) {
    String itemName = "";
    if ("1".equalsIgnoreCase(selected)) {
      itemName = ItemTypes.FISHING_ROD.toString();
    } else if ("2".equalsIgnoreCase(selected)) {
      itemName = ItemTypes.HOCKEY_STICK.toString();
    } else if ("3".equalsIgnoreCase(selected)) {
      itemName = ItemTypes.SKATES.toString();
    } else if ("4".equalsIgnoreCase(selected)) {
      itemName = ItemTypes.RUNNING_SHOES.toString();
    } else if ("5".equalsIgnoreCase(selected)) {
      itemName = ItemTypes.PROTEIN_BAR.toString();
    } else {
      System.out.println("That is not a valid choice");
    }
    return itemName;
  }

  /**
   * Prompts user input for quantity of item, checks if it is valid.
   * 
   * @return the user inputed number
   * @throws IOException
   */
  private int getQuantity() throws IOException {
    System.out.println("Please enter a quantity:");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    String input = bufferedReader.readLine();
    int inputNum = 0;
    try {
      inputNum = Integer.parseInt(input);
    } catch (NumberFormatException e) {
      System.out.println("Quantity must be a number greater than 0");
    }
    return inputNum;
  }

}
