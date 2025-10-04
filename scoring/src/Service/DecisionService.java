package Service;

import Model.Credit;
import Model.Personne;
import enums.Decision;

public class DecisionService {

    private ScoringService scoringService = new ScoringService();

    /**
     * Prend une décision automatique sur la demande de crédit
     * @param personne Le client demandeur
     * @param credit La demande de crédit
     * @param isNouveauClient Si c'est un nouveau client ou existant
     * @return La décision prise
     */
    public Decision prendreDecision(Personne personne, Credit credit, boolean isNouveauClient) {
        double score = personne.getScore();

        // 1. Vérifier l'éligibilité minimale
        if (!scoringService.estEligible(personne, isNouveauClient)) {
            credit.setDecision(Decision.REFUS_AUTOMATIQUE);
            credit.setMontantoctroye(0);
            return Decision.REFUS_AUTOMATIQUE;
        }

        // 2. Calculer la capacité d'emprunt
        double capaciteMax = scoringService.calculerCapaciteEmprunt(personne, isNouveauClient);

        // 3. Vérifier si le montant demandé est dans la capacité
        if (credit.getMontantdemande() > capaciteMax) {
            credit.setDecision(Decision.REFUS_AUTOMATIQUE);
            credit.setMontantoctroye(0);
            return Decision.REFUS_AUTOMATIQUE;
        }

        // 4. Décision basée sur le score
        if (score >= 80) {
            // Accord immédiat
            credit.setDecision(Decision.ACCORDIMMEDIAT);
            credit.setMontantoctroye(credit.getMontantdemande());
            return Decision.ACCORDIMMEDIAT;

        } else if (score >= 60 && score < 80) {
            // Étude manuelle
            credit.setDecision(Decision.ETUDEMANUELLE);
            //  proposer un montant réduit
            credit.setMontantoctroye(credit.getMontantdemande() * 0.8);
            return Decision.ETUDEMANUELLE;

        } else {
            // Refus automatique
            credit.setDecision(Decision.REFUS_AUTOMATIQUE);
            credit.setMontantoctroye(0);
            return Decision.REFUS_AUTOMATIQUE;
        }
    }

    /**
     * Affiche les détails de la décision
     */
    public void afficherDecision(Credit credit, Personne personne, boolean isNouveauClient) {
        System.out.println("----DÉCISION DE CRÉDIT-----");
        System.out.println("Client : " + personne.getNom() + " " + personne.getPrenom());
        System.out.println("Score : " + personne.getScore() + "/100");
        System.out.println("Type client : " + (isNouveauClient ? "Nouveau" : "Existant"));
        System.out.println("Montant demandé : " + credit.getMontantdemande() + " DH");
        System.out.println("Capacité max : " + scoringService.calculerCapaciteEmprunt(personne, isNouveauClient) + " DH");
        System.out.println("----------------------------------------");

        switch (credit.getDecision()) {
            case ACCORDIMMEDIAT:
                System.out.println(" ACCORD IMMÉDIAT");
                System.out.println("Montant octroyé : " + credit.getMontantoctroye() + " DH");
                System.out.println("Félicitations ! Votre crédit est approuvé.");
                break;

            case ETUDEMANUELLE:
                System.out.println(" ÉTUDE MANUELLE REQUISE");
                System.out.println("Montant proposé : " + credit.getMontantoctroye() + " DH");
                System.out.println("Votre dossier nécessite une analyse approfondie.");
                break;

            case REFUS_AUTOMATIQUE:
                System.out.println("✗ REFUS AUTOMATIQUE");
                System.out.println("Votre demande ne peut être acceptée.");
                System.out.println("Raison : Score insuffisant ou montant trop élevé.");
                break;
        }
        System.out.println("---------\n");
    }
}