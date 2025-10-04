package Model;


import enums.Secteuractivite;
import enums.Situation_Familiale;

import java.time.LocalDate;
import java.util.Date;

public class Professionnel extends Personne {
    private double revenu;
    private String immatriculationfiscale;
    private Secteuractivite secteuractivite;
    private String activité;

    public Professionnel(int id, String nom, String prenom, LocalDate datedenaissance, String ville, int nombreEnfants, boolean investissement, boolean placement, Situation_Familiale Situation_Familiale, int score, double revenu, String immatriculationfiscale, Secteuractivite secteuractivite, String activité) {
        super(id, nom, prenom, datedenaissance, ville, nombreEnfants, investissement, placement, Situation_Familiale, score);
        this.id = id;
        this.revenu = revenu;
        this.immatriculationfiscale = immatriculationfiscale;
        this.secteuractivite = secteuractivite;
        this.activité = activité;
    }

    public double getRevenu() {
        return revenu;
    }

    public String getImmatriculationfiscale() {
        return immatriculationfiscale;
    }

    public Secteuractivite getSecteuractivite() {
        return secteuractivite;
    }

    public String getActivité() {
        return activité;
    }

    public void setRevenu(double revenu) {
        this.revenu = revenu;
    }

    public void setImmatriculationfiscale(String immatriculationfiscale) {
        this.immatriculationfiscale = immatriculationfiscale;
    }

    public void setSecteuractivite(Secteuractivite secteuractivite) {
        this.secteuractivite = secteuractivite;
    }

    public void setActivité(String activité) {
        this.activité = activité;
    }
}
