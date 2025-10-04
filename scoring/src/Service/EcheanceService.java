package Service;

import Model.Credit;
import Model.Echeance;
import Model.Incident;
import Model.Personne;
import DAO.EcheanceDAO;
import DAO.IncidentDAO;
import enums.Statutpaiement;
import enums.Typeincident;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class EcheanceService {

    private EcheanceDAO echeanceDAO = new EcheanceDAO();
    private IncidentDAO incidentDAO = new IncidentDAO();

    /**
     * Génère toutes les échéances pour un crédit approuvé
     */
    public List<Echeance> genererEcheances(Credit credit) {
        List<Echeance> echeances = new ArrayList<>();

        // Calculer la mensualité
        double mensualite = calculerMensualite(
                credit.getMontantoctroye(),
                credit.getTauxinteret(),
                credit.getDureeenmois()
        );

        Calendar cal = Calendar.getInstance();
        cal.setTime(credit.getDatedecredit());

        // Générer chaque échéance
        for (int i = 1; i <= credit.getDureeenmois(); i++) {
            cal.add(Calendar.MONTH, 1);

            Echeance echeance = new Echeance(
                    new Date(cal.getTimeInMillis()),
                    mensualite,
                    Statutpaiement.IMPAYENONREGLE,
                    null  // Pas encore payé
            );

            // Sauvegarder en BD
            int id = echeanceDAO.create(echeance, credit.getId());
            if (id > 0) {
                echeances.add(echeance);
            }
        }

        return echeances;
    }

    /**
     * Calcule la mensualité selon la formule d'amortissement
     * M = P * (r * (1+r)^n) / ((1+r)^n - 1)
     */
    private double calculerMensualite(double capital, double tauxAnnuel, int duree) {
        if (tauxAnnuel == 0) {
            return capital / duree;
        }

        double tauxMensuel = tauxAnnuel / 100 / 12;
        double facteur = Math.pow(1 + tauxMensuel, duree);

        return capital * (tauxMensuel * facteur) / (facteur - 1);
    }

    /**
     * Enregistre un paiement d'échéance
     */
    public void enregistrerPaiement(int echeanceId, Date datePaiement, int clientId) {
        Echeance echeance = echeanceDAO.findById(echeanceId);

        if (echeance == null) {
            System.out.println("Échéance introuvable!");
            return;
        }

        // Calculer le nombre de jours de retard
        long diffMillis = datePaiement.getTime() - echeance.getDateecheance().getTime();
        int joursRetard = (int) TimeUnit.MILLISECONDS.toDays(diffMillis);

        // Déterminer le statut
        Statutpaiement statut;
        Typeincident typeIncident = null;
        double penalite = 0;

        if (joursRetard <= 0) {
            // Paiement à temps
            statut = Statutpaiement.PAYEATEMPS;
            typeIncident = Typeincident.PAYEATEMPS;
            penalite = 0;

        } else if (joursRetard <= 5) {
            // Léger retard (1-5 jours)
            statut = Statutpaiement.ENRETARD;
            typeIncident = Typeincident.ENRETARD;
            penalite = -2; // -2 points

        } else if (joursRetard <= 30) {
            // Retard moyen (6-30 jours)
            statut = Statutpaiement.PAYEENRETARD;
            typeIncident = Typeincident.PAYEENRETARD;
            penalite = -5; // -5 points

        } else {
            // Impayé (31+ jours) - maintenant réglé
            statut = Statutpaiement.IMPAYEREGLE;
            typeIncident = Typeincident.IMPAYEREGLE;
            penalite = -10; // -10 points
        }

        // Mettre à jour l'échéance
        echeance.setDatedepaiement(datePaiement);
        echeance.setStatutpaiement(statut);
        echeanceDAO.update(echeance);

        // Créer un incident
        Incident incident = new Incident(
                new java.sql.Date(datePaiement.getTime()),
                penalite,
                typeIncident
        );
        incidentDAO.create(incident, echeanceId);

        // Mettre à jour le score du client
        if (penalite != 0) {
            mettreAJourScoreClient(clientId, penalite);
        }

        System.out.println("Paiement enregistré : " + statut);
        if (joursRetard > 0) {
            System.out.println("Retard : " + joursRetard + " jours | Pénalité : " + penalite + " points");
        }
    }

    /**
     * Marque les échéances impayées (à exécuter périodiquement)
     */
    public void verifierEcheancesImpayees(int creditId) {
        List<Echeance> echeances = echeanceDAO.findByCreditId(creditId);
        Date aujourdhui = new Date();

        for (Echeance e : echeances) {
            // Si échéance non payée et date dépassée de plus de 30 jours
            if (e.getDatedepaiement() == null) {
                long diffMillis = aujourdhui.getTime() - e.getDateecheance().getTime();
                int joursRetard = (int) TimeUnit.MILLISECONDS.toDays(diffMillis);

                if (joursRetard > 30 && e.getStatutpaiement() != Statutpaiement.IMPAYENONREGLE) {
                    e.setStatutpaiement(Statutpaiement.IMPAYENONREGLE);
                    echeanceDAO.update(e);

                    // Créer un incident impayé
                    Incident incident = new Incident(
                            new java.sql.Date(aujourdhui.getTime()),
                            -15, // -15 points pour impayé non réglé
                            Typeincident.IMPAYENONREGLE
                    );
                    incidentDAO.create(incident, e.getId());
                }
            }
        }
    }

    /**
     * Calcule et attribue le bonus pour historique sans incident
     */
    public void attribuerBonusSansIncident(int clientId) {
        List<Incident> incidents = incidentDAO.findByClientId(clientId);

        // Vérifier les 12 derniers mois
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -12);
        Date il_y_a_12_mois = cal.getTime();

        long incidentsRecents = incidents.stream()
                .filter(i -> i.getDateIncident().after(il_y_a_12_mois))
                .filter(i -> i.getTypeincident() != Typeincident.PAYEATEMPS)
                .count();

        if (incidentsRecents == 0 && incidents.size() > 0) {
            // Client sans incident depuis 12 mois : bonus +10 points
            mettreAJourScoreClient(clientId, 10);
            System.out.println("✓ Bonus +10 points attribué pour historique sans incident!");
        }
    }

    /**
     * Met à jour le score d'un client
     */
    private void mettreAJourScoreClient(int clientId, double ajustement) {
    }

    /**
     * Affiche le tableau d'amortissement
     */
    public void afficherTableauAmortissement(Credit credit) {
        List<Echeance> echeances = echeanceDAO.findByCreditId(credit.getId());

        System.out.println("--------TABLEAU D'AMORTISSEMENT---------");
        System.out.println("Montant : " + credit.getMontantoctroye() + " DH");
        System.out.println("Taux : " + credit.getTauxinteret() + "%");
        System.out.println("Durée : " + credit.getDureeenmois() + " mois");
        System.out.println("----------------------------------------");
        System.out.printf("%-5s %-12s %-12s %-15s %-12s\n",
                "N°", "Date", "Mensualité", "Statut", "Payé le");
        System.out.println("----------------------------------------");

        int i = 1;
        for (Echeance e : echeances) {
            String datePaiement = e.getDatedepaiement() != null
                    ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(e.getDatedepaiement())
                    : "-";

            System.out.printf("%-5d %-12s %-12.2f %-15s %-12s\n",
                    i++,
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(e.getDateecheance()),
                    e.getMensualité(),
                    e.getStatutpaiement(),
                    datePaiement
            );
        }
        System.out.println("---------------------------------------\n");
    }

    /**
     * Récupère l'historique des incidents d'un client
     */
    public void afficherHistoriqueIncidents(int clientId) {
        List<Incident> incidents = incidentDAO.findByClientId(clientId);

        System.out.println("\n=== HISTORIQUE DES INCIDENTS ===");
        System.out.printf("%-12s %-20s %-10s\n", "Date", "Type", "Impact");
        System.out.println("----------------------------------------");

        for (Incident inc : incidents) {
            System.out.printf("%-12s %-20s %-10.1f\n",
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(inc.getDateIncident()),
                    inc.getTypeincident(),
                    inc.getScore()
            );
        }
        System.out.println();
    }
}