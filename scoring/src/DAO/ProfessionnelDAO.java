package DAO;

import Database.Database;
import Model.Professionnel;

import java.sql.*;

public class ProfessionnelDAO {

    // Créer un professionnel
    public int create(Professionnel professionnel) {
        String sql = "INSERT INTO Professional (nom,prenom,datedenaissance,ville,investissement,placement,nombreEnfants,situation_familiale,score,revenu,immatriculationfiscale,secteuractivité,activité) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, professionnel.getNom());
            stmt.setString(2, professionnel.getPrenom());
            stmt.setDate(3, java.sql.Date.valueOf(professionnel.getDatedenaissance()));
            stmt.setString(4, professionnel.getVille());
            stmt.setBoolean(5, professionnel.isInvestissement());
            stmt.setBoolean(6, professionnel.isPlacement());
            stmt.setInt(7, professionnel.getNombreEnfants());
            stmt.setString(8, professionnel.getSituation_Familiale().toString());
            stmt.setDouble(9, professionnel.getScore());
            stmt.setDouble(10, professionnel.getRevenu());
            stmt.setString(11, professionnel.getImmatriculationfiscale());
            stmt.setString(12, professionnel.getSecteuractivite().toString());
            stmt.setString(13, professionnel.getActivité());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        professionnel.setId(id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du professionnel: " + e.getMessage());
        }
        return -1;
    }

    // Modifier un professionnel
    public boolean update(Professionnel professionnel) {
        String sql = "UPDATE Professional SET nom=?, prenom=?, datedenaissance=?, ville=?, investissement=?, placement=?, nombreEnfants=?, situation_familiale=?, score=?, revenu=?, immatriculationfiscale=?, secteuractivité=?, activité=? WHERE id=?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, professionnel.getNom());
            stmt.setString(2, professionnel.getPrenom());
            stmt.setDate(3, java.sql.Date.valueOf(professionnel.getDatedenaissance()));
            stmt.setString(4, professionnel.getVille());
            stmt.setBoolean(5, professionnel.isInvestissement());
            stmt.setBoolean(6, professionnel.isPlacement());
            stmt.setInt(7, professionnel.getNombreEnfants());
            stmt.setString(8, professionnel.getSituation_Familiale().toString());
            stmt.setDouble(9, professionnel.getScore());
            stmt.setDouble(10, professionnel.getRevenu());
            stmt.setString(11, professionnel.getImmatriculationfiscale());
            stmt.setString(12, professionnel.getSecteuractivite().toString());
            stmt.setString(13, professionnel.getActivité());
            stmt.setInt(14, professionnel.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du professionnel: " + e.getMessage());
            return false;
        }
    }

    // Récupérer par ID
    public Professionnel findById(int id) {
        String sql = "SELECT * FROM Professional WHERE id=?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Professionnel(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getDate("date_naissance").toLocalDate(),
                            rs.getString("ville"),
                            rs.getInt("nombreEnfants"),
                            rs.getBoolean("investissement"),
                            rs.getBoolean("placement"),
                            enums.Situation_Familiale.valueOf(rs.getString("situation_familiale")),
                            rs.getInt("score"),
                            rs.getDouble("revenu"),
                            rs.getString("immatriculationfiscale"),
                            enums.Secteuractivite.valueOf(rs.getString("secteuractivité").toUpperCase()),
                            rs.getString("activité")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du professionnel: " + e.getMessage());
        }
        return null;
    }

    // Supprimer un professionnel
    public boolean delete(int id) {
        String sql = "DELETE FROM Professional WHERE id=?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du professionnel: " + e.getMessage());
            return false;
        }
    }
}
