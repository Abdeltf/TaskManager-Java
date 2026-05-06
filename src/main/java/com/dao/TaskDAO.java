package com.taskmanager.dao;

import com.taskmanager.model.Priority;
import com.taskmanager.model.Status;
import com.taskmanager.model.Task;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) für Aufgaben.
 * Alle Datenbankoperationen für die Tabelle "tasks".
 */
public class TaskDAO {

    private final Connection conn;

    public TaskDAO() {
        this.conn = DatabaseManager.getInstance().getConnection();
    }

    // ── CREATE ─────────────────────────────────────────────────────────────────

    public boolean insert(Task task) {
        String sql = """
            INSERT INTO tasks (project_id, title, description, priority, status, deadline)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,    task.getProjectId());
            ps.setString(2, task.getTitle());
            ps.setString(3, task.getDescription());
            ps.setString(4, task.getPriority().name());
            ps.setString(5, task.getStatus().name());
            ps.setString(6, task.getDeadline() != null ? task.getDeadline().toString() : null);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) task.setId(keys.getInt(1));
            }
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Fehler beim Einfügen der Aufgabe: " + e.getMessage());
            return false;
        }
    }

    // ── READ ───────────────────────────────────────────────────────────────────

    /** Gibt alle Aufgaben eines Projekts zurück. */
    public List<Task> findByProjectId(int projectId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE project_id = ? ORDER BY deadline ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) tasks.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Fehler beim Laden der Aufgaben: " + e.getMessage());
        }
        return tasks;
    }

    /** Gibt alle Aufgaben zurück (unabhängig vom Projekt). */
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY priority DESC, deadline ASC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) tasks.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Fehler beim Laden aller Aufgaben: " + e.getMessage());
        }
        return tasks;
    }

    /** Sucht Aufgaben nach Schlüsselwort in Titel oder Beschreibung. */
    public List<Task> search(String keyword) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE LOWER(title) LIKE ? OR LOWER(description) LIKE ?";
        String pattern = "%" + keyword.toLowerCase() + "%";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) tasks.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Fehler bei der Suche: " + e.getMessage());
        }
        return tasks;
    }

    // ── UPDATE ─────────────────────────────────────────────────────────────────

    public boolean update(Task task) {
        String sql = """
            UPDATE tasks
            SET title=?, description=?, priority=?, status=?, deadline=?
            WHERE id=?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getPriority().name());
            ps.setString(4, task.getStatus().name());
            ps.setString(5, task.getDeadline() != null ? task.getDeadline().toString() : null);
            ps.setInt(6,    task.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Fehler beim Aktualisieren der Aufgabe: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ─────────────────────────────────────────────────────────────────

    public boolean delete(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Fehler beim Löschen der Aufgabe: " + e.getMessage());
            return false;
        }
    }

    // ── Hilfsmethode ───────────────────────────────────────────────────────────

    private Task mapRow(ResultSet rs) throws SQLException {
        String deadlineStr = rs.getString("deadline");
        return new Task(
                rs.getInt("id"),
                rs.getInt("project_id"),
                rs.getString("title"),
                rs.getString("description"),
                Priority.valueOf(rs.getString("priority")),
                Status.valueOf(rs.getString("status")),
                deadlineStr != null ? LocalDate.parse(deadlineStr) : null
        );
    }
}
