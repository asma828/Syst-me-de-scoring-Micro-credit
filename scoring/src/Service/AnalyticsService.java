package Service;

import Model.Employe;
import Model.Personne;
import Model.Professionnel;
import Model.Incident;
import DAO.EmployeDAO;
import DAO.ProfessionnelDAO;
import DAO.IncidentDAO;
import enums.TypeContrat;
import enums.Situation_Familiale;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsService {

    private EmployeDAO employeDAO = new EmployeDAO();
    private ProfessionnelDAO professionnelDAO = new ProfessionnelDAO();
    private IncidentDAO incidentDAO = new IncidentDAO();

    /**
     * 1. Recherche clients éligibles crédit immobilier
     * Critères: Age 25-50, Revenus >4000, CDI, Score >70, Marié
     */
    public List<Employe> rechercherClientsEligiblesCreditImmobilier(List<Employe> employes) {
        return employes.stream()
                .filter(e -> {
                    int age = calculerAge(e.getDatedenaissance());
                    return age >= 25 && age <= 50
                            && e.getSalaire() > 4000
                            && e.getTypeContrat() == TypeContrat.CDI
                            && e.getScore() > 70
                            && e.getSituation_Familiale() == Situation_Familiale.MARRIED;
                })
                .collect(Collectors.toList());
    }

    /**
     * 2. Clients à risque nécessitant suivi (Top 10)
     * Score < 60 et incidents récents (< 6 mois)
     */
    public List<Map<String, Object>> rechercherClientsARisque(List<Personne> clients) {
        LocalDate sixMoisAvant = LocalDate.now().minusMonths(6);

        return clients.stream()
                .filter(c -> c.getScore() < 60)
                .map(client -> {
                    List<Incident> incidents = incidentDAO.findByClientId(client.getId());

                    // Filtrer incidents récents (< 6 mois)
                    long incidentsRecents = incidents.stream()
                            .filter(i -> {
                                LocalDate dateIncident = i.getDateIncident()
                                        .toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate();
                                return dateIncident.isAfter(sixMoisAvant);
                            })
                            .count();

                    Map<String, Object> result = new HashMap<>();
                    result.put("client", client);
                    result.put("incidentsRecents", incidentsRecents);
                    return result;
                })
                .filter(m -> (long)m.get("incidentsRecents") > 0)
                .sorted((m1, m2) -> {
                    Personne p1 = (Personne) m1.get("client");
                    Personne p2 = (Personne) m2.get("client");
                    return Double.compare(p2.getScore(), p1.getScore()); // Décroissant
                })
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * 3. Tri clients par critères multiples
     */
    public List<Personne> trierClients(List<Personne> clients, String critere) {
        switch (critere.toLowerCase()) {
            case "score":
                return clients.stream()
                        .sorted((p1, p2) -> Double.compare(p2.getScore(), p1.getScore()))
                        .collect(Collectors.toList());

            case "revenus":
                return clients.stream()
                        .sorted((p1, p2) -> {
                            double r1 = getRevenu(p1);
                            double r2 = getRevenu(p2);
                            return Double.compare(r2, r1);
                        })
                        .collect(Collectors.toList());

            case "anciennete":
                return clients.stream()
                        .filter(p -> p instanceof Employe)
                        .sorted((p1, p2) -> {
                            int a1 = ((Employe)p1).getAncienneté();
                            int a2 = ((Employe)p2).getAncienneté();
                            return Integer.compare(a2, a1);
                        })
                        .collect(Collectors.toList());

            default:
                return clients;
        }
    }

    /**
     * 4. Répartition par type d'emploi avec statistiques
     */
    public Map<String, StatistiquesEmploye> repartitionParTypeEmploi(List<Employe> employes) {
        Map<String, StatistiquesEmploye> stats = new HashMap<>();

        // Grouper par type de contrat et secteur
        Map<String, List<Employe>> groupes = employes.stream()
                .collect(Collectors.groupingBy(e ->
                        e.getTypeContrat().toString() + "_" + e.getSecteur().toString()
                ));

        // Calculer les statistiques pour chaque groupe
        groupes.forEach((key, liste) -> {
            StatistiquesEmploye stat = new StatistiquesEmploye();
            stat.setNombreClients(liste.size());

            double scoreMoyen = liste.stream()
                    .mapToDouble(Employe::getScore)
                    .average()
                    .orElse(0.0);
            stat.setScoreMoyen(scoreMoyen);

            double revenuMoyen = liste.stream()
                    .mapToDouble(Employe::getSalaire)
                    .average()
                    .orElse(0.0);
            stat.setRevenuMoyen(revenuMoyen);

            // Taux d'approbation (score >= 70)
            long approuves = liste.stream()
                    .filter(e -> e.getScore() >= 70)
                    .count();
            double tauxApprobation = (double) approuves / liste.size() * 100;
            stat.setTauxApprobation(tauxApprobation);

            stats.put(key, stat);
        });

        return stats;
    }

    /**
     * 5. Campagne publicitaire - Crédit de consommation
     * Score 65-85, Revenus 4000-8000, Age 28-45, Pas de crédit en cours
     */
    public List<Personne> campagnePublicitaireCreditConso(List<Personne> clients) {
        return clients.stream()
                .filter(c -> {
                    double score = c.getScore();
                    double revenu = getRevenu(c);
                    int age = calculerAge(c.getDatedenaissance());

                    return score >= 65 && score <= 85
                            && revenu >= 4000 && revenu <= 8000
                            && age >= 28 && age <= 45;
                    // TODO: Vérifier "pas de crédit en cours" via CreditDAO
                })
                .collect(Collectors.toList());
    }

    // Méthodes utilitaires
    private int calculerAge(LocalDate dateNaissance) {
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }

    private double getRevenu(Personne p) {
        if (p instanceof Employe) {
            return ((Employe) p).getSalaire();
        } else if (p instanceof Professionnel) {
            return ((Professionnel) p).getRevenu();
        }
        return 0;
    }
}