package com.example.digital_banking.services;

import com.example.digital_banking.entities.Client;
import com.example.digital_banking.entities.Compte;
import com.example.digital_banking.entities.Operation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.util.Optional;

public class BanqueService {
    private ObservableList<Client> clients = FXCollections.observableArrayList();
    private ObservableList<Compte> comptes = FXCollections.observableArrayList();
    private ObservableList<Operation> operations = FXCollections.observableArrayList();

    // Gestion des clients
    public void ajouterClient(Client client) {
        clients.add(client);
    }

    public void modifierClient(Client client) {
        Optional<Client> clientExistant = clients.stream()
                .filter(c -> c.getId().equals(client.getId()))
                .findFirst();
        
        if (clientExistant.isPresent()) {
            int index = clients.indexOf(clientExistant.get());
            clients.set(index, client);
        }
    }

    public void supprimerClient(Client client) {
        clients.remove(client);
    }

    public ObservableList<Client> getClients() {
        return clients;
    }

    // Gestion des comptes
    public void creerCompte(Compte compte) {
        comptes.add(compte);
    }

    public void fermerCompte(Compte compte) {
        comptes.remove(compte);
    }

    public ObservableList<Compte> getComptes() {
        return comptes;
    }

    // Gestion des opérations
    public void effectuerDepot(Compte compte, double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant du dépôt doit être positif");
        }

        compte.setSolde(compte.getSolde() + montant);
        Operation operation = new Operation(montant, "DEPOT", compte);
        operations.add(operation);
    }

    public void effectuerRetrait(Compte compte, double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant du retrait doit être positif");
        }
        if (compte.getSolde() < montant) {
            throw new IllegalArgumentException("Solde insuffisant");
        }

        compte.setSolde(compte.getSolde() - montant);
        Operation operation = new Operation(montant, "RETRAIT", compte);
        operations.add(operation);
    }

    public void effectuerVirement(Compte source, Compte destination, double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant du virement doit être positif");
        }
        if (source.getSolde() < montant) {
            throw new IllegalArgumentException("Solde insuffisant");
        }

        source.setSolde(source.getSolde() - montant);
        destination.setSolde(destination.getSolde() + montant);

        Operation operationDebit = new Operation(montant, "VIREMENT_DEBIT", source);
        operationDebit.setCompteDestination(destination);
        operations.add(operationDebit);

        Operation operationCredit = new Operation(montant, "VIREMENT_CREDIT", destination);
        operationCredit.setCompteDestination(source);
        operations.add(operationCredit);
    }

    public ObservableList<Operation> getOperations() {
        return operations;
    }

    public ObservableList<Operation> getOperationsCompte(Compte compte) {
        ObservableList<Operation> operationsCompte = FXCollections.observableArrayList();
        operations.stream()
                .filter(op -> op.getCompte().equals(compte))
                .forEach(operationsCompte::add);
        return operationsCompte;
    }
} 