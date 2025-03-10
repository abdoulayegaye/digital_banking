package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {
    
    private Db db = new Db();
    private ResultSet rs;
    private int ok;

    @Override
    public int save(Compte compte) throws Exception {
        String sql = "INSERT INTO comptes (numero, balance, created_at, client_id) VALUES (?, ?, ?, ?)";
        try {
            if (compte.getNumero() == null || compte.getNumero().trim().isEmpty()) {
                throw new Exception("Le numéro de compte est obligatoire");
            }
            if (compte.getClient() == null || compte.getClient().getId() <= 0) {
                throw new Exception("Le client est obligatoire");
            }
            if (compte.getSolde() < 0) {
                throw new Exception("Le solde initial doit être positif");
            }

            db.initPrepar(sql);
            db.getPstm().setString(1, compte.getNumero().trim());
            db.getPstm().setDouble(2, compte.getSolde());
            db.getPstm().setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
            db.getPstm().setInt(4, compte.getClient().getId());
            
            ok = db.executeMaj();
            db.closeConnection();
            return ok;
        } catch (Exception e) {
            throw new Exception("Erreur lors de la création du compte : " + e.getMessage());
        }
    }

    @Override
    public int delete(String numero) throws Exception {
        String sql = "DELETE FROM comptes WHERE numero = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, numero);
            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            throw new Exception("Erreur lors de la fermeture du compte : " + e.getMessage());
        }
        return ok;
    }

    @Override
    public List<Compte> getAll() throws Exception {
        String sql = "SELECT c.*, cl.nom, cl.prenom, cl.email FROM comptes c " +
                    "JOIN clients cl ON c.client_id = cl.id ORDER BY c.created_at DESC";
        List<Compte> comptes = new ArrayList<>();
        try {
            System.out.println("Exécution de la requête : " + sql);
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                
                Compte compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setSolde(rs.getDouble("balance"));
                java.sql.Timestamp timestamp = rs.getTimestamp("created_at");
                compte.setDateCreation(timestamp != null ? timestamp.toInstant() : Instant.now());
                compte.setClient(client);
                comptes.add(compte);
                System.out.println("Compte chargé : " + compte.getNumero() + " - Client : " + client.getNom());
            }
            System.out.println("Nombre total de comptes chargés : " + comptes.size());
            db.closeConnection();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des comptes : " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Erreur lors de la récupération des comptes : " + e.getMessage());
        }
        return comptes;
    }

    @Override
    public Compte getByNumero(String numero) throws Exception {
        String sql = "SELECT c.*, cl.nom, cl.prenom, cl.email FROM comptes c " +
                    "JOIN clients cl ON c.client_id = cl.id WHERE c.numero = ?";
        Compte compte = null;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, numero);
            rs = db.executeSelect();
            if (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                
                compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setSolde(rs.getDouble("balance"));
                java.sql.Timestamp timestamp = rs.getTimestamp("created_at");
                compte.setDateCreation(timestamp != null ? timestamp.toInstant() : Instant.now());
                compte.setClient(client);
            }
            db.closeConnection();
        } catch (Exception e) {
            throw new Exception("Erreur lors de la récupération du compte : " + e.getMessage());
        }
        return compte;
    }

    @Override
    public int updateSolde(String numero, double montant) throws Exception {
        String sql = "UPDATE comptes SET balance = balance + ? WHERE numero = ?";
        try {
            System.out.println("Mise à jour du solde du compte " + numero + " avec montant " + montant);
            System.out.println("Exécution de la requête : " + sql);
            
            db.initPrepar(sql);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setString(2, numero);
            ok = db.executeMaj();
            
            System.out.println("Résultat de la mise à jour du solde : " + ok);
            
            if (ok > 0) {
                // Vérifier que le solde a bien été mis à jour
                Compte compte = getByNumero(numero);
                System.out.println("Nouveau solde du compte " + numero + " : " + compte.getSolde());
            } else {
                System.err.println("ATTENTION: Le solde du compte " + numero + " n'a pas été mis à jour");
            }
            
            db.closeConnection();
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du solde : " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Erreur lors de la mise à jour du solde : " + e.getMessage());
        }
        return ok;
    }
}
