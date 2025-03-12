package org.example.javafx.service;
import org.example.javafx.entities.Compte;
import java.util.List;

public interface ICompte {
        public int create(Compte compte);
        public List<Compte> getAllComptes();
}
