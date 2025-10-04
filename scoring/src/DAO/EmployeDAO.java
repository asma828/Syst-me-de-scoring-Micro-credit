package DAO;

import Database.Database;
import Model.Employe;
import enums.Secteur;
import enums.Situation_Familiale;
import enums.TypeContrat;

import java.sql.*;

public class EmployeDAO {
    public int create(Employe employe) {
        String sql = "INSERT INTO employee (nom,prenom,date_naissance,ville,investissement,placement,nombreEnfants,situation_familiale,score,salaire,anciennete,poste,typecontrat,secteur) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, employe.getNom());
            stmt.setString(2, employe.getPrenom());
            stmt.setDate(3, java.sql.Date.valueOf(employe.getDatedenaissance()));
            stmt.setString(4, employe.getVille());
            stmt.setBoolean(5, employe.isInvestissement());
            stmt.setBoolean(6, employe.isPlacement());
            stmt.setInt(7, employe.getNombreEnfants());
            stmt.setString(8, employe.getSituation_Familiale().toString());
            stmt.setDouble(9, employe.getScore());
            stmt.setDouble(10, employe.getSalaire());
            stmt.setInt(11, employe.getAncienneté());
            stmt.setString(12, employe.getPoste());
            stmt.setString(13, employe.getTypeContrat().toString());
            stmt.setString(14, employe.getSecteur().toString());


            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        employe.setId(id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du employee: " + e.getMessage());
        }
        return -1;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du employee: " + e.getMessage());
        }
        return false;
    }

    public boolean update(Employe employe) {
        String sql = "UPDATE employee SET nom=?, prenom=?, date_naissance=?, ville=?, investissement=?, placement=?, nombreEnfants=?, situation_familiale=?, score=?, salaire=?, anciennete=?, poste=?, typecontrat=?, secteur=? WHERE id=?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employe.getNom());
            stmt.setString(2, employe.getPrenom());
            stmt.setDate(3, java.sql.Date.valueOf(employe.getDatedenaissance()));
            stmt.setString(4, employe.getVille());
            stmt.setBoolean(5, employe.isInvestissement());
            stmt.setBoolean(6, employe.isPlacement());
            stmt.setInt(7, employe.getNombreEnfants());
            stmt.setString(8, employe.getSituation_Familiale().toString());
            stmt.setDouble(9, employe.getScore());
            stmt.setDouble(10, employe.getSalaire());
            stmt.setInt(11, employe.getAncienneté());
            stmt.setString(12, employe.getPoste());
            stmt.setString(13, employe.getTypeContrat().toString());
            stmt.setString(14, employe.getSecteur().toString());
            stmt.setInt(15, employe.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de l'employé: " + e.getMessage());
            return false;
        }
    }

    public Employe findById(int id) {
        String sql = "SELECT * FROM employee WHERE id=?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Employe(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getDate("date_naissance").toLocalDate(),
                            rs.getString("ville"),
                            rs.getInt("nombreEnfants"),
                            rs.getBoolean("investissement"),
                            rs.getBoolean("placement"),
                            Situation_Familiale.valueOf(rs.getString("situation_familiale")),
                            rs.getInt("score"),
                            rs.getDouble("salaire"),
                            rs.getInt("anciennete"),
                            rs.getString("poste"),
                            TypeContrat.valueOf(rs.getString("typecontrat")),
                            Secteur.valueOf(rs.getString("secteur").toUpperCase())
                    );

                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'employé : " + e.getMessage());
        }
        return null;
    }



}
