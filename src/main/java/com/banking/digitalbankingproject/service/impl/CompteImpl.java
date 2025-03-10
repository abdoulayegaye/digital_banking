package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {
    private final Db db = new Db();
    private final ClientImpl clientService = new ClientImpl();

    @Override
    public int addCompte(Compte compte) {
        String sql = "INSERT INTO comptes (numero, balance, created_at, client_id) VALUES (?, ?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, generateAccountNumber());
            db.getPstm().setDouble(2, compte.getBalance());
            db.getPstm().setTimestamp(3, Timestamp.from(Instant.now()));
            db.getPstm().setInt(4, compte.getClient().getId());
            return db.executeMaj();
        } catch (SQLException e) {
            handleException(e);
            return 0;
        } finally {
            db.closeConnection();
        }
    }

    public String generateAccountNumber() {
        return "ACC-" + System.currentTimeMillis();
    }

    @Override
    public double consulterSolde(int compteId) {
        String sql = "SELECT balance FROM comptes WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compteId);
            ResultSet rs = db.executeSelect();
            return rs.next() ? rs.getDouble("balance") : 0;
        } catch (SQLException e) {
            handleException(e);
            return 0;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public int associerCompteClient(String numeroCompte, int clientId) {
        String sql = "UPDATE comptes SET client_id = ? WHERE numero = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, clientId);
            db.getPstm().setString(2, numeroCompte);
            return db.executeMaj();
        } catch (SQLException e) {
            handleException(e);
            return 0;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public int fermerCompte(int compteId) {
        String sql = "DELETE FROM comptes WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compteId);
            return db.executeMaj();
        } catch (SQLException e) {
            handleException(e);
            return 0;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public List<Compte> getAllComptes() {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT c.*, cl.id as client_id, cl.nom, cl.prenom, cl.email " +
                "FROM comptes c " +
                "LEFT JOIN clients cl ON c.client_id = cl.id";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                comptes.add(mapResultSetToCompte(rs));
            }
            return comptes;
        } catch (SQLException e) {
            handleException(e);
            return List.of();
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public Compte getCompteById(int id) {
        String sql = "SELECT c.*, cl.id as client_id, cl.nom, cl.prenom, cl.email " +
                "FROM comptes c " +
                "LEFT JOIN clients cl ON c.client_id = cl.id " +
                "WHERE c.id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ResultSet rs = db.executeSelect();
            return rs.next() ? mapResultSetToCompte(rs) : null;
        } catch (SQLException e) {
            handleException(e);
            return null;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public Compte getCompteByNumero(String numeroCompte) {
        String sql = "SELECT c.*, cl.id as client_id, cl.nom, cl.prenom, cl.email " +
                "FROM comptes c " +
                "LEFT JOIN clients cl ON c.client_id = cl.id " +
                "WHERE c.numero = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, numeroCompte);
            ResultSet rs = db.executeSelect();
            return rs.next() ? mapResultSetToCompte(rs) : null;
        } catch (SQLException e) {
            handleException(e);
            return null;
        } finally {
            db.closeConnection();
        }
    }

    private Compte mapResultSetToCompte(ResultSet rs) throws SQLException {
        Compte compte = new Compte();
        compte.setId(rs.getInt("id"));
        compte.setNumero(rs.getString("numero"));
        compte.setBalance(rs.getDouble("balance"));
        compte.setCreatedAt(rs.getTimestamp("created_at").toInstant());

        // Création de l'objet Client
        Client client = new Client(
                rs.getInt("client_id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email")
        );

        compte.setClient(client);
        return compte;
    }

    @Override
    public int countComptesActif() {
        return executeCountQuery("SELECT COUNT(*) FROM comptes");
    }

    @Override
    public int countComptesFerme() {
        return executeCountQuery("SELECT COUNT(*) FROM comptes WHERE balance = 0");
    }

    @Override
    public int countClientsComptes() {
        return executeCountQuery("SELECT COUNT(DISTINCT client_id) FROM comptes");
    }

    private int executeCountQuery(String sql) {
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            handleException(e);
            return 0;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public List<Client> loadClients() {
        return clientService.getAllClients();
    }

    @Override
    public List<Compte> searchCompteByNumero(String numeroCompte) {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT c.*, cl.id as client_id, cl.nom, cl.prenom, cl.email " +
                "FROM comptes c " +
                "LEFT JOIN clients cl ON c.client_id = cl.id " +
                "WHERE c.numero LIKE ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, "%" + numeroCompte + "%");
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                comptes.add(mapResultSetToCompte(rs));
            }
            return comptes;
        } catch (SQLException e) {
            handleException(e);
            return List.of();
        } finally {
            db.closeConnection();
        }
    }

    private void handleException(Exception e) {
        System.err.println("Erreur base de données : ");
        e.printStackTrace();
        // Logger professionnel recommandé
    }
}