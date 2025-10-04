

import View.*;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        EmployeView employeView = new EmployeView();
        ProfessionnelView professionnelView = new ProfessionnelView();
        CreditView creditView = new CreditView();
        PaiementView paiementView = new PaiementView();
        AnalyticsView analyticsView = new AnalyticsView();

        while (true) {

            System.out.println("** SYSTÈME DE SCORING CRÉDIT **");

            System.out.println("1. Gestion des Employés");
            System.out.println("2. Gestion des Professionnels");
            System.out.println("3. Gestion des Crédits");
            System.out.println("4. Gestion des Paiements");
            System.out.println("5. Analytics & Recherches");
            System.out.println("0. Quitter");
            System.out.print("Choix: ");

            int choix = Integer.parseInt(scanner.nextLine());

            switch (choix) {
                case 1:
                    employeView.menuEmploye();
                    break;
                case 2:
                    professionnelView.menuProfessionnel();
                    break;
                case 3:
                    creditView.menuCredit();
                    break;
                case 4:
                    paiementView.menuPaiement();
                    break;
                case 5:
                    analyticsView.menuAnalytics();
                    break;
                case 0:
                    System.out.println("Au revoir!");
                    System.exit(0);
                default:
                    System.out.println("Option invalide!");
            }
        }
    }
}

