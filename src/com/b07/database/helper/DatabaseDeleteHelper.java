package com.b07.database.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseDeleteHelper {

  public static void deleteAccountSummaries(int accountId) throws SQLException {
    String sql = "DELETE FROM ACCOUNTSUMMARY WHERE ACCTID = ?;";
    try (Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();) {
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, accountId);
      preparedStatement.executeUpdate();
    }
  }
}
