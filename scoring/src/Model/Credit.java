package Model;

import enums.Decision;
import enums.TypeCredit;

import java.util.Date;

public class Credit {
    private int id;
    private Integer employeeId;
    private Integer professionalId;
    private Date datedecredit;
    private double montantdemande;
    private double montantoctroye;
    private double tauxinteret;
    private int dureeenmois;
    private TypeCredit typeCredit;
    private Decision decision;

    public Credit(Integer employeeId, Integer professionalId,Date datedecredit,double montantdemande,double montantoctroye,double tauxinteret,int dureeenmois,TypeCredit typeCredit,Decision decision){
        this.employeeId = employeeId;
        this.professionalId = professionalId;
        this.datedecredit = datedecredit;
        this.montantdemande = montantdemande;
        this.montantoctroye = montantoctroye;
        this.tauxinteret = tauxinteret;
        this.dureeenmois = dureeenmois;
        this.typeCredit = typeCredit;
        this.decision = decision;
    }


    public Date getDatedecredit() {
        return datedecredit;
    }

    public double getMontantdemande() {
        return montantdemande;
    }

    public double getMontantoctroye() {
        return montantoctroye;
    }

    public double getTauxinteret() {
        return tauxinteret;
    }

    public TypeCredit getTypeCredit() {
        return typeCredit;
    }

    public Decision getDecision() {
        return decision;
    }

    public int getDureeenmois() {
        return dureeenmois;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDatedecredit(Date datedecredit) {
        this.datedecredit = datedecredit;
    }

    public void setMontantdemande(double montantdemande) {
        this.montantdemande = montantdemande;
    }

    public void setMontantoctroye(double montantoctroye) {
        this.montantoctroye = montantoctroye;
    }

    public void setTauxinteret(double tauxinteret) {
        this.tauxinteret = tauxinteret;
    }

    public void setDureeenmois(int dureeenmois) {
        this.dureeenmois = dureeenmois;
    }

    public void setTypeCredit(TypeCredit typeCredit) {
        this.typeCredit = typeCredit;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public int getClientId() {
        return employeeId != null ? employeeId : professionalId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public Integer getProfessionalId() {
        return professionalId;
    }

    public String getTypeClient() {
        return employeeId != null ? "EMPLOYE" : "PROFESSIONNEL";
    }

}
