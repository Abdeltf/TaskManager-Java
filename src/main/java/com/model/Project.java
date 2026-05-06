package com.taskmanager.model;

import com.taskmanager.interfaces.Manageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert ein Projekt, das mehrere Aufgaben enthält.
 *
 * <p>Ein Projekt ist der übergeordnete Container für Aufgaben.
 * Es kennt sein Start- und Enddatum sowie den aktuellen Status.</p>
 */
public class Project implements Manageable {

    private int id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private final List<Task> tasks;

    // ── Konstruktoren ──────────────────────────────────────────────────────────

    /** Konstruktor für neue Projekte (ohne ID, kommt von der DB). */
    public Project(String title, String description, LocalDate startDate, LocalDate endDate) {
        this.title       = title;
        this.description = description;
        this.startDate   = startDate;
        this.endDate     = endDate;
        this.tasks       = new ArrayList<>();
    }

    /** Konstruktor zum Laden aus der Datenbank (mit ID). */
    public Project(int id, String title, String description,
                   LocalDate startDate, LocalDate endDate) {
        this(title, description, startDate, endDate);
        this.id = id;
    }

    // ── Geschäftslogik ─────────────────────────────────────────────────────────

    /** Fügt eine Aufgabe zu diesem Projekt hinzu. */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /** Berechnet den Fortschritt als Prozentsatz (0–100). */
    public int getProgress() {
        if (tasks.isEmpty()) return 0;
        long done = tasks.stream()
                         .filter(t -> t.getStatus() == Status.DONE)
                         .count();
        return (int) ((done * 100) / tasks.size());
    }

    /** Prüft, ob das Projekt überfällig ist. */
    public boolean isOverdue() {
        return endDate != null
            && LocalDate.now().isAfter(endDate)
            && tasks.stream().anyMatch(t -> t.getStatus() != Status.DONE);
    }

    // ── Manageable-Implementierung ─────────────────────────────────────────────

    @Override
    public int getId() { return id; }

    @Override
    public String getTitle() { return title; }

    @Override
    public void setTitle(String title) { this.title = title; }

    @Override
    public String getSummary() {
        return String.format("Projekt[id=%d, titel='%s', fortschritt=%d%%, aufgaben=%d]",
                id, title, getProgress(), tasks.size());
    }

    // ── Getter / Setter ────────────────────────────────────────────────────────

    public String getDescription()            { return description; }
    public void setDescription(String desc)   { this.description = desc; }
    public LocalDate getStartDate()           { return startDate; }
    public void setStartDate(LocalDate date)  { this.startDate = date; }
    public LocalDate getEndDate()             { return endDate; }
    public void setEndDate(LocalDate date)    { this.endDate = date; }
    public List<Task> getTasks()              { return tasks; }
    public void setId(int id)                 { this.id = id; }

    @Override
    public String toString() { return title; }
}
