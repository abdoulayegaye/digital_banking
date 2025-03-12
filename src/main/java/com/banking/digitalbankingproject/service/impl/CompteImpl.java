package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CompteImpl implements ICompte {

    private static final Logger logger = Logger.getLogger(CompteImpl.class.getName());
    private Db db = new Db();
    private int ok;

    @Override
    public boolean createCompte(Compte compte) {
        if (compte.getSolde() < 0) {
            logger.warning("Le solde du compte ne peut pas être négatif");
            return false;
        }
        if (compte.getClientId() <= 0) {
            logger.warning("L'ID du client est invalide");
            return false;
        }

        String sql = "INSERT INTO comptes VALUES(NULL, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setDouble(1, compte.getSolde());
            db.getPstm().setInt(2, compte.getClientId());
            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            logger.severe("Erreur lors de la création du compte : " + e.getMessage());
            return false;
        }
        return ok == 1;
    }

    @Override
    public List<Compte> getAllComptes() {
        String sql = "SELECT * FROM comptes ORDER BY id ASC";
        List<Compte> comptes = new ArrayList<>();
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Compte compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setSolde(rs.getDouble("solde"));
                compte.setClientId(rs.getInt("clientId"));
                comptes.add(compte);
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération des comptes : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return comptes;
    }

    @Override
    public Compte getCompteById(int id) {
        String sql = "SELECT * FROM comptes WHERE id = ?";
        Compte compte = null;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setSolde(rs.getDouble("solde"));
                compte.setClientId(rs.getInt("clientId"));
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération du compte : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return compte;
    }

    @Override
    public boolean updateCompte(Compte compte) {
        if (compte.getSolde() < 0) {
            logger.warning("Le solde du compte ne peut pas être négatif");
            return false;
        }
        if (compte.getClientId() <= 0) {
            logger.warning("L'ID du client est invalide");
            return false;
        }

        String sql = "UPDATE comptes SET solde = ?, clientId = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setDouble(1, compte.getSolde());
            db.getPstm().setInt(2, compte.getClientId());
            db.getPstm().setInt(3, compte.getId());
            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            logger.severe("Erreur lors de la mise à jour du compte : " + e.getMessage());
            return false;
        }
        return ok == 1;
    }

    @Override
    public boolean deleteCompte(int id) {
        String sql = "DELETE FROM comptes WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception e) {
            logger.severe("Erreur lors de la suppression du compte : " + e.getMessage());
            return false;
        }
        return ok == 1;
    }
    
    @Override
    public List<Compte> getComptesByClientId(int clientId) {
        String sql = "SELECT * FROM comptes WHERE clientId = ? ORDER BY id ASC";
        List<Compte> comptes = new ArrayList<>();
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, clientId);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Compte compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setSolde(rs.getDouble("solde"));
                compte.setClientId(rs.getInt("clientId"));
                comptes.add(compte);
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération des comptes du client : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return comptes;
    }
}