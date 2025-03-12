package Dao;

import java.sql.*;

public class CompteDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/Db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public boolean createCompte(String numero, double balance, Date createdAt, int client_Id) {
        String sql = "INSERT INTO comptes (numero, balance, createdAt, client_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numero);
            statement.setDouble(2, balance);
            statement.setDate(3, new java.sql.Date(createdAt.getTime()));
            statement.setInt(4, client_Id);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion du compte : " + e.getMessage());
            return false;
        }
    }
}
