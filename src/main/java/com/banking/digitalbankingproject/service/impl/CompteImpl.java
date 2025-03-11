package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.ICompte;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {
    private final Db db;

    public CompteImpl() {
        this.db = new Db();
    }

    @Override
    public boolean createCompte(Compte compte) {
        String sql = "INSERT INTO comptes (numero, balance, created_at, client_id) VALUES (?, ?, NOW(), ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, compte.getNumero());
            db.getPstm().setDouble(2, compte.getBalance());
            db.getPstm().setInt(3, compte.getClientId());
            return db.executeMaj() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeStatement();
        }
    }

    @Override
    public List<Compte> getAllComptes() {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT c.*, cl.nom, cl.prenom FROM comptes c JOIN clients cl ON c.client_id = cl.id";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                comptes.add(extractCompteFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement();
        }
        return comptes;
    }

    @Override
    public Compte getCompteByNumero(String numero) {
        String sql = "SELECT c.*, cl.nom, cl.prenom FROM comptes c JOIN clients cl ON c.client_id = cl.id WHERE c.numero = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, numero);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                return extractCompteFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement();
        }
        return null;
    }

    @Override
    public Compte getCompteById(int id) {
        String sql = "SELECT c.*, cl.nom, cl.prenom FROM comptes c JOIN clients cl ON c.client_id = cl.id WHERE c.id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                return extractCompteFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement();
        }
        return null;
    }

    @Override
    public boolean updateCompte(Compte compte) {
        String sql = "UPDATE comptes SET numero = ?, balance = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, compte.getNumero());
            db.getPstm().setDouble(2, compte.getBalance());
            db.getPstm().setInt(3, compte.getId());
            return db.executeMaj() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeStatement();
        }
    }

    @Override
    public boolean deleteCompte(int id) {
        String sql = "DELETE FROM comptes WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            return db.executeMaj() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeStatement();
        }
    }

    @Override
    public List<Compte> getComptesByClientId(int clientId) {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT c.*, cl.nom, cl.prenom FROM comptes c JOIN clients cl ON c.client_id = cl.id WHERE c.client_id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, clientId);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                comptes.add(extractCompteFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement();
        }
        return comptes;
    }

    @Override
    public boolean updateSolde(int compteId, double nouveauSolde) {
        String sql = "UPDATE comptes SET balance = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setDouble(1, nouveauSolde);
            db.getPstm().setInt(2, compteId);
            return db.executeMaj() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeStatement();
        }
    }

    private Compte extractCompteFromResultSet(ResultSet rs) throws SQLException {
        Compte compte = new Compte();
        compte.setId(rs.getInt("id"));
        compte.setNumero(rs.getString("numero"));
        compte.setBalance(rs.getDouble("balance"));
        compte.setClientId(rs.getInt("client_id"));
        compte.setCreatedAt(rs.getTimestamp("created_at").toInstant());
        
        Client client = new Client();
        client.setId(rs.getInt("client_id"));
        client.setNom(rs.getString("nom"));
        client.setPrenom(rs.getString("prenom"));
        compte.setClient(client);
        
        return compte;
    }
}
