# Système de Gestion Bancaire

Une application JavaFX pour la gestion des clients, des comptes et des opérations bancaires.

## Fonctionnalités

### Module de Gestion des Clients
- Création d'un nouveau client
- Modification des informations client
- Consultation des informations client
- Suppression d'un client
- Recherche de clients

### Module de Gestion des Comptes
- Création d'un nouveau compte
- Association d'un compte à un client
- Consultation des soldes
- Fermeture de compte
- Filtrage des comptes par client

### Module des Opérations
- Dépôt
- Retrait
- Virement entre comptes
- Consultation de l'historique des transactions
- Génération de relevés bancaires en PDF

## Architecture

L'application est construite selon une architecture en couches :

1. **Couche Présentation** : Interfaces utilisateur JavaFX (fichiers FXML et contrôleurs)
2. **Couche Service** : Interfaces et implémentations des services métier
3. **Couche Entité** : Classes modèles représentant les données
4. **Couche Persistance** : Gestion de la base de données

## Technologies utilisées

- Java 11+
- JavaFX pour l'interface graphique
- CSS pour le style
- Base de données (à définir selon l'implémentation)

## Installation et exécution

### Prérequis
- Java JDK 11 ou supérieur
- Maven

### Étapes d'installation
1. Cloner le dépôt
```
git clone https://github.com/votre-utilisateur/digital-banking.git
```

2. Naviguer vers le répertoire du projet
```
cd digital-banking
```

3. Compiler le projet avec Maven
```
mvn clean install
```

4. Exécuter l'application
```
mvn javafx:run
```

## Captures d'écran

(À ajouter des captures d'écran de l'application)

## Auteurs

- Votre nom

## Licence

Ce projet est sous licence MIT - voir le fichier LICENSE pour plus de détails.
