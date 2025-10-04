package Service;

import Model.Employe;
import Model.Personne;
import Model.Professionnel;
import enums.TypeContrat;
import enums.Secteur;

public class ScoringService {

    // Calcul du score total
    public int calculerScore(Personne personne) {
        int scoreTotal = 0;

        // 1. Stabilité professionnelle (25 points)
        scoreTotal += calculerStabiliteProfessionnelle(personne);

        // 2. Capacité financière (30 points)
        scoreTotal += calculerCapaciteFinanciere(personne);

        // 3. Critères complémentaires (20 points)
        scoreTotal += calculerCriteresComplementaires(personne);

        // 4. Relation client (15 points) - basé sur l'historique
        scoreTotal += calculerRelationClient(personne);

        // 5. Patrimoine (10 points)
        scoreTotal += calculerPatrimoine(personne);

        personne.setScore(scoreTotal);
        return scoreTotal;
    }

    private int calculerStabiliteProfessionnelle(Personne personne) {
        int score = 0;

        if (personne instanceof Employe) {
            Employe emp = (Employe) personne;

            // Type de contrat (0-10 points)
            if (emp.getTypeContrat() == TypeContrat.CDI) {
                score += 10;
            } else if (emp.getTypeContrat() == TypeContrat.CDD) {
                score += 5;
            } else {
                score += 2;
            }

            // Secteur (0-8 points)
            if (emp.getSecteur() == Secteur.PUBLIC) {
                score += 8;
            } else if (emp.getSecteur() == Secteur.GRANDE_ENTREPRISE) {
                score += 6;
            } else {
                score += 4;
            }

            // Ancienneté (0-7 points)
            if (emp.getAncienneté() >= 5) {
                score += 7;
            } else if (emp.getAncienneté() >= 2) {
                score += 5;
            } else if (emp.getAncienneté() >= 1) {
                score += 3;
            }

        } else if (personne instanceof Professionnel) {
            Professionnel prof = (Professionnel) personne;
            // Secteur d'activité (0-15 points)
            switch (prof.getSecteuractivite()) {
                case SERVICE:
                    score += 12; // Services
                    break;
                case COMMERCE:
                    score += 10; // Commerce
                    break;
                case CONSTRUCTION:
                    score += 8; // Construction
                    break;
                case AGRICULTURE:
                    score += 6; // Agriculture
                    break;
                default:
                    score += 7;
            }
        }

        return Math.min(score, 25);
    }

    private int calculerCapaciteFinanciere(Personne personne) {
        int score = 0;
        double revenu = 0;

        if (personne instanceof Employe) {
            revenu = ((Employe) personne).getSalaire();
        } else if (personne instanceof Professionnel) {
            revenu = ((Professionnel) personne).getRevenu();
        }

        // Revenu mensuel (0-20 points)
        if (revenu >= 10000) {
            score += 20;
        } else if (revenu >= 7000) {
            score += 15;
        } else if (revenu >= 5000) {
            score += 12;
        } else if (revenu >= 3000) {
            score += 8;
        } else {
            score += 4;
        }

        // Charge familiale (0-10 points)
        int nbEnfants = personne.getNombreEnfants();
        if (nbEnfants == 0) {
            score += 10;
        } else if (nbEnfants <= 2) {
            score += 7;
        } else if (nbEnfants <= 4) {
            score += 4;
        } else {
            score += 2;
        }

        return Math.min(score, 30);
    }

    private int calculerCriteresComplementaires(Personne personne) {
        int score = 0;

        // Situation familiale (0-10 points)
        switch (personne.getSituation_Familiale()) {
            case MARRIED:
                score += 10;
                break;
            case SINGLE:
                score += 6;
                break;
            case DIVORCED:
                score += 5;
                break;
        }

        // Âge (0-10 points)
        int age = java.time.Period.between(
                personne.getDatedenaissance(),
                java.time.LocalDate.now()
        ).getYears();

        if (age >= 30 && age <= 50) {
            score += 10;
        } else if (age >= 25 && age < 30 || age > 50 && age <= 60) {
            score += 7;
        } else {
            score += 4;
        }

        return Math.min(score, 20);
    }

    private int calculerRelationClient(Personne personne) {
        // mis à jour par l'historique de paiement
        // Pour un nouveau client, je donne 10 points de base
        return 10;
    }

    private int calculerPatrimoine(Personne personne) {
        int score = 0;

        if (personne.isInvestissement()) {
            score += 5;
        }

        if (personne.isPlacement()) {
            score += 5;
        }

        return score;
    }

    // Calcul de la capacité d'emprunt
    public double calculerCapaciteEmprunt(Personne personne, boolean isNouveauClient) {
        double revenu = 0;

        if (personne instanceof Employe) {
            revenu = ((Employe) personne).getSalaire();
        } else if (personne instanceof Professionnel) {
            revenu = ((Professionnel) personne).getRevenu();
        }

        double score = personne.getScore();

        if (isNouveauClient) {
            return revenu * 4; // 4x salaire pour nouveau client
        } else {
            if (score > 80) {
                return revenu * 10; // 10x salaire si score > 80
            } else if (score >= 60 && score <= 80) {
                return revenu * 7; // 7x salaire si score entre 60-80
            } else {
                return 0; // Pas éligible
            }
        }
    }

    // Validation de l'éligibilité
    public boolean estEligible(Personne personne, boolean isNouveauClient) {
        double score = personne.getScore();

        if (isNouveauClient) {
            return score >= 70;
        } else {
            return score >= 60;
        }
    }
}