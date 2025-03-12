package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.ICompte;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompteImpl implements ICompte {

    private static final Logger logger = Logger.getLogger(CompteImpl.class.getName());
    private Db db = new Db();

    @Override
    public boolean createCompte(Compte compte) {
        if (compte == null || compte.getNumero() == null) {
            logger.warning("Compte ou numéro invalide");
            return false;
        }

        String checkSql = "SELECT COUNT(*) FROM comptes WHERE numero = ?";
        String insertSql = "INSERT INTO comptes (numero, balance, created_at, client_id) VALUES (?, ?, ?, ?)";

        try (var connection = db.getConnection()) {
            // Vérifier si le numéro existe déjà
            try (var checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setString(1, compte.getNumero());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    logger.warning("Numéro de compte déjà existant: " + compte.getNumero());
                    return false;
                }
            }

            // Insérer le nouveau compte sans la colonne 'actif'
            try (var stmt = connection.prepareStatement(insertSql)) {
                stmt.setString(1, compte.getNumero());
                stmt.setDouble(2, compte.getBalance());
                stmt.setTimestamp(3, Timestamp.from(compte.getCreatedAt()));
                stmt.setObject(4, compte.getClient() != null ? compte.getClient().getId() : null);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 1) {
                    logger.info("Compte créé avec succès: " + compte.getNumero());
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur création compte: " + compte.getNumero(), e);
            return false;
        }
    }

    public boolean accountNumberExists(String numero) {
        String sql = "SELECT COUNT(*) FROM comptes WHERE numero = ?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numero);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur vérification numéro", e);
            return false;
        }
    }



    @Override
    public List<Compte> getAllComptes() {
        String sql = "SELECT c.*, cl.nom, cl.prenom FROM comptes c LEFT JOIN clients cl ON c.client_id = cl.id ORDER BY c.numero ASC";
        List<Compte> comptes = new ArrayList<>();

        try (var connection = db.getConnection();
             var preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Compte compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setCreatedAt(rs.getTimestamp("created_at").toInstant());

                // Récupérer le client associé au compte
                int clientId = rs.getInt("client_id");
                if (clientId > 0) {
                    Client client = new Client();
                    client.setId(clientId);
                    client.setNom(rs.getString("nom"));
                    client.setPrenom(rs.getString("prenom"));
                    compte.setClient(client);
                }

                comptes.add(compte);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des comptes", e);
        }

        return comptes;
    }

    @Override
    public Compte getCompteById(int id) {
        String sql = "SELECT c.*, cl.nom, cl.prenom FROM comptes c LEFT JOIN clients cl ON c.client_id = cl.id WHERE c.id = ?";
        Compte compte = null;

        try (var connection = db.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    compte = new Compte();
                    compte.setId(rs.getInt("id"));
                    compte.setNumero(rs.getString("numero"));
                    compte.setBalance(rs.getDouble("balance"));
                    compte.setCreatedAt(rs.getTimestamp("created_at").toInstant());

                    // Récupérer le client associé au compte
                    int clientId = rs.getInt("client_id");
                    if (clientId > 0) {
                        Client client = new Client();
                        client.setId(clientId);
                        client.setNom(rs.getString("nom"));
                        client.setPrenom(rs.getString("prenom"));
                        compte.setClient(client);
                    }
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération du compte par ID", e);
        }

        return compte;
    }

    @Override
    public boolean updateCompte(Compte compte) {
        if (compte == null) {
            logger.warning("Tentative de mise à jour d'un compte null.");
            return false;
        }

        String sql = "UPDATE comptes SET balance = ?, actif = ? WHERE id = ?";
        try (var connection = db.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDouble(1, compte.getBalance());
            preparedStatement.setInt(3, compte.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la mise à jour du compte", e);
            return false;
        }
    }

    @Override
    public void supprimerCompte(Compte compte) {
        String sql = "DELETE FROM comptes WHERE id = ?";
        try (var connection = db.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, compte.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la suppression du compte", e);
        }
    }

    @Override
    public void modifierCompte(Compte compte) {
        // Corps vide pour respecter la signature de l'interface
    }

    @Override
    public void retrait(Compte compteSelectionne, double montant) {
        if (compteSelectionne.getBalance() >= montant) {
            compteSelectionne.setBalance(compteSelectionne.getBalance() - montant);
            updateCompte(compteSelectionne);
            enregistrerOperation(compteSelectionne, -montant, "Retrait");
        }
    }

    @Override
    public void depot(Compte compteSelectionne, double montant) {
        compteSelectionne.setBalance(compteSelectionne.getBalance() + montant);
        updateCompte(compteSelectionne);
        enregistrerOperation(compteSelectionne, montant, "Dépôt");
    }

    @Override
    public void virement(Compte compteSource, Compte compteDestinataire, double montant) {
        if (compteSource == null || compteDestinataire == null) {
            throw new IllegalArgumentException("Les comptes source et destinataire doivent être sélectionnés.");
        }

        if (compteSource.getBalance() >= montant) {
            retrait(compteSource, montant);
            depot(compteDestinataire, montant);
            enregistrerOperation(compteSource, -montant, "Virement vers " + compteDestinataire.getNumero());
            enregistrerOperation(compteDestinataire, montant, "Virement de " + compteSource.getNumero());
        } else {
            throw new IllegalArgumentException("Le solde du compte source est insuffisant pour le virement.");
        }
    }

    @Override
    public List<Operation> getOperations(Compte compte) {
        String sql = "SELECT * FROM operations WHERE compte_id = ? ORDER BY date ASC";
        List<Operation> operations = new ArrayList<>();

        try (var connection = db.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, compte.getId());
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
                    String description = rs.getString("description");
                    double montant = rs.getDouble("montant");
                    double solde = rs.getDouble("solde");
                    String type = rs.getString("type");

                    Operation operation = new Operation(date, description, montant, solde, type, compte);
                    operation.setId(rs.getInt("id"));
                    operations.add(operation);
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des opérations", e);
        }

        return operations;
    }

    public List<Client> getAllClients() {
        String sql = "SELECT id, nom, prenom FROM clients ORDER BY nom ASC";
        List<Client> clients = new ArrayList<>();

        try (var connection = db.getConnection();
             var preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                clients.add(client);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des clients", e);
        }
        return clients;
    }

    private void enregistrerOperation(Compte compte, double montant, String description) {
        String sql = "INSERT INTO operations (date, description, montant, solde, compte_id, type) VALUES (?, ?, ?, ?, ?, ?)";
        try (var connection = db.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, montant);
            preparedStatement.setDouble(4, compte.getBalance());
            preparedStatement.setInt(5, compte.getId());
            preparedStatement.setString(6, montant < 0 ? "Débit" : "Crédit");

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'enregistrement de l'opération", e);
        }
    }
}
