#  Système de Scoring Automatisé – Micro-Finance Maroc

##  Contexte du projet

Le secteur de la **micro-finance marocaine** fait face à plusieurs défis dans l’évaluation du **risque crédit** :
- Processus manuels et longs
- Décisions parfois subjectives
- Exclusion de profils pourtant solvables

Les méthodes traditionnelles ne répondent plus aux besoins de **rapidité**, **précision** et **traçabilité** du marché moderne.

 **Objectif du projet**  
Développer une **application Java** qui automatise le processus de scoring crédit grâce à :
- Un **algorithme de scoring** basé sur 5 composants métier
- Un **moteur de décision automatique**
- Un **système d’historisation** complet pour audit
- Une **analyse intelligente** des clients pour réduire les risques et améliorer l’accès au financement

---

##  Architecture du projet

Le projet est structuré en **4 couches principales** :


**Design Patterns utilisés :**
- Singleton (connexion à la base)
- Repository Pattern (accès aux données)

---

##  Exigences techniques

- **Langage :** Java 8
- **Base de données :** MySQL
- **Connexion :** JDBC
- **Concepts Java :** Collections, Stream API, Optional, Enum, LocalDate (Java Time API)
- **Paradigmes :** Programmation orientée objet (héritage, abstraction)
- **Validation :** Contrôle des données d’entrée utilisateur

---

##  Fonctionnalités principales

###  MODULE 1 : Gestion des Clients

- Créer un nouveau client
- Modifier les informations d’un client
- Consulter le profil d’un client
- Supprimer un client
- Lister tous les clients

**Classes principales :**
- `Personne` *(abstraite)* : nom, prénom, dateNaissance, ville, nombreEnfants, investissement, placement, situationFamiliale, createdAt, score
- `Employe` : salaire, ancienneté, poste, typeContrat, secteur (public, grande entreprise, PME)
- `Professionnel` : revenu, immatriculationFiscale, secteurActivité (agriculture, service, commerce, construction…), activité

---

###  MODULE 2 : Calcul du Score

Algorithme de scoring automatique basé sur :
- Stabilité professionnelle
- Capacité financière
- Historique de paiement
- Relation client
- Patrimoine / critères complémentaires

**Seuils d’éligibilité :**

| Type client | Seuil minimum |
|--------------|----------------|
| Nouveau client | 70/100 |
| Client existant | 60/100 |

**Capacité d’emprunt :**
- Nouveau client : *4x salaire*
- Client existant :
    - *7x salaire* si score entre 60 et 80
    - *10x salaire* si score > 80

**Classe :**
- `Credit` : dateCredit, montantDemande, montantOctroye, tauxInteret, dureeEnMois, typeCredit, décision (Enum : ACCORD_IMMEDIAT, ETUDE_MANUELLE, REFUS_AUTOMATIQUE)

---

###  MODULE 3 : Gestion de l’Historique de Paiement

- Génération automatique des échéances
- Suivi des paiements (à temps, retard, impayé)
- Régularisation des impayés
- Application de **pénalités** ou **bonus** sur le score

**Classes :**
- `Echeance` : dateÉchéance, mensualité, datePaiement, statutPaiement
- `Incident` : dateIncident, typeIncident (PAYE_A_TEMPS, EN_RETARD, IMPAYE_RÉGLÉ, IMPAYE_NON_RÉGLÉ), scoreImpact

---

###  MODULE 4 : Moteur de Décision Automatique

Décision basée sur le score :
- **Score ≥ 80** → Accord immédiat
- **Score 60–79** → Étude manuelle
- **Score < 60** → Refus automatique

---

###  MODULE 5 : Analytics & Requêtes Avancées

**Exemples de cas d’usage :**

 **Recherche multi-critères :**
- Clients éligibles au crédit immobilier :
    - Âge : 25–50 ans
    - Revenu > 4000 DH
    - CDI uniquement
    - Score > 70
    - Marié

 **Clients à risque (suivi prioritaire) :**
- Score < 60
- Incidents récents (<6 mois)
- Triés par score décroissant

 **Statistiques :**
- Répartition par type d’emploi
- Score moyen / taux d’approbation par catégorie
- Tri par score, revenu ou ancienneté

---

##  Exemple d’Analyse Business

**Campagne de crédit consommation :**
- Cible : clients avec score 65–85
- Revenus : 4000–8000 DH
- Âge : 28–45 ans
- Sans crédit en cours

 **Résultat :** 1247 clients identifiés → Envoi campagne SMS/Email

---

##  Technologies & Outils

| Outil / Librairie               | Rôle |
|---------------------------------|------|
| Java 8                          | Langage principal |
| JDBC                            | Connexion base de données |
| MySQL                           | Base de données relationnelle |
| Java Time API                   | Gestion des dates |
| Enum                            | Gestion des statuts & types |
| Stream / Optional / Collections | Traitement de données efficace |
| Pattern Singleton               | Connexion base unique |
| Pattern Repository              | Abstraction de la couche DAO |

---

## 📂 Organisation des packages

```bash
src/
├── DAO/
│   ├── CreditDAO.java
│   ├── EcheanceDAO.java
│   ├── EmployeDAO.java
│   ├── IncidentDAO.java
│   └── ProfessionnelDAO.java
│
├── Database/
│   └── Database.java
│
├── enums/
│   ├── Activite.java
│   ├── Decision.java
│   ├── Secteur.java
│   ├── SecteurActivite.java
│   ├── Situation_Familiale.java
│   ├── StatutPaiement.java
│   ├── TypeContrat.java
│   ├── TypeCredit.java
│   └── TypeIncident.java
│
├── Model/
│   ├── Credit.java
│   ├── Echeance.java
│   ├── Employe.java
│   ├── Incident.java
│   ├── Personne.java
│   └── Professionnel.java
│
├── Service/
│   ├── AnalyticsService.java
│   ├── CreditService.java
│   ├── DecisionService.java
│   ├── EcheanceService.java
│   ├── EmployeService.java
│   ├── ProfessionnelService.java
│   ├── ScoringService.java
│   └── StatistiquesEmploye.java
│
├── View/
│   ├── AnalyticsView.java
│   ├── CreditView.java
│   ├── EmployeView.java
│   ├── PaiementView.java
│   └── ProfessionnelView.java
│
└── Main.java
