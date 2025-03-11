package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.database.Db;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {
    private Db db = new Db();

    @Override
    public void createCompte(Compte compte) {
        String sql = "INSERT INTO comptes (numero, balance, actif, created_at, client_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, compte.getNumero());
            ps.setDouble(2, compte.getBalance());
            ps.setBoolean(3, compte.isActif());
            ps.setTimestamp(4, Timestamp.from(compte.getCreatedAt()));
            if(compte.getClient() != null){
                ps.setInt(5, compte.getClient().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCompte(Compte compte) {
        String sql = "UPDATE comptes SET balance = ?, actif = ?, client_id = ? WHERE numero = ?";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, compte.getBalance());
            ps.setBoolean(2, compte.isActif());
            if(compte.getClient() != null){
                ps.setInt(3, compte.getClient().getId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setString(4, compte.getNumero());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerCompte(Compte compte) {
        String sql = "DELETE FROM comptes WHERE numero = ?";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, compte.getNumero());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Compte> getAllComptes() {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT c.*, cl.id as client_id, cl.nom, cl.prenom, cl.email FROM comptes c LEFT JOIN clients cl ON c.client_id = cl.id";
        try (Connection con = db.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while(rs.next()){
                Compte compte = new Compte();
                compte.setNumero(rs.getString("numero"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setActif(rs.getBoolean("actif"));
                compte.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                int clientId = rs.getInt("client_id");
                if(clientId > 0) {
                    com.banking.digitalbankingproject.entity.Client client = new com.banking.digitalbankingproject.entity.Client();
                    client.setId(clientId);
                    client.setNom(rs.getString("nom"));
                    client.setPrenom(rs.getString("prenom"));
                    client.setEmail(rs.getString("email"));
                    compte.setClient(client);
                }
                comptes.add(compte);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comptes;
    }

    @Override
    public List<Operation> getOperations(Compte compte) {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT * FROM operations WHERE compte_numero = ?";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, compte.getNumero());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Operation op = new Operation();
                op.setDate(rs.getTimestamp("date").toLocalDateTime());
                op.setDescription(rs.getString("description"));
                op.setMontant(rs.getDouble("montant"));
                op.setSolde(rs.getDouble("solde"));
                operations.add(op);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operations;
    }

    @Override
    public void virement(Compte source, Compte destinataire, double montant) {
        if(source.getBalance() < montant) {
            throw new IllegalArgumentException("Solde insuffisant pour le virement.");
        }
        source.setBalance(source.getBalance() - montant);
        destinataire.setBalance(destinataire.getBalance() + montant);
        updateCompte(source);
        updateCompte(destinataire);
        // Enregistrement optionnel des opérations
    }
}
