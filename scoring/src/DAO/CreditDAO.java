package DAO;

import Database.Database;
import Model.Credit;
import enums.Decision;
import enums.TypeCredit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CreditDAO {

    // CREATE
    public int create(Credit credit) {
        String sql = "INSERT INTO credit (employee_id, professional_id,datedecredit, montantdemande, montantoctroye, tauxinteret, dureeenmois, typecredit, decision) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Gestion des NULL pour employee_id ou professional_id
            if (credit.getEmployeeId() != null) {
                stmt.setInt(1, credit.getEmployeeId());
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setNull(1, Types.INTEGER);
                stmt.setInt(2, credit.getProfessionalId());
            }

            stmt.setDate(3, new java.sql.Date(credit.getDatedecredit().getTime()));
            stmt.setDouble(4, credit.getMontantdemande());
            stmt.setDouble(5, credit.getMontantoctroye());
            stmt.setDouble(6, credit.getTauxinteret());
            stmt.setInt(7, credit.getDureeenmois());
            stmt.setString(8, credit.getTypeCredit().name());
            stmt.setString(9, credit.getDecision().name());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    credit.setId(rs.getInt(1));
                    return credit.getId();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur DAO create credit: " + e.getMessage());
        }
        return -1;
    }

    // READ
    public Credit findById(int id) {
        String sql = "SELECT * FROM credit WHERE id=?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractCredit(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur DAO findById: " + e.getMessage());
        }
        return null;
    }

    // READ ALL
    public List<Credit> findAll() {
        List<Credit> list = new ArrayList<>();
        String sql = "SELECT * FROM credit";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(extractCredit(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur DAO findAll: " + e.getMessage());
        }
        return list;
    }


    public List<Credit> findByEmployeeId(int employeeId) {
        List<Credit> list = new ArrayList<>();
        String sql = "SELECT * FROM credit WHERE employee_id=?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(extractCredit(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur findByEmployeeId: " + e.getMessage());
        }
        return list;
    }

    public List<Credit> findByProfessionalId(int professionalId) {
        List<Credit> list = new ArrayList<>();
        String sql = "SELECT * FROM credit WHERE professional_id=?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, professionalId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(extractCredit(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur findByProfessionalId: " + e.getMessage());
        }
        return list;
    }

    // UPDATE
    public boolean update(Credit credit) {
        String sql = "UPDATE credit SET employee_id=?, professional_id=?, datedecredit=?, montantdemande=?, montantoctroye=?, tauxinteret=?, dureeenmois=?, typecredit=?, decision=? WHERE id=?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (credit.getEmployeeId() != null) {
                stmt.setInt(1, credit.getEmployeeId());
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setNull(1, Types.INTEGER);
                stmt.setInt(2, credit.getProfessionalId());
            }

            stmt.setDate(3, new java.sql.Date(credit.getDatedecredit().getTime()));
            stmt.setDouble(4, credit.getMontantdemande());
            stmt.setDouble(5, credit.getMontantoctroye());
            stmt.setDouble(6, credit.getTauxinteret());
            stmt.setInt(7, credit.getDureeenmois());
            stmt.setString(8, credit.getTypeCredit().name());
            stmt.setString(9, credit.getDecision().name());
            stmt.setInt(10, credit.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur DAO update: " + e.getMessage());
        }
        return false;
    }

    // DELETE
    public boolean delete(int id) {
        String sql = "DELETE FROM credit WHERE id=?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur DAO delete: " + e.getMessage());
        }
        return false;
    }

    // Méthode utilitaire
    private Credit extractCredit(ResultSet rs) throws SQLException {
        Integer empId = rs.getObject("employee_id") != null ? rs.getInt("employee_id") : null;
        Integer profId = rs.getObject("professional_id") != null ? rs.getInt("professional_id") : null;

        Credit c = new Credit(
                empId,
                profId,
                rs.getDate("datedecredit"),
                rs.getDouble("montantdemande"),
                rs.getDouble("montantoctroye"),
                rs.getDouble("tauxinteret"),
                rs.getInt("dureeenmois"),
                TypeCredit.valueOf(rs.getString("typecredit")),
                Decision.valueOf(rs.getString("decision"))
        );
        c.setId(rs.getInt("id"));
        return c;
    }
}

