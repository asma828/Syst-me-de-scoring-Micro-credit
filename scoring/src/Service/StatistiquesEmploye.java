package Service;

public class StatistiquesEmploye {
    private int nombreClients;
    private double scoreMoyen;
    private double revenuMoyen;
    private double tauxApprobation;

    // Getters et setters
    public int getNombreClients() { return nombreClients; }
    public void setNombreClients(int nombreClients) { this.nombreClients = nombreClients; }

    public double getScoreMoyen() { return scoreMoyen; }
    public void setScoreMoyen(double scoreMoyen) { this.scoreMoyen = scoreMoyen; }

    public double getRevenuMoyen() { return revenuMoyen; }
    public void setRevenuMoyen(double revenuMoyen) { this.revenuMoyen = revenuMoyen; }

    public double getTauxApprobation() { return tauxApprobation; }
    public void setTauxApprobation(double tauxApprobation) { this.tauxApprobation = tauxApprobation; }

    @Override
    public String toString() {
        return String.format(
                "%d clients | Score moyen: %.1f | Revenus moyens: %.0f DH | Taux approbation: %.1f%%",
                nombreClients, scoreMoyen, revenuMoyen, tauxApprobation
        );
    }
}
