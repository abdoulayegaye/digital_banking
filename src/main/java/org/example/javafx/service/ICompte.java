package org.example.javafx.service;
import org.example.javafx.entities.Compte;
import java.util.List;

public interface ICompte {
        public int create(Compte compte);
        public double getSolde(int compteId);

        public int closeAccount(int compteId);

        // Génération de relevés bancaires en PDF
        public byte[] generateBankStatement(int compteId);

        // Obtenir tous les comptes d'un client
        public List<Compte> getAllComptes();
}
