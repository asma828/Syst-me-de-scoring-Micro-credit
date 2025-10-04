package Model;

import enums.Decision;
import enums.Statutpaiement;
import enums.TypeCredit;

import java.util.Date;

public class Echeance {
    private int id;
    private int creditId;
    private Date dateecheance;
    private double mensualité;
    private Statutpaiement statutpaiement;
    private Date datedepaiement;

    public Echeance(Date dateecheance,double mensualité,Statutpaiement statutpaiement,Date datedepaiement){
        this.dateecheance = dateecheance;
        this.mensualité = mensualité;
        this.statutpaiement = statutpaiement;
        this.datedepaiement = datedepaiement;
    }

    public int getCreditId() {
        return creditId;
    }

    public int getId() {
        return id;
    }

    public Date getDateecheance() {
        return dateecheance;
    }

    public Statutpaiement getStatutpaiement() {
        return statutpaiement;
    }

    public Date getDatedepaiement() {
        return datedepaiement;
    }

    public double getMensualité() {
        return mensualité;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDatedepaiement(Date datedepaiement) {
        this.datedepaiement = datedepaiement;
    }

    public void setDateecheance(Date dateecheance) {
        this.dateecheance = dateecheance;
    }

    public void setMensualité(double mensualité) {
        this.mensualité = mensualité;
    }

    public void setStatutpaiement(Statutpaiement statutpaiement) {
        this.statutpaiement = statutpaiement;
    }
}
