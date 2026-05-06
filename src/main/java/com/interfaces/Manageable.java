package com.taskmanager.interfaces;

/**
 * Schnittstelle für alle verwaltbaren Elemente (Aufgaben, Projekte).
 * Definiert die grundlegenden CRUD-Operationen.
 */
public interface Manageable {

    /** Gibt die eindeutige ID zurück. */
    int getId();

    /** Gibt den Titel zurück. */
    String getTitle();

    /** Setzt den Titel. */
    void setTitle(String title);

    /**
     * Gibt eine kompakte Zusammenfassung des Elements zurück.
     * Nützlich für Logs und Debugging.
     */
    String getSummary();
}
