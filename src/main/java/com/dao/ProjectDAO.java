package com.taskmanager.dao;

import com.taskmanager.model.Project;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) für Projekte.
 *
 * <p>Das DAO-Pattern trennt die Datenbanklogik vollständig von der
 * Geschäftslogik. Wer ein Projekt speichern oder laden will, spricht
 * nur mit dieser Klasse – wie die DB intern funktioniert, ist egal.</p>
 */
public class ProjectDAO {

    private final Connection conn;

    public ProjectDAO() {
        this.conn = DatabaseManager.getInstance().getConnection();
    }

    // ── CREATE ─────────────────────────────────────────────────────────────────

    /**
     * Speichert ein neues Projekt und setzt die generierte ID.
     *
     * @param project das zu speichernde Projekt
     * @return {@code true} bei Erfolg
     */
    public boolean insert(Project project) {
        String sql = "INSERT INTO projects (title, description, start_date, end_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, project.getTitle());
            ps.setString(2, project.getDescription());
            ps.setString(3, project.getStartDate() != null ? project.getStartDate().toString() : null);
            ps.setString(4, project.getEndDate()   != null ? project.getEndDate().toString()   : null);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) project.setId(keys.getInt(1));
            }
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Fehler beim Einfügen des Projekts: " + e.getMessage());
            return false;
        }
    }

    // ── READ ───────────────────────────────────────────────────────────────────

    /** Gibt alle Projekte zurück, sortiert nach Titel. */
    public List<Project> findAll() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects ORDER BY title";
        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                projects.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Laden der Projekte: " + e.getMessage());
        }
        return projects;
    }

    /** Sucht ein Projekt anhand seiner ID. */
    public Project findById(int id) {
        String sql = "SELECT * FROM projects WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("Fehler beim Suchen des Projekts: " + e.getMessage());
        }
        return null;
    }

    // ── UPDATE ─────────────────────────────────────────────────────────────────

    /** Aktualisiert ein bestehendes Projekt in der DB. */
    public boolean update(Project project) {
        String sql = "UPDATE projects SET title=?, description=?, start_date=?, end_date=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, project.getTitle());
            ps.setString(2, project.getDescription());
            ps.setString(3, project.getStartDate() != null ? project.getStartDate().toString() : null);
            ps.setString(4, project.getEndDate()   != null ? project.getEndDate().toString()   : null);
            ps.setInt(5, project.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Fehler beim Aktualisieren des Projekts: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ─────────────────────────────────────────────────────────────────

    /** Löscht ein Projekt und (durch ON DELETE CASCADE) alle seine Aufgaben. */
    public boolean delete(int id) {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Fehler beim Löschen des Projekts: " + e.getMessage());
            return false;
        }
    }

    // ── Hilfsmethode ───────────────────────────────────────────────────────────

    /** Konvertiert eine Datenbankzeile in ein Project-Objekt (Mapping). */
    private Project mapRow(ResultSet rs) throws SQLException {
        String startStr = rs.getString("start_date");
        String endStr   = rs.getString("end_date");
        return new Project(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                startStr != null ? LocalDate.parse(startStr) : null,
                endStr   != null ? LocalDate.parse(endStr)   : null
        );
    }
}
