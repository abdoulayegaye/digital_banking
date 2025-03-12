package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {
    private Db db = new Db();
    private ResultSet rs;
    private int ok;

    @Override
    public int addCompte(Compte compte) {
        String sql = "INSERT INTO comptes (numero, balance, client_id) VALUES (?, ?, ?)";
        int ok = 0;

        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, compte.getNumero());
            db.getPstm().setDouble(2, compte.getBalance());
            db.getPstm().setInt(3, compte.getClient().getId());
            ok = db.executeMaj();
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }


    @Override
    public List<Compte> getAllComptes() {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT c.id, c.numero, c.balance, c.created_at, c.client_id, cl.prenom, cl.nom " +
                "FROM comptes c " +
                "LEFT JOIN clients cl ON c.client_id = cl.id " + // Jointure avec la table clients
                "ORDER BY c.numero ASC";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()) {
                Compte compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setCreatedAt(rs.getTimestamp("created_at").toInstant());

                int clientId = rs.getInt("client_id");
                if (!rs.wasNull()) { // Vérifie si client_id n'est pas null
                    Client client = new Client();
                    client.setId(clientId);
                    client.setPrenom(rs.getString("prenom")); // Récupère le prénom du client
                    client.setNom(rs.getString("nom"));       // Récupère le nom du client
                    compte.setClient(client); // Affecter l'objet Client au compte
                }
                comptes.add(compte);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comptes;
    }

    @Override
    public Compte getCompteById(int id) {
        Compte compte = null;
        String sql = "SELECT * FROM comptes WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            rs = db.executeSelect();
            if (rs.next()) {
                compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                int clientId = rs.getInt("client_id");
                if (!rs.wasNull()) { // Vérifie si client_id n'est pas null
                    Client client = new Client();
                    client.setId(clientId); // Supposons que la classe Client a une méthode setId
                    compte.setClient(client); // Affecter l'objet Client au compte
                }
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compte;
    }

    @Override
    public double consulterSolde(int compteId) {
        String sql = "SELECT balance FROM comptes WHERE id = ?";
        double solde = 0.0;

        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compteId);
            ResultSet rs = db.executeSelect();

            if (rs.next()) {
                solde = rs.getDouble("balance");
            }

            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return solde;
    }

    @Override
    public ObservableList<Client> loadClients() {
        String sql = "SELECT id, prenom, nom FROM clients";
        ObservableList<Client> clientList = FXCollections.observableArrayList();

        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setPrenom(rs.getString("prenom"));
                client.setNom(rs.getString("nom"));
                clientList.add(client);
            }

            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientList;
    }

    @Override
    public int associerCompteClient(String numeroCompte, int clientId) {
        String sql = "UPDATE comptes SET client_id = ? WHERE numero = ?";
        int ok = 0;

        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, clientId);
            db.getPstm().setString(2, numeroCompte);

            ok = db.executeMaj();
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public int fermerCompte(int compteId) {
        try {
            Compte compte = getCompteById(compteId);
            if (compte == null || compte.getBalance() != 0) {
                return 0;
            }

            String sql = "UPDATE comptes SET statut = ? WHERE id = ?";
            db.initPrepar(sql);
            db.getPstm().setBoolean(1, false);
            db.getPstm().setInt(2, compteId);

            return db.executeMaj() > 0 ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public Compte getCompteByNumero(String numeroCompte) {
        Compte compte = null;
        String sql = "SELECT * FROM comptes WHERE numero = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, numeroCompte);
            rs = db.executeSelect();
            if (rs.next()) {
                compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                int clientId = rs.getInt("client_id");
                if (!rs.wasNull()) { // Vérifie si client_id n'est pas null
                    Client client = new Client();
                    client.setId(clientId); // Supposons que la classe Client a une méthode setId
                    compte.setClient(client); // Affecter l'objet Client au compte
                }
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compte;
    }

    @Override
    public int countComptesActif() {
        String sql = "SELECT COUNT(*) FROM comptes WHERE statut = TRUE";
        int totalCompteActif = 0;

        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();

            if (rs.next()) {
                totalCompteActif = rs.getInt(1);
            }

            rs.close();
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCompteActif;
    }

    @Override
    public int countComptesFerme() {
        String sql = "SELECT COUNT(*) FROM comptes WHERE statut = FALSE";
        int totalFerme = 0;

        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();

            if (rs.next()) {
                totalFerme = rs.getInt(1);
            }

            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalFerme;
    }

    @Override
    public int countClientsComptes() {
        String sql = "SELECT COUNT(*) FROM comptes WHERE client_id IS NOT NULL";
        int totalClientsCompte = 0;

        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();

            if (rs.next()) {
                totalClientsCompte = rs.getInt(1);
            }

            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalClientsCompte;
    }

    @Override
    public List<Compte> searchCompteByNumero(String numeroCompte) {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT * FROM comptes WHERE numero LIKE ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, "%" + numeroCompte + "%");
            ResultSet rs = db.executeSelect();

            while (rs.next()) {
                Compte compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                int clientId = rs.getInt("client_id");
                if (!rs.wasNull()) { // Vérifie si client_id n'est pas null
                    Client client = new Client();
                    client.setId(clientId); // Supposons que la classe Client a une méthode setId
                    compte.setClient(client); // Affecter l'objet Client au compte
                }
                comptes.add(compte);
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comptes;
    }
}