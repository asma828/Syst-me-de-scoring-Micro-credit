package View;

import Model.Credit;
import Model.Echeance;
import Service.CreditService;
import Service.EcheanceService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class PaiementView {

    private EcheanceService echeanceService = new EcheanceService();
    private CreditService creditService = new CreditService();
    private Scanner scanner = new Scanner(System.in);
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public void menuPaiement() {
        while (true) {
            System.out.println("\n=== GESTION DES PAIEMENTS ===");
            System.out.println("1. Afficher tableau d'amortissement");
            System.out.println("2. Enregistrer un paiement");
            System.out.println("3. Vérifier échéances impayées");
            System.out.println("4. Afficher historique incidents client");
            System.out.println("5. Attribuer bonus sans incident");
            System.out.println("0. Retour");
            System.out.print("Choix: ");

            int choix = Integer.parseInt(scanner.nextLine());

            switch (choix) {
                case 1:
                    afficherTableau();
                    break;
                case 2:
                    enregistrerPaiement();
                    break;
                case 3:
                    verifierImpayees();
                    break;
                case 4:
                    afficherHistorique();
                    break;
                case 5:
                    attribuerBonus();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Option invalide!");
            }
        }
    }

    private void afficherTableau() {
        System.out.print("ID du crédit: ");
        int creditId = Integer.parseInt(scanner.nextLine());

        Credit credit = creditService.getCredit(creditId);
        if (credit != null) {
            echeanceService.afficherTableauAmortissement(credit);
        } else {
            System.out.println("Crédit introuvable!");
        }
    }

    private void enregistrerPaiement() {
        System.out.print("ID de l'échéance: ");
        int echeanceId = Integer.parseInt(scanner.nextLine());

        System.out.print("ID du client: ");
        int clientId = Integer.parseInt(scanner.nextLine());

        System.out.print("Date de paiement (dd/MM/yyyy): ");
        String dateStr = scanner.nextLine();

        try {
            Date datePaiement = sdf.parse(dateStr);
            echeanceService.enregistrerPaiement(echeanceId, datePaiement, clientId);
        } catch (ParseException e) {
            System.out.println("Format de date invalide!");
        }
    }

    private void verifierImpayees() {
        System.out.print("ID du crédit: ");
        int creditId = Integer.parseInt(scanner.nextLine());

        echeanceService.verifierEcheancesImpayees(creditId);
        System.out.println("Vérification terminée!");
    }

    private void afficherHistorique() {
        System.out.print("ID du client: ");
        int clientId = Integer.parseInt(scanner.nextLine());

        echeanceService.afficherHistoriqueIncidents(clientId);
    }

    private void attribuerBonus() {
        System.out.print("ID du client: ");
        int clientId = Integer.parseInt(scanner.nextLine());

        echeanceService.attribuerBonusSansIncident(clientId);
    }
}