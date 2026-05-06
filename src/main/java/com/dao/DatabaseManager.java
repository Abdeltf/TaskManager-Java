package com.taskmanager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Verwaltet die SQLite-Datenbankverbindung (Singleton-Pattern).
 *
 * <p>Verwendet das Singleton-Entwurfsmuster, damit immer nur eine
 * Verbindung zur Datenbank offen ist. SQLite speichert alles in
 * einer einzigen .db-Datei – kein Server nötig.</p>
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:taskmanager.db";
    private static DatabaseManager instance;
    private Connection connection;

    // ── Singleton ──────────────────────────────────────────────────────────────

    private DatabaseManager() {
        connect();
        initializeTables();
    }

    /** Gibt die einzige Instanz zurück (Thread-safe). */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // ── Verbindung ─────────────────────────────────────────────────────────────

    private void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Datenbankverbindung hergestellt: " + DB_URL);
        } catch (SQLException e) {
            throw new RuntimeException("Verbindung zur Datenbank fehlgeschlagen: " + e.getMessage(), e);
        }
    }

    /** Gibt die aktive Verbindung zurück. Stellt sie wieder her, falls nötig. */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            connect();
        }
        return connection;
    }

    /** Schließt die Verbindung (aufrufen beim Beenden der App). */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Datenbankverbindung geschlossen.");
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Schließen: " + e.getMessage());
        }
    }

    // ── Tabellen erstellen ─────────────────────────────────────────────────────

    /**
     * Erstellt die Tabellen beim ersten Start (falls nicht vorhanden).
     * "IF NOT EXISTS" verhindert Fehler bei weiteren Starts.
     */
    private void initializeTables() {
        try (Statement stmt = connection.createStatement()) {

            // Projekte-Tabelle
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS projects (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    title       TEXT    NOT NULL,
                    description TEXT,
                    start_date  TEXT,
                    end_date    TEXT
                )
            """);

            // Aufgaben-Tabelle (referenziert projects über Fremdschlüssel)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS tasks (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    project_id  INTEGER NOT NULL,
                    title       TEXT    NOT NULL,
                    description TEXT,
                    priority    TEXT    NOT NULL DEFAULT 'MEDIUM',
                    status      TEXT    NOT NULL DEFAULT 'TODO',
                    deadline    TEXT,
                    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
                )
            """);

            System.out.println("Datenbanktabellen bereit.");

        } catch (SQLException e) {
            throw new RuntimeException("Tabellenerstellung fehlgeschlagen: " + e.getMessage(), e);
        }
    }
}
