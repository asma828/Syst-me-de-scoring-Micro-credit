package View;

import Model.Employe;
import Model.Personne;
import Service.AnalyticsService;
import Service.StatistiquesEmploye;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AnalyticsView {

    private AnalyticsService analyticsService = new AnalyticsService();
    private Scanner scanner = new Scanner(System.in);

    public void menuAnalytics() {
        while (true) {
            System.out.println("\n=== MODULE ANALYTICS ===");
            System.out.println("1. Clients éligibles crédit immobilier");
            System.out.println("2. Clients à risque (Top 10)");
            System.out.println("3. Tri clients par critères");
            System.out.println("4. Répartition par type d'emploi");
            System.out.println("5. Campagne crédit consommation");
            System.out.println("0. Retour");
            System.out.print("Choix: ");

            int choix = Integer.parseInt(scanner.nextLine());

            switch (choix) {
                case 1:
                    afficherClientsEligiblesImmobilier();
                    break;
                case 2:
                    afficherClientsARisque();
                    break;
                case 3:
                    trierClients();
                    break;
                case 4:
                    afficherRepartition();
                    break;
                case 5:
                    afficherCampagneConso();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Option invalide!");
            }
        }
    }

    private void afficherClientsEligiblesImmobilier() {
        // TODO: Récupérer tous les employés depuis la BD
        List<Employe> employes = getAllEmployes();
        List<Employe> eligibles = analyticsService.rechercherClientsEligiblesCreditImmobilier(employes);

        System.out.println("\n=== CLIENTS ÉLIGIBLES CRÉDIT IMMOBILIER ===");
        System.out.println("Total: " + eligibles.size() + " clients");

        eligibles.forEach(e -> {
            System.out.printf("%s %s | Score: %.0f | Salaire: %.0f DH | %s\n",
                    e.getNom(), e.getPrenom(), e.getScore(), e.getSalaire(), e.getTypeContrat());
        });
    }

    private void afficherClientsARisque() {
        List<Personne> clients = getAllClients();
        List<Map<String, Object>> risque = analyticsService.rechercherClientsARisque(clients);

        System.out.println("\n=== CLIENTS À RISQUE (TOP 10) ===");

        risque.forEach(m -> {
            Personne p = (Personne) m.get("client");
            long incidents = (long) m.get("incidentsRecents");
            System.out.printf(" %s %s | Score: %.0f | Incidents récents: %d\n",
                    p.getNom(), p.getPrenom(), p.getScore(), incidents);
        });
    }

    private void trierClients() {
        System.out.print("Trier par (score/revenus/anciennete): ");
        String critere = scanner.nextLine();

        List<Personne> clients = getAllClients();
        List<Personne> tries = analyticsService.trierClients(clients, critere);

        System.out.println("\n=== CLIENTS TRIÉS PAR " + critere.toUpperCase() + " ===");
        tries.stream().limit(20).forEach(p -> {
            System.out.printf("%s %s | Score: %.0f\n",
                    p.getNom(), p.getPrenom(), p.getScore());
        });
    }

    private void afficherRepartition() {
        List<Employe> employes = getAllEmployes();
        Map<String, StatistiquesEmploye> stats = analyticsService.repartitionParTypeEmploi(employes);

        System.out.println("\n=== RÉPARTITION PAR TYPE D'EMPLOI ===");
        stats.forEach((type, stat) -> {
            System.out.println("\n" + type + ":");
            System.out.println("  " + stat);
        });
    }

    private void afficherCampagneConso() {
        List<Personne> clients = getAllClients();
        List<Personne> cibles = analyticsService.campagnePublicitaireCreditConso(clients);

        System.out.println("\n=== CAMPAGNE CRÉDIT CONSOMMATION ===");
        System.out.println("Clients ciblés: " + cibles.size());
        System.out.println("Action suggérée: Envoi SMS/Email");

        cibles.stream().limit(10).forEach(p -> {
            System.out.printf("→ %s %s | Score: %.0f\n",
                    p.getNom(), p.getPrenom(), p.getScore());
        });
    }

    // Méthodes pour récupérer tous les clients
    private List<Employe> getAllEmployes() {
        return new ArrayList<>();
    }

    private List<Personne> getAllClients() {
        return new ArrayList<>();
    }
}