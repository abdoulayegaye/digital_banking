package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {
    private final Db db = new Db();

    @Override
    public boolean createCompte(Compte compte) {
        try {
            db.initPrepar("INSERT INTO comptes (numero, balance, client_id) VALUES (?, ?, ?)");
            db.getPstm().setString(1, compte.getNumero());
            db.getPstm().setDouble(2, compte.getBalance());
            db.getPstm().setInt(3, compte.getClientId());
            
            return db.executeMaj() != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public boolean updateCompte(Compte compte) {
        try {
            db.initPrepar("UPDATE comptes SET numero = ?, balance = ?, client_id = ? WHERE id = ?");
            db.getPstm().setString(1, compte.getNumero());
            db.getPstm().setDouble(2, compte.getBalance());
            db.getPstm().setInt(3, compte.getClientId());
            db.getPstm().setInt(4, compte.getId());
            
            return db.executeMaj() != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public boolean deleteCompte(int id) {
        try {
            db.initPrepar("DELETE FROM comptes WHERE id = ?");
            db.getPstm().setInt(1, id);
            
            return db.executeMaj() != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public Compte getCompteById(int id) {
        try {
            db.initPrepar("SELECT c.*, cl.nom, cl.prenom, cl.email FROM comptes c JOIN clients cl ON c.client_id = cl.id WHERE c.id = ?");
            db.getPstm().setInt(1, id);
            
            ResultSet rs = db.executeSelect();
            if (rs != null && rs.next()) {
                return extractCompteFromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return null;
    }

    @Override
    public Compte getCompteByNumero(String numero) {
        try {
            db.initPrepar("SELECT c.*, cl.nom, cl.prenom, cl.email FROM comptes c JOIN clients cl ON c.client_id = cl.id WHERE c.numero = ?");
            db.getPstm().setString(1, numero);
            
            ResultSet rs = db.executeSelect();
            if (rs != null && rs.next()) {
                return extractCompteFromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return null;
    }

    @Override
    public List<Compte> getAllComptes() {
        List<Compte> comptes = new ArrayList<>();
        try {
            db.initPrepar("SELECT c.*, cl.nom, cl.prenom, cl.email FROM comptes c JOIN clients cl ON c.client_id = cl.id");
            
            ResultSet rs = db.executeSelect();
            if (rs != null) {
                while (rs.next()) {
                    comptes.add(extractCompteFromResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return comptes;
    }

    @Override
    public List<Compte> getComptesByClientId(int clientId) {
        List<Compte> comptes = new ArrayList<>();
        try {
            db.initPrepar("SELECT c.*, cl.nom, cl.prenom, cl.email FROM comptes c JOIN clients cl ON c.client_id = cl.id WHERE c.client_id = ?");
            db.getPstm().setInt(1, clientId);
            
            ResultSet rs = db.executeSelect();
            if (rs != null) {
                while (rs.next()) {
                    comptes.add(extractCompteFromResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return comptes;
    }

    private Compte extractCompteFromResultSet(ResultSet rs) throws Exception {
        Compte compte = new Compte();
        compte.setId(rs.getInt("id"));
        compte.setNumero(rs.getString("numero"));
        compte.setBalance(rs.getDouble("balance"));
        compte.setCreatedAt(rs.getTimestamp("created_at"));
        compte.setClientId(rs.getInt("client_id"));
        
        // Créer et remplir l'objet Client
        Client client = new Client();
        client.setId(rs.getInt("client_id"));
        client.setNom(rs.getString("nom"));
        client.setPrenom(rs.getString("prenom"));
        client.setEmail(rs.getString("email"));
        compte.setClient(client);
        
        return compte;
    }
}
