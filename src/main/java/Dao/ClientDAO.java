package Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/Db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public boolean CreateClient(String nom, String prenom, String email) {
        String sql = "INSERT INTO clients (nom, prenom, email) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setString(3, email);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion du client : " + e.getMessage());
            return false;
        }
    }

    public boolean createClient(String gaye, String abdoulaye, String mail) {
        return false;
    }
}