## Application de Gestion Bancaire

Une application de gestion bancaire moderne développée avec JavaFX (JDK 17), permettant la gestion des clients, des comptes et des opérations bancaires.

📋 Fonctionnalités

Gestion des Clients

- Création de nouveaux clients
- Modification des informations clients
- Consultation de la liste des clients
- Recherche de clients


Gestion des Comptes

- Création de comptes (Courant, Épargne)
- Association de comptes aux clients
- Consultation des soldes
- Historique des comptes


Opérations Bancaires

- Dépôts
- Retraits
- Virements entre comptes
- Génération de relevés bancaires



🔧 Prérequis

- JDK 17 ou supérieur
- Maven 3.8.x ou supérieur
- Une base de données MySQL 5.0 ou supérieur

🚀 Installation

Cloner le repository

    git clone -b develop https://github.com/abdoulayegaye/digital_banking.git
    cd digital_banking

Configurer la base de données

Dans le package database, vous y trouverez la base de données MySQL que vous importiez.

📁 Structure du Projet

      src/
      ├── main/
      │   ├── java/
      │   │   ├── com/
      │   │   │   ├── banking/
      │   │   │   │   ├── controller/
      │   │   │   │   ├── database/
      │   │   │   │   ├── entity/
      │   │   │   │   ├── enums/
      │   │   │   │   ├── service/
      │   │   │   │   ├── tools/
      │   │   │   │   └── App.java
      │   │   │   
      │   ├── resources/
      │   │   ├── fxml/
      │   │   ├── css/
      │   │   ├── images/

🛠️ Technologies Utilisées

    JavaFX 17 - Framework d'interface utilisateur
    Maven - Gestion des dépendances et build
    MySQL - Base de données
    Lombok - Dépendance de génération des constructeurs, getters, setters, ...
    Scene Builder - Création des interafces
    TrayNotification - Dépendance pour les notifications
    BCrypt - Dépendance pour le hashage des mots de passe

📱 Captures d'écran

À venir

📖 Documentation

L'application suit une architecture MVC (Model-View-Controller) avec les composants suivants :

    entity : Représentation des entités métier (Client, Compte, Operation)
    fxml : Interfaces FXML pour l'UI
    controller : Logique de contrôle et gestion des événements
    service : Logique métier
    impl : Accès aux données

Guides d'utilisation

Gestion des Clients

    Pour créer un nouveau client : Menu → Clients → Nouveau Client
    Pour modifier un client : Sélectionner le client → Clic droit → Modifier
    Pour rechercher : Utiliser la barre de recherche en haut de la liste des clients

Gestion des Comptes

    Pour créer un compte : Sélectionner un client → Menu → Comptes → Nouveau Compte
    Pour effectuer une opération : Sélectionner le compte → Opérations → Choisir l'opération

🤝 Contribution

    Fork le projet

    Créer une branche pour votre fonctionnalité
    
    git checkout -b feature/nouvelle-fonctionnalite
    
    Commit vos changements
    
    git commit -m "Ajout d'une nouvelle fonctionnalité"
    
    Push vers la branche
    
    git push origin feature/nouvelle-fonctionnalite
    
    Ouvrir une Pull Request

📝 License

Ce projet est sous licence MIT - voir le fichier LICENSE.md pour plus de détails.

👥 Auteurs

Abdoulaye GAYE - Développement initial - abdoulayegaye

🙏 Remerciements

L'équipe JavaFX pour leur excellent framework
Tous les contributeurs (les étudiants de L3IAGE et L3GDA) qui participent à l'amélioration de ce projet
