package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.IClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientImpl implements IClient {

    private static final Logger logger = Logger.getLogger(ClientImpl.class.getName());
    private Db db = new Db();

    @Override
    public boolean createClient(Client client) {
        String sql = "INSERT INTO clients (nom, prenom, email) VALUES (?, ?, ?)";
        try (Connection connection = db.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, client.getNom());
            preparedStatement.setString(2, client.getPrenom());
            preparedStatement.setString(3, client.getEmail());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la création du client", e);
            return false;
        }
    }

    @Override
    public List<Client> getAllClients() {
        String sql = "SELECT * FROM clients ORDER BY nom ASC";
        List<Client> clients = new ArrayList<>();

        try (Connection connection = db.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                clients.add(client);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des clients", e);
        }

        return clients;
    }

    @Override
    public Client getClientById(int id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        Client client = null;

        try (Connection connection = db.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    client = new Client();
                    client.setId(rs.getInt("id"));
                    client.setNom(rs.getString("nom"));
                    client.setPrenom(rs.getString("prenom"));
                    client.setEmail(rs.getString("email"));
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération du client par ID", e);
        }

        return client;
    }

    @Override
    public List<Compte> getAllComptes() {
        String sql = "SELECT * FROM comptes ORDER BY created_at DESC";
        List<Compte> comptes = new ArrayList<>();

        try (Connection connection = db.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Compte compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setActif(rs.getBoolean("actif"));
                compte.setCreatedAt(rs.getTimestamp("created_at").toInstant());

                int clientId = rs.getInt("client_id");
                Client client = getClientById(clientId);
                compte.setClient(client);

                comptes.add(compte);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des comptes", e);
        }

        return comptes;
    }

    @Override
    public void ajouterCompte(Compte compte) {
        String sql = "INSERT INTO comptes (numero, balance, created_at, client_id, actif) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = db.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, compte.getNumero());
            preparedStatement.setDouble(2, compte.getBalance());
            preparedStatement.setTimestamp(3, java.sql.Timestamp.from(compte.getCreatedAt()));
            preparedStatement.setInt(4, compte.getClient().getId());
            preparedStatement.setBoolean(5, compte.isActif());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'ajout du compte", e);
        }
    }

    @Override
    public void modifierCompte(Compte compte) {
        String sql = "UPDATE comptes SET balance = ?, actif = ? WHERE id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDouble(1, compte.getBalance());
            preparedStatement.setBoolean(2, compte.isActif());
            preparedStatement.setInt(3, compte.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la modification du compte", e);
        }
    }

    @Override
    public void supprimerCompte(Compte compte) {
        String sql = "DELETE FROM comptes WHERE id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, compte.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la suppression du compte", e);
        }
    }

    @Override
    public void modifierClient(Client client) {
        String sql = "UPDATE clients SET nom = ?, prenom = ?, email = ? WHERE id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, client.getNom());
            preparedStatement.setString(2, client.getPrenom());
            preparedStatement.setString(3, client.getEmail());
            preparedStatement.setInt(4, client.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la modification du client", e);
        }
    }

    @Override
    public void supprimerClient(Client client) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, client.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la suppression du client", e);
        }
    }

    @Override
    public void ajouterClient(Client client) {
        createClient(client);
    }
}