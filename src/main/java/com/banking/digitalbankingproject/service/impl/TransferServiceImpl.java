package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.ITransferService;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.database.Db;

import java.time.Instant;

public class TransferServiceImpl implements ITransferService {

    private IOperation operationDao = new OperationImpl();
    private Db db = new Db();

    @Override
    public boolean transferer(Compte source, Compte destination, double montant) {
        try {
            System.out.println("Début de la transaction");
            db.beginTransaction();

            if (source.getBalance() < montant) {
                Notification.NotifError("Erreur", "Solde insuffisant pour le virement");
                db.rollbackTransaction(); // Annuler la transaction
                System.out.println("Annulation de la transaction : solde insuffisant");
                return false;
            }

            Operation operation = new Operation();
            operation.setDateOp(Instant.now());
            operation.setAmount(montant);
            operation.setType(TypeOperation.VIREMENT);
            operation.setCompte(source); // Compte source
            operation.setCompteDestination(destination); // Compte destination

            source.setBalance(source.getBalance() - montant);
            updateCompteBalanceInDatabase(source);

            destination.setBalance(destination.getBalance() + montant);
            updateCompteBalanceInDatabase(destination);

            if (operationDao.createOperation(operation)) {
                db.commitTransaction();
                Notification.NotifSuccess("Succès", "Virement effectué avec succès");
                return true;
            } else {
                db.rollbackTransaction();
                Notification.NotifError("Erreur", "Échec du virement");
                System.out.println("Annulation de la transaction : échec de l'opération");
                return false;
            }
        } catch (Exception e) {
            db.rollbackTransaction();
            e.printStackTrace();
            Notification.NotifError("Erreur", "Une erreur s'est produite lors du virement : " + e.getMessage());
            System.out.println("Annulation de la transaction : exception");
            return false;
        }
    }

    private void updateCompteBalanceInDatabase(Compte compte) {
        String sqlUpdate = "UPDATE comptes SET balance = ? WHERE id = ?";
        try {
            db.initPrepar(sqlUpdate);
            db.getPstm().setDouble(1, compte.getBalance());
            db.getPstm().setInt(2, compte.getId());
            db.executeMaj();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}