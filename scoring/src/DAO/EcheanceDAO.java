package DAO;

import Database.Database;
import Model.Echeance;
import enums.Statutpaiement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EcheanceDAO {

    public int create(Echeance echeance, int creditId) {
        String sql = "INSERT INTO echeance (date_echeance, mensualite, statut_paiement, date_paiement, credit_id) VALUES (?,?,?,?,?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, new java.sql.Date(echeance.getDateecheance().getTime()));
            stmt.setDouble(2, echeance.getMensualité());
            stmt.setString(3, echeance.getStatutpaiement().toString());

            if (echeance.getDatedepaiement() != null) {
                stmt.setDate(4, new java.sql.Date(echeance.getDatedepaiement().getTime()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setInt(5, creditId);

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    echeance.setId(rs.getInt(1));
                    return echeance.getId();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur création échéance: " + e.getMessage());
        }
        return -1;
    }

    public boolean update(Echeance echeance) {
        String sql = "UPDATE echeance SET date_echeance=?, mensualite=?, statut_paiement=?, date_paiement=? WHERE id=?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(echeance.getDateecheance().getTime()));
            stmt.setDouble(2, echeance.getMensualité());
            stmt.setString(3, echeance.getStatutpaiement().toString());

            if (echeance.getDatedepaiement() != null) {
                stmt.setDate(4, new java.sql.Date(echeance.getDatedepaiement().getTime()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setInt(5, echeance.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur modification échéance: " + e.getMessage());
        }
        return false;
    }

    public List<Echeance> findByCreditId(int creditId) {
        List<Echeance> list = new ArrayList<>();
        String sql = "SELECT * FROM echeance WHERE credit_id=? ORDER BY date_echeance";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, creditId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Echeance e = new Echeance(
                        rs.getDate("date_echeance"),
                        rs.getDouble("mensualite"),
                        Statutpaiement.valueOf(rs.getString("statut_paiement")),
                        rs.getDate("date_paiement")
                );
                e.setId(rs.getInt("id"));
                list.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération échéances: " + e.getMessage());
        }
        return list;
    }

    // Ajouter dans EcheanceDAO.java

    public Echeance findById(int id) {
        String sql = "SELECT * FROM echeance WHERE id=?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Echeance e = new Echeance(
                        rs.getDate("date_echeance"),
                        rs.getDouble("mensualite"),
                        Statutpaiement.valueOf(rs.getString("statut_paiement")),
                        rs.getDate("date_paiement")
                );
                e.setId(rs.getInt("id"));
                return e;
            }
        } catch (SQLException e) {
            System.err.println("Erreur findById échéance: " + e.getMessage());
        }
        return null;
    }
}