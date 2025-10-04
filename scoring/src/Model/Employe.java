package Model;

import enums.Secteur;
import enums.Situation_Familiale;
import enums.TypeContrat;

import java.time.LocalDate;
import java.util.Date;

public class Employe extends Personne{
    private double salaire;
    private int Ancienneté;
    private String poste;
    private TypeContrat typeContrat;
    private Secteur secteur;

    public Employe(int id,String nom, String prenom, LocalDate datedenaissance, String ville, int nombreEnfants, boolean investissement, boolean placement, Situation_Familiale Situation_Familiale, int score, double salaire, int Ancienneté, String poste, TypeContrat typeContrat, Secteur secteur){
        super(id,nom,prenom,datedenaissance,ville,nombreEnfants,investissement,placement,Situation_Familiale,score);
        this.salaire = salaire;
        this.Ancienneté = Ancienneté;
        this.poste = poste;
        this.typeContrat = typeContrat;
        this.secteur = secteur;
    }

    public double getSalaire() {
        return salaire;
    }

    public int getAncienneté() {
        return Ancienneté;
    }
    public String getPoste(){
        return poste;
    }
    public TypeContrat getTypeContrat(){
        return typeContrat;
    }
    public Secteur getSecteur(){
        return secteur;
    }
    public void setSalaire(double salaire){
        this.salaire = salaire;
    }
    public void setAncienneté(int ancienneté){
        this.Ancienneté = ancienneté;
    }
    public void setPoste(String poste){
        this.poste = poste;
    }
    public void setTypeContrat(TypeContrat typeContrat){
        this.typeContrat = typeContrat;
    }
    public void setSecteur(Secteur secteur){
        this.secteur = secteur;
    }
}
