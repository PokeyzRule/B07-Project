package com.b07.database.helper;

import java.sql.Connection;
import com.b07.database.DatabaseDriver;
import com.b07.exceptions.ConnectionFailedException;

public class DatabaseDriverHelper extends DatabaseDriver {

  /**
   * Returns the connection to the database.
   */
  protected static Connection connectOrCreateDataBase() {
    return DatabaseDriver.connectOrCreateDataBase();
  }

  public static void reInitializeDb() throws ConnectionFailedException {
    DatabaseDriver.reInitialize();
  }
}
