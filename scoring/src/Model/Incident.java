package Model;

import enums.Typeincident;

import java.sql.Date;

public class Incident {
    private int id;
    private int echeanceId;
    private Date dateIncident;
    private double score;
    private Typeincident typeincident;


    public Incident(Date dateIncident, double score, Typeincident typeincident){
        this.dateIncident = dateIncident;
        this.score = score;
        this.typeincident = typeincident;
    }

    public int getEcheanceId() {
        return echeanceId;
    }

    public int getId() {
        return id;
    }

    public Date getDateIncident() {
        return dateIncident;
    }

    public double getScore() {
        return score;
    }

    public Typeincident getTypeincident() {
        return typeincident;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setDateIncident(Date dateIncident) {
        this.dateIncident = dateIncident;
    }

    public void setTypeincident(Typeincident typeincident) {
        this.typeincident = typeincident;
    }
}
