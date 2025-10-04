package View;

import Model.Credit;
import Model.Personne;
import Service.CreditService;
import Service.EmployeService;
import Service.ProfessionnelService;
import enums.Decision;
import enums.TypeCredit;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CreditView {
    private CreditService service = new CreditService();
    private EmployeService employeService = new EmployeService();
    private ProfessionnelService professionnelService = new ProfessionnelService();
    private Scanner scanner = new Scanner(System.in);

    public void menuCredit() {
        while (true) {
            System.out.println("\n=== GESTION DES CRÉDITS ===");
            System.out.println("1. Demander un crédit");
            System.out.println("2. Lister crédits d'un client");
            System.out.println("3. Lister tous les crédits");
            System.out.println("4. Consulter un crédit");
            System.out.println("5. Supprimer crédit");
            System.out.println("0. Retour");
            System.out.print("Choix: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    demanderCredit();
                    break;
                case 2:
                    listerCreditsClient();
                    break;
                case 3:
                    listerTousLesCredits();
                    break;
                case 4:
                    consulterCredit();
                    break;
                case 5:
                    supprimerCredit();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Option invalide!");
            }
        }
    }




    private void listerCredit() {
        List<Credit> list = service.getAllCredits();
        for (Credit c : list) {
            System.out.println(c.getId() + " | " + c.getMontantdemande() + " | " + c.getTypeCredit() + " | " + c.getDecision());
        }
    }

    private void modifierCredit() {
        System.out.print("ID du crédit à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Credit c = service.getCredit(id);
        if (c == null) {
            System.out.println("Crédit introuvable !");
            return;
        }

        System.out.print("Nouveau taux d’intérêt (" + c.getTauxinteret() + ") : ");
        String tauxStr = scanner.nextLine();
        if (!tauxStr.isEmpty()) c.setTauxinteret(Double.parseDouble(tauxStr));

        if (service.modifierCredit(c)) {
            System.out.println("Crédit modifié !");
        } else {
            System.out.println("Échec de modification.");
        }
    }

    private void demanderCredit() {
        System.out.println("\n=== DEMANDE DE CRÉDIT ===");

        System.out.print("Type de client (EMPLOYE/PROFESSIONNEL): ");
        String typeClient = scanner.nextLine().toUpperCase();

        System.out.print("ID du client: ");
        int clientId = Integer.parseInt(scanner.nextLine());

        // Vérifier existence
        Personne client = null;
        if (typeClient.equals("EMPLOYE")) {
            client = employeService.getEmployeById(clientId);
        } else {
            client = professionnelService.getProfessionnelById(clientId);
        }

        if (client == null) {
            System.out.println("Client introuvable!");
            return;
        }

        System.out.println("Client: " + client.getNom() + " " + client.getPrenom());
        System.out.println("Score actuel: " + client.getScore());

        // Informations du crédit
        System.out.print("Montant demandé (DH): ");
        double montant = Double.parseDouble(scanner.nextLine());

        System.out.print("Durée (mois): ");
        int duree = Integer.parseInt(scanner.nextLine());

        System.out.print("Type crédit (CONSO/IMMO/AUTO): ");
        TypeCredit type = TypeCredit.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Taux d'intérêt annuel (%): ");
        double taux = Double.parseDouble(scanner.nextLine());

        System.out.print("Nouveau client ? (true/false): ");
        boolean isNouveau = Boolean.parseBoolean(scanner.nextLine());

        // Traiter la demande
        Credit credit = service.demanderCredit(
                clientId, typeClient, montant, taux, duree, type, isNouveau
        );

        if (credit != null && credit.getId() > 0) {
            System.out.println("\nDemande enregistrée avec ID: " + credit.getId());
        }
    }

    private void listerCreditsClient() {
        System.out.print("Type de client (EMPLOYE/PROFESSIONNEL): ");
        String typeClient = scanner.nextLine().toUpperCase();

        System.out.print("ID du client: ");
        int clientId = Integer.parseInt(scanner.nextLine());

        List<Credit> credits = service.getCreditsByClient(clientId, typeClient);

        if (credits.isEmpty()) {
            System.out.println("Aucun crédit trouvé pour ce client.");
            return;
        }

        System.out.println("\n=== CRÉDITS DU CLIENT ===");
        for (Credit c : credits) {
            System.out.printf("ID:%d | %s | %.0f DH | %s | %s\n",
                    c.getId(),
                    c.getTypeCredit(),
                    c.getMontantdemande(),
                    c.getDecision(),
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(c.getDatedecredit())
            );
        }
    }

    private void listerTousLesCredits() {
        List<Credit> list = service.getAllCredits();

        if (list.isEmpty()) {
            System.out.println("Aucun crédit enregistré.");
            return;
        }

        System.out.println("\n=== TOUS LES CRÉDITS ===");
        System.out.printf("%-5s %-12s %-12s %-20s\n", "ID", "Demandé", "Octroyé", "Décision");
        System.out.println("------------------------------------------------");

        for (Credit c : list) {
            System.out.printf("%-5d %-12.0f %-12.0f %-20s\n",
                    c.getId(),
                    c.getMontantdemande(),
                    c.getMontantoctroye(),
                    c.getDecision()
            );
        }
    }

    private void consulterCredit() {
        System.out.print("ID du crédit: ");
        int id = Integer.parseInt(scanner.nextLine());

        Credit c = service.getCredit(id);
        if (c == null) {
            System.out.println("Crédit introuvable!");
            return;
        }

        System.out.println("\n=== DÉTAILS DU CRÉDIT ===");
        System.out.println("ID: " + c.getId());
        System.out.println("Client: " + c.getTypeClient() + " #" + c.getClientId());
        System.out.println("Type: " + c.getTypeCredit());
        System.out.println("Montant demandé: " + c.getMontantdemande() + " DH");
        System.out.println("Montant octroyé: " + c.getMontantoctroye() + " DH");
        System.out.println("Taux: " + c.getTauxinteret() + "%");
        System.out.println("Durée: " + c.getDureeenmois() + " mois");
        System.out.println("Décision: " + c.getDecision());
        System.out.println("Date: " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(c.getDatedecredit()));
    }

    private void supprimerCredit() {
        System.out.print("ID du crédit à supprimer: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Êtes-vous sûr ? (oui/non): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("oui")) {
            if (service.supprimerCredit(id)) {
                System.out.println("✓ Crédit supprimé!");
            } else {
                System.out.println(" Échec de suppression.");
            }
        }
    }
}

