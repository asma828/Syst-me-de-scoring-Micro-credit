package DAO;

import Database.Database;
import Model.Incident;
import enums.Typeincident;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncidentDAO {

    public int create(Incident incident, int echeanceId) {
        String sql = "INSERT INTO incidents (date_incident, score_impact, type_incident, echeance_id) VALUES (?,?,?,?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, incident.getDateIncident());
            stmt.setDouble(2, incident.getScore());
            stmt.setString(3, incident.getTypeincident().toString());
            stmt.setInt(4, echeanceId);

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    incident.setId(rs.getInt(1));
                    return incident.getId();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur création incident: " + e.getMessage());
        }
        return -1;
    }

    public List<Incident> findByClientId(int clientId) {
        List<Incident> list = new ArrayList<>();
        String sql = "SELECT i.* FROM incidents i " +
                "JOIN echeance e ON i.echeance_id = e.id " +
                "JOIN credit c ON e.credit_id = c.id " +
                "WHERE c.client_id = ? ORDER BY i.date_incident DESC";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Incident inc = new Incident(
                        rs.getDate("date_incident"),
                        rs.getDouble("score_impact"),
                        Typeincident.valueOf(rs.getString("type_incident"))
                );
                inc.setId(rs.getInt("id"));
                list.add(inc);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération incidents: " + e.getMessage());
        }
        return list;
    }
}