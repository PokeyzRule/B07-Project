package com.b07.services;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.domain.Inventory;
import com.b07.domain.Item;
import com.b07.domain.impl.ItemImpl;
import com.b07.enums.ItemTypes;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.InsufficientItemException;
import com.b07.exceptions.ValidationFailedException;

public class InventoryInterface {

  public static final int NUM_FISHING_ROD = 10;
  public static final BigDecimal PRICE_FISHING_ROD = new BigDecimal("5");
  public static final int NUM_HOCKEY_STICK = 10;
  public static final BigDecimal PRICE_HOCKEY_STICK = new BigDecimal("6");
  public static final int NUM_SKATES = 10;
  public static final BigDecimal PRICE_SKATES = new BigDecimal("5");
  public static final int NUM_RUNNING_SHOES = 10;
  public static final BigDecimal PRICE_RUNNING_SHOES = new BigDecimal("9");
  public static final int NUM_PROTEIN_BAR = 10;
  public static final BigDecimal PRICE_PROTEIN_BAR = new BigDecimal("2");

  /**
   * Constructor for InventoryInterface.
   */
  public InventoryInterface() {
  };

  /**
   * Gets current inventory.
   * 
   * @return inventory
   * @throws SQLException
   */
  public Inventory getCurrentInventory() throws SQLException {
    Inventory inventory = DatabaseSelectHelper.getInventory();
    return inventory;
  }

  /**
   * Initializes interface.
   * 
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  public void initializeInventory() throws DatabaseInsertException, SQLException,
  ValidationFailedException {
    System.out.println("\nInitializing Inventory");
    initializeFishingRods();
    initializeHockeySticks();
    initializeSkates();
    initializeRunningShoes();
    initializeProteinBars();
  }

  /**
   * Initializes fishing rod item.
   * 
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  private void initializeFishingRods() throws DatabaseInsertException, SQLException, 
  ValidationFailedException {
    Item fishingRod = new ItemImpl(ItemTypes.FISHING_ROD.toString(), PRICE_FISHING_ROD);
    int fishingRodId = DatabaseInsertHelper.insertItem(ItemTypes.FISHING_ROD.toString(), 
        PRICE_FISHING_ROD);
    fishingRod.setId(fishingRodId);
    DatabaseInsertHelper.insertInventory(fishingRodId, NUM_FISHING_ROD);
    getCurrentInventory().updateMap(fishingRod, NUM_FISHING_ROD);
    System.out.println("Fishing rods, itemId: " + fishingRodId + " quantity: " + NUM_FISHING_ROD);
  }

  /**
   * Initializes hockey stick item.
   * 
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  private void initializeHockeySticks() throws DatabaseInsertException, SQLException, 
  ValidationFailedException {
    Item hockeyStick = new ItemImpl(ItemTypes.HOCKEY_STICK.toString(), PRICE_HOCKEY_STICK);
    int hockeyStickId = DatabaseInsertHelper.insertItem(ItemTypes.HOCKEY_STICK.toString(), 
        PRICE_HOCKEY_STICK);
    hockeyStick.setId(hockeyStickId);
    DatabaseInsertHelper.insertInventory(hockeyStickId, NUM_HOCKEY_STICK);
    getCurrentInventory().updateMap(hockeyStick, NUM_HOCKEY_STICK);
    System.out.println("Hockey Sticks, itemId: " + hockeyStickId + " quantity: " 
    + NUM_HOCKEY_STICK);
  }

  /**
   * Initializes skates item.
   * 
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  private void initializeSkates() throws DatabaseInsertException, SQLException, 
  ValidationFailedException {
    Item skates = new ItemImpl(ItemTypes.SKATES.toString(), PRICE_SKATES);
    int skatesId = DatabaseInsertHelper.insertItem(ItemTypes.SKATES.toString(), PRICE_SKATES);
    skates.setId(skatesId);
    DatabaseInsertHelper.insertInventory(skatesId, NUM_SKATES);
    getCurrentInventory().updateMap(skates, NUM_SKATES);
    System.out.println("Skates, itemId: " + skatesId + " quantity: " + NUM_SKATES);
  }

  /**
   * Initializes running shoes item.
   * 
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  private void initializeRunningShoes() throws DatabaseInsertException, SQLException, 
  ValidationFailedException {
    Item runningShoes = new ItemImpl(ItemTypes.RUNNING_SHOES.toString(), PRICE_RUNNING_SHOES);
    int runningShoesId = DatabaseInsertHelper.insertItem(ItemTypes.RUNNING_SHOES.toString(), 
        PRICE_RUNNING_SHOES);
    runningShoes.setId(runningShoesId);
    DatabaseInsertHelper.insertInventory(runningShoesId, NUM_RUNNING_SHOES);
    getCurrentInventory().updateMap(runningShoes, NUM_RUNNING_SHOES);
    System.out.println("Running Shoes, itemId: " + runningShoesId + " quantity: " 
    + NUM_RUNNING_SHOES);
  }

  /**
   * Initializes protein bars item.
   * 
   * @throws DatabaseInsertException
   * @throws SQLException
   * @throws ValidationFailedException
   */
  private void initializeProteinBars() throws DatabaseInsertException, SQLException, 
  ValidationFailedException {
    Item proteinBar = new ItemImpl(ItemTypes.PROTEIN_BAR.toString(), PRICE_PROTEIN_BAR);
    int proteinBarId = DatabaseInsertHelper.insertItem(ItemTypes.PROTEIN_BAR.toString(), 
        PRICE_PROTEIN_BAR);
    proteinBar.setId(proteinBarId);
    DatabaseInsertHelper.insertInventory(proteinBarId, NUM_PROTEIN_BAR);
    getCurrentInventory().updateMap(proteinBar, NUM_PROTEIN_BAR);
    System.out.println("Protein Bars, itemId: " + proteinBarId + " quantity: " + NUM_PROTEIN_BAR);
  }

  /**
   * Removes item from inventory.
   * 
   * @param itemMap
   * @return true if inventory is removed, else false
   */
  public boolean removeInventory(Map<Item, Integer> itemMap) {
    try {
      for (Item item : itemMap.keySet()) {
        int existingQuantity = DatabaseSelectHelper.getInventoryQuantity(item.getId());
        int net = existingQuantity - itemMap.get(item);
        DatabaseUpdateHelper.updateInventoryQuantity(net, item.getId());
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Checks if an item is in stock.
   * 
   * @param itemMap
   * @throws SQLException
   * @throws InsufficientItemException
   * @throws ValidationFailedException
   */
  public void checkItemsInStock(Map<Item, Integer> itemMap)
      throws SQLException, InsufficientItemException, ValidationFailedException {
    for (Item item : itemMap.keySet()) {
      int existingQuantity = DatabaseSelectHelper.getInventoryQuantity(item.getId());
      if (itemMap.get(item) > existingQuantity) {
        throw new InsufficientItemException(
            "The requested quantity for " + item.getName() + " exceeds available amount");
      }
    }
  }

}
