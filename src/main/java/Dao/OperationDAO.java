package Dao;

import java.sql.*;

public class OperationDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/Db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public boolean createOperation(String type, double amount, Date dateOp, int compteId) {
        String sql = "INSERT INTO operations (type, amount, date_op, compte_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, type);
            statement.setDouble(2, amount);
            statement.setTimestamp(3, new java.sql.Timestamp(dateOp.getTime()));
            statement.setInt(4, compteId);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de l'opération : " + e.getMessage());
            return false;
        }
    }
}
