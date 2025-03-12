package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {
    private Connection connection;

    public CompteImpl(Connection connection) throws SQLException {
        this.connection = Db.getConnection();
    }

    @Override
    public void ajouterCompte(Compte compte) {
        String sql = "INSERT INTO comptes (numero, balance, created_at, client_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, compte.getNumero());
            stmt.setDouble(2, compte.getBalance());
            stmt.setDate(3, Date.valueOf(compte.getCreatedAt()));
            stmt.setInt(4, compte.getClient().getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        compte.setId(generatedKeys.getInt(1));  // Récupère l'ID généré
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifierCompte(Compte compte) {
        String sql = "UPDATE comptes SET numero = ?, balance = ?, created_at = ?, client_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, compte.getNumero());
            stmt.setDouble(2, compte.getBalance());
            stmt.setDate(3, Date.valueOf(compte.getCreatedAt()));
            stmt.setInt(4, compte.getClient().getId());
            stmt.setInt(5, compte.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerCompte(int id) {
        String sql = "DELETE FROM comptes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Compte getCompteById(int id) {
        String sql = "SELECT * FROM comptes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String numero = rs.getString("numero");
                double balance = rs.getDouble("balance");
                Date created_at = Date.valueOf(rs.getDate("created_at").toLocalDate());
                int clientId = rs.getInt("client_id");

                // Récupérer le client
                Client client = new Client(); // Récupérer le client en fonction de l'ID client
                client.setId(clientId);

                return new Compte(numero, balance, created_at.toLocalDate(), client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Méthode pour récupérer un compte par son numéro
    public Compte getCompteByNumero(String numero) {
        Compte compte = null;

        String sql = "SELECT c.id, c.numero, c.balance, c.created_at, cl.id AS client_id, cl.prenom, cl.nom, cl.email " +
                "FROM comptes c " +
                "JOIN clients cl ON c.client_id = cl.id " +
                "WHERE c.numero = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numero);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String numCompte = rs.getString("numero");
                    double balance = rs.getDouble("balance");
                    Date createdAt = rs.getDate("created_at");

                    // Récupérer les informations du client
                    int clientId = rs.getInt("client_id");
                    String prenom = rs.getString("prenom");
                    String nom = rs.getString("nom");
                    String email = rs.getString("email");

                    // Créer le client
                    Client client = new Client(clientId, prenom, nom, email);

                    // Créer le compte avec le client associé
                    compte = new Compte(numCompte, balance, createdAt.toLocalDate(), client);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return compte;
    }

    @Override
    public List<Compte> getTousLesComptes() {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT * FROM comptes";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String numero = rs.getString("numero");
                double balance = rs.getDouble("balance");
                Date created_at = rs.getDate("created_at");
                int clientId = rs.getInt("client_id");

                // Récupérer les infos du client
                Client client = getClientById(clientId);

                comptes.add(new Compte(numero, balance, created_at.toLocalDate(), client));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comptes;
    }

    @Override
    public void updateCompte(Compte compte) {
        String sql = "UPDATE comptes SET balance = ? WHERE numero = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, compte.getBalance());
            stmt.setString(2, compte.getNumero());
            stmt.executeUpdate();
            System.out.println("Compte mis à jour avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Client getClientById(int clientId) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Client(
                        rs.getInt("id"),
                        rs.getString("prenom"),
                        rs.getString("nom"),
                        rs.getString("email") // Ajoute d'autres champs si nécessaire
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Client(); // Retourne un client vide si non trouvé (à améliorer selon ton besoin)
    }


}
