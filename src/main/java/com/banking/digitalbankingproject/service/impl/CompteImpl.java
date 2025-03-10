package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.tools.Notification;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CompteImpl implements ICompte {

    private Db db = new Db();

    @Override
    public boolean createCompte(Compte compte) {
        // Vérifier si le numéro de compte existe déjà
        String checkSql = "SELECT COUNT(*) FROM comptes WHERE numero = ?";
        try {
            db.initPrepar(checkSql);
            db.getPstm().setString(1, compte.getNumero());
            ResultSet rs = db.executeSelect();
            if (rs.next() && rs.getInt(1) > 0) {
                // Le numéro de compte existe déjà
                Notification.NotifError("Erreur", "Le numéro de compte existe déjà");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // Si le numéro de compte n'existe pas, procéder à l'insertion
        String sql = "INSERT INTO comptes (numero, balance, client_id, typeCompte, statut, dateOuverture) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, compte.getNumero());
            db.getPstm().setDouble(2, compte.getBalance());
            db.getPstm().setInt(3, compte.getClient().getId());
            db.getPstm().setString(4, compte.getTypeCompte());
            db.getPstm().setString(5, compte.getStatut());
            db.getPstm().setDate(6, java.sql.Date.valueOf(compte.getDateOuverture()));
            db.executeMaj();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public boolean updateCompte(Compte compte) {
        String sql = "UPDATE comptes SET numero = ?, balance = ?, client_id = ?, typeCompte = ?, statut = ?, dateOuverture = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, compte.getNumero());
            db.getPstm().setDouble(2, compte.getBalance());
            db.getPstm().setInt(3, compte.getClient().getId());
            db.getPstm().setString(4, compte.getTypeCompte());
            db.getPstm().setString(5, compte.getStatut());
            db.getPstm().setDate(6, java.sql.Date.valueOf(compte.getDateOuverture())); // Ajout de dateOuverture
            db.getPstm().setInt(7, compte.getId());
            db.executeMaj();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public boolean deleteCompte(int id) {
        String sql = "DELETE FROM comptes WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            db.executeMaj();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public List<Compte> getAllComptes() {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT * FROM comptes";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Compte compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setTypeCompte(rs.getString("typeCompte"));
                compte.setStatut(rs.getString("statut"));

                // Gestion de dateOuverture (vérification de NULL)
                java.sql.Date dateOuverture = rs.getDate("dateOuverture");
                if (dateOuverture != null) {
                    compte.setDateOuverture(dateOuverture.toLocalDate());
                } else {
                    compte.setDateOuverture(null); // Ou une valeur par défaut si nécessaire
                }

                // Récupération de l'id du client et chargement du client associé
                int clientId = rs.getInt("client_id");
                if (clientId > 0) {
                    Client client = new ClientImpl().getClientById(clientId);
                    compte.setClient(client);
                }
                comptes.add(compte);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
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
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setBalance(rs.getDouble("balance"));
                compte.setTypeCompte(rs.getString("typeCompte"));
                compte.setStatut(rs.getString("statut"));
                compte.setDateOuverture(rs.getDate("dateOuverture").toLocalDate()); // Récupération de dateOuverture
                // Récupération du client associé si nécessaire
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return compte;
    }

    @Override
    public boolean fermerCompte(int id) {
        String sql = "UPDATE comptes SET statut = 'FERME' WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            db.executeMaj();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public boolean ouvrirCompte(int id) {
        String sql = "UPDATE comptes SET statut = 'ACTIF' WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            db.executeMaj();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public boolean deposer(int compteId, double montant) {
        Compte compte = getCompteById(compteId);
        if (compte == null || "FERME".equals(compte.getStatut())) {
            Notification.NotifError("Erreur", "Le compte est fermé ou n'existe pas");
            return false;
        }

        String sql = "UPDATE comptes SET balance = balance + ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setInt(2, compteId);
            db.executeMaj();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public int countComptes() {
        String sql = "SELECT COUNT(*) FROM comptes";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            if (rs.next()) {
                return rs.getInt(1); // Retourne le nombre de comptes
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return 0;
    }

    @Override
    public boolean updateBalance(int compteId, double nouveauSolde) {
        String sql = "UPDATE comptes SET balance = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setDouble(1, nouveauSolde);
            db.getPstm().setInt(2, compteId);
            db.executeMaj();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }
}