package Model;

import enums.Situation_Familiale;

import java.time.LocalDate;
import java.util.Date;

public class Personne {
    protected int id;
    protected String nom;
    protected String prenom;
    protected LocalDate datedenaissance;
    protected String ville;
    protected int nombreEnfants;
    protected boolean investissement;
    protected boolean placement;
    protected Situation_Familiale Situation_Familiale;
    protected double score;

    public Personne(int id,String nom,String prenom,LocalDate datedenaissance,String ville,int nombreEnfants,boolean investissement,boolean placement, Situation_Familiale Situation_Familiale,double score){
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.datedenaissance = datedenaissance;
        this.ville = ville;
        this.nombreEnfants = nombreEnfants;
        this.investissement = investissement;
        this.placement = placement;
        this.Situation_Familiale = Situation_Familiale;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public LocalDate getDatedenaissance() {
        return datedenaissance;
    }

    public String getVille() {
        return ville;
    }

    public int getNombreEnfants() {
        return nombreEnfants;
    }

    public boolean isInvestissement() {
        return investissement;
    }

    public boolean isPlacement() {
        return placement;
    }

    public Situation_Familiale getSituation_Familiale() {
        return Situation_Familiale;
    }

    public double getScore() {
        return score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDatedenaissance(LocalDate datedenaissance) {
        this.datedenaissance = datedenaissance;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setNombreEnfants(int nombreEnfants) {
        this.nombreEnfants = nombreEnfants;
    }

    public void setInvestissement(boolean investissement) {
        this.investissement = investissement;
    }

    public void setPlacement(boolean placement) {
        this.placement = placement;
    }

    public void setSituation_Familiale(Situation_Familiale situation_Familiale) {
        Situation_Familiale = situation_Familiale;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
