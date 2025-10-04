package View;

import Model.Employe;
import Service.EmployeService;
import enums.Secteur;
import enums.Situation_Familiale;
import enums.TypeContrat;

import java.time.LocalDate;
import java.util.Scanner;

public class EmployeView {
    private EmployeService employeService = new EmployeService();
    private Scanner scanner = new Scanner(System.in);

    public void menuEmploye(){
        while (true){
            System.out.println("\n=== GESTION DES Employeé ===");
            System.out.println("1. Créer un nouveau Employeé");
            System.out.println("2. Modifier informations de Employeé");
            System.out.println("3. Consulter profil de Employeé");
            System.out.println("4. Supprimer Employeé");
            System.out.println("0. Retour au menu principal");
            System.out.print("Choisissez une option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1:
                    ajouterEmploye();
                    break;
                case 2:
                    modifierEmploye();
                    break;
                case 3:
                    consulterProfilEmploye();
                    break;
                case 4:
                    supprimerEmploye();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Option invalide!");
            }
        }
    }

    private void ajouterEmploye() {
        System.out.println("=== Ajouter un employé ===");

        System.out.print("Nom : ");
        String nom = scanner.nextLine();

        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();

        System.out.print("Date de naissance (format: yyyy-MM-dd) : ");
        LocalDate dateNaissance = LocalDate.parse(scanner.nextLine());

        System.out.print("Ville : ");
        String ville = scanner.nextLine();

        System.out.print("Nombre d'enfants : ");
        int nombreEnfants = Integer.parseInt(scanner.nextLine());

        System.out.print("A-t-il un investissement (true/false) ? ");
        boolean investissement = Boolean.parseBoolean(scanner.nextLine());

        System.out.print("A-t-il un placement (true/false) ? ");
        boolean placement = Boolean.parseBoolean(scanner.nextLine());

        System.out.print("Situation familiale (SINGLE,MARRIED,DIVORCED) : ");
        Situation_Familiale situation = Situation_Familiale.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("ID : ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Salaire : ");
        double salaire = Double.parseDouble(scanner.nextLine());

        System.out.print("Ancienneté (en années) : ");
        int anciennete = Integer.parseInt(scanner.nextLine());

        System.out.print("Poste : ");
        String poste = scanner.nextLine();

        System.out.print("Type de contrat (CDI, CDD, STAGE, INTERIM) : ");
        TypeContrat typeContrat = TypeContrat.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Secteur(PUBLIC,GRANDE_ENTREPRISE,PME) : ");
        Secteur secteur = Secteur.valueOf(scanner.nextLine().toUpperCase());

        // Créer l'objet employé
        Employe employe = new Employe(
                0,nom, prenom, dateNaissance, ville, nombreEnfants,
                investissement, placement, situation, 0,
                 salaire, anciennete, poste, typeContrat, secteur
        );

        // Appel du service pour sauvegarder
        int generatedId = employeService.ajouterEmploye(employe);

        if (generatedId > 0) {
            System.out.println("Employé ajouté avec succès ! ID : " + generatedId);
        } else {
            System.out.println("Erreur lors de l'ajout de l'employé.");
        }
    }

    private void supprimerEmploye() {
        System.out.print("ID du employee à supprimer: ");
        int id = scanner.nextInt();

        if (employeService.deleteEmploye(id)) {
            System.out.println("employee supprimé avec succès!");
        } else {
            System.out.println("Erreur lors de la suppression ou employee introuvable.");
        }
    }

    private void modifierEmploye() {
        System.out.print("Entrez l'ID de l'employé à modifier : ");
        int id = Integer.parseInt(scanner.nextLine());


        System.out.print("Nom : ");
        String nom = scanner.nextLine();

        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();

        System.out.print("Date de naissance (yyyy-MM-dd) : ");
        LocalDate dateNaissance = LocalDate.parse(scanner.nextLine());

        System.out.print("Ville : ");
        String ville = scanner.nextLine();

        System.out.print("Nombre d'enfants : ");
        int nombreEnfants = Integer.parseInt(scanner.nextLine());

        System.out.print("Investissement (true/false) : ");
        boolean investissement = Boolean.parseBoolean(scanner.nextLine());

        System.out.print("Placement (true/false) : ");
        boolean placement = Boolean.parseBoolean(scanner.nextLine());

        System.out.print("Situation familiale (SINGLE,MARRIED,DIVORCED) : ");
        Situation_Familiale situation = Situation_Familiale.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Score : ");
        int score = Integer.parseInt(scanner.nextLine());

        System.out.print("Salaire : ");
        double salaire = Double.parseDouble(scanner.nextLine());

        System.out.print("Ancienneté : ");
        int anciennete = Integer.parseInt(scanner.nextLine());

        System.out.print("Poste : ");
        String poste = scanner.nextLine();

        System.out.print("Type de contrat (CDI,CDD,STAGE,INTERIM) : ");
        TypeContrat typeContrat = TypeContrat.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Secteur : ");
        Secteur secteur = Secteur.valueOf(scanner.nextLine().toUpperCase());

        Employe employe = new Employe(
                0,nom, prenom, dateNaissance, ville, nombreEnfants,
                investissement, placement, situation, score,
                 salaire, anciennete, poste, typeContrat, secteur
        );

        boolean success = employeService.modifierEmploye(employe);

        if (success) {
            System.out.println("Employé modifié avec succès !");
        } else {
            System.out.println("Erreur lors de la modification de l'employé.");
        }
    }

    private void consulterProfilEmploye() {
        System.out.print("Entrez l'ID de l'employé : ");
        int id = Integer.parseInt(scanner.nextLine());

        Employe employe = employeService.getEmployeById(id);

        if (employe != null) {
            System.out.println("\n=== Profil de l'employé ===");
            System.out.println("ID : " + employe.getId());
            System.out.println("Nom : " + employe.getNom());
            System.out.println("Prénom : " + employe.getPrenom());
            System.out.println("Date de naissance : " + employe.getDatedenaissance());
            System.out.println("Ville : " + employe.getVille());
            System.out.println("Nombre d'enfants : " + employe.getNombreEnfants());
            System.out.println("Investissement : " + employe.isInvestissement());
            System.out.println("Placement : " + employe.isPlacement());
            System.out.println("Situation familiale : " + employe.getSituation_Familiale());
            System.out.println("Score : " + employe.getScore());
            System.out.println("Salaire : " + employe.getSalaire());
            System.out.println("Ancienneté : " + employe.getAncienneté());
            System.out.println("Poste : " + employe.getPoste());
            System.out.println("Type de contrat : " + employe.getTypeContrat());
            System.out.println("Secteur : " + employe.getSecteur());
        } else {
            System.out.println("Employé introuvable avec l'ID " + id);
        }
    }



}
