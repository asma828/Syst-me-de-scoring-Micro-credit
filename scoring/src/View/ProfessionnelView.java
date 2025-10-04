package View;

import Model.Professionnel;
import Service.ProfessionnelService;
import enums.Secteur;
import enums.Secteuractivite;
import enums.Situation_Familiale;

import java.time.LocalDate;
import java.util.Scanner;

public class ProfessionnelView {
    private ProfessionnelService service = new ProfessionnelService();
    private Scanner scanner = new Scanner(System.in);

    public void menuProfessionnel() {
        while (true) {
            System.out.println("\n=== GESTION DES PROFESSIONNELS ===");
            System.out.println("1. Créer un nouveau professionnel");
            System.out.println("2. Modifier informations du professionnel");
            System.out.println("3. Consulter profil du professionnel");
            System.out.println("4. Supprimer professionnel");
            System.out.println("0. Retour au menu principal");
            System.out.print("Choisissez une option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    ajouterProfessionnel();
                    break;
                case 2:
                    modifierProfessionnel();
                    break;
                case 3:
                    consulterProfilProfessionnel();
                    break;
                case 4:
                    supprimerProfessionnel();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Option invalide !");
            }
        }
    }

    private void ajouterProfessionnel() {
        System.out.println("\n=== Ajouter un professionnel ===");

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

        System.out.print("Situation familiale (SINGLE, MARRIED, DIVORCED) : ");
        Situation_Familiale situation = Situation_Familiale.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Revenu : ");
        double revenu = Double.parseDouble(scanner.nextLine());

        System.out.print("Immatriculation fiscale : ");
        String immatriculation = scanner.nextLine();

        System.out.print("Secteur d'activité : ");
        Secteuractivite secteur = Secteuractivite.valueOf(scanner.nextLine().toUpperCase());


        System.out.print("Activité : ");
        String activite = scanner.nextLine();


        Professionnel p = new Professionnel(
                0,nom, prenom, dateNaissance, ville, nombreEnfants,
                investissement, placement, situation,
                0, revenu, immatriculation, secteur, activite
        );

        int id = service.ajouterProfessionnel(p);

        if (id > 0) {
            System.out.println("Professionnel ajouté avec succès ! ID : " + id);
        } else {
            System.out.println("Erreur lors de l'ajout du professionnel.");
        }
    }

    private void modifierProfessionnel() {
        System.out.print("Entrez l'ID du professionnel à modifier : ");
        int id = Integer.parseInt(scanner.nextLine());

        Professionnel p = service.getProfessionnelById(id);
        if (p == null) {
            System.out.println("Professionnel introuvable !");
            return;
        }

        System.out.println("=== Modifier les informations (laisser vide pour ne pas changer) ===");

        System.out.print("Nom (" + p.getNom() + ") : ");
        String nom = scanner.nextLine();
        if (!nom.isEmpty()) p.setNom(nom);

        System.out.print("Prénom (" + p.getPrenom() + ") : ");
        String prenom = scanner.nextLine();
        if (!prenom.isEmpty()) p.setPrenom(prenom);

        System.out.print("Date de naissance (" + p.getDatedenaissance() + ") : ");
        String dateStr = scanner.nextLine();
        if (!dateStr.isEmpty()) p.setDatedenaissance(LocalDate.parse(dateStr));

        System.out.print("Ville (" + p.getVille() + ") : ");
        String ville = scanner.nextLine();
        if (!ville.isEmpty()) p.setVille(ville);

        System.out.print("Nombre d'enfants (" + p.getNombreEnfants() + ") : ");
        String nbEnfantsStr = scanner.nextLine();
        if (!nbEnfantsStr.isEmpty()) p.setNombreEnfants(Integer.parseInt(nbEnfantsStr));

        System.out.print("Investissement (" + p.isInvestissement() + ") : ");
        String invStr = scanner.nextLine();
        if (!invStr.isEmpty()) p.setInvestissement(Boolean.parseBoolean(invStr));

        System.out.print("Placement (" + p.isPlacement() + ") : ");
        String placeStr = scanner.nextLine();
        if (!placeStr.isEmpty()) p.setPlacement(Boolean.parseBoolean(placeStr));

        System.out.print("Situation familiale (" + p.getSituation_Familiale() + ") : ");
        String sitStr = scanner.nextLine();
        if (!sitStr.isEmpty()) p.setSituation_Familiale(Situation_Familiale.valueOf(sitStr.toUpperCase()));

        System.out.print("Score (" + p.getScore() + ") : ");
        String scoreStr = scanner.nextLine();
        if (!scoreStr.isEmpty()) p.setScore(Integer.parseInt(scoreStr));

        System.out.print("Revenu (" + p.getRevenu() + ") : ");
        String revenuStr = scanner.nextLine();
        if (!revenuStr.isEmpty()) p.setRevenu(Double.parseDouble(revenuStr));

        System.out.print("Immatriculation fiscale (" + p.getImmatriculationfiscale() + ") : ");
        String immatStr = scanner.nextLine();
        if (!immatStr.isEmpty()) p.setImmatriculationfiscale(immatStr);

        System.out.print("Secteur d'activité (AGRICULTURE, SERVICR, COMMERCE, CONSTRUCTION) : ");
        Secteuractivite secteur = Secteuractivite.valueOf(scanner.nextLine().toUpperCase());
        p.setSecteuractivite(secteur);


        System.out.print("Activité (" + p.getActivité() + ") : ");
        String actStr = scanner.nextLine();
        if (!actStr.isEmpty()) p.setActivité(actStr);

        boolean success = service.modifierProfessionnel(p);

        if (success) {
            System.out.println("Professionnel modifié avec succès !");
        } else {
            System.out.println("Erreur lors de la modification.");
        }
    }

    private void consulterProfilProfessionnel() {
        System.out.print("Entrez l'ID du professionnel : ");
        int id = Integer.parseInt(scanner.nextLine());

        Professionnel p = service.getProfessionnelById(id);

        if (p != null) {
            System.out.println("\n=== Profil du professionnel ===");
            System.out.println("ID : " + p.getId());
            System.out.println("Nom : " + p.getNom());
            System.out.println("Prénom : " + p.getPrenom());
            System.out.println("Date de naissance : " + p.getDatedenaissance());
            System.out.println("Ville : " + p.getVille());
            System.out.println("Nombre d'enfants : " + p.getNombreEnfants());
            System.out.println("Investissement : " + p.isInvestissement());
            System.out.println("Placement : " + p.isPlacement());
            System.out.println("Situation familiale : " + p.getSituation_Familiale());
            System.out.println("Score : " + p.getScore());
            System.out.println("Revenu : " + p.getRevenu());
            System.out.println("Immatriculation fiscale : " + p.getImmatriculationfiscale());
            System.out.println("Secteur activité : " + p.getSecteuractivite());
            System.out.println("Activité : " + p.getActivité());
        } else {
            System.out.println("Professionnel introuvable !");
        }
    }

    private void supprimerProfessionnel() {
        System.out.print("Entrez l'ID du professionnel à supprimer : ");
        int id = Integer.parseInt(scanner.nextLine());

        Professionnel p = service.getProfessionnelById(id);
        if (p == null) {
            System.out.println("Professionnel introuvable !");
            return;
        }

        System.out.println("Professionnel: " + p.getNom() + " " + p.getPrenom());
        System.out.print("Êtes-vous sûr de vouloir supprimer ? (oui/non): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("oui")) {
            boolean success = service.supprimerProfessionnel(id);

            if (success) {
                System.out.println("Professionnel supprimé avec succès !");
            } else {
                System.out.println("Erreur lors de la suppression.");
            }
        } else {
            System.out.println("Suppression annulée.");
        }
    }
}
