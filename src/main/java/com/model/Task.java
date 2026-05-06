package com.taskmanager.model;

import com.taskmanager.interfaces.Manageable;
import java.time.LocalDate;

/**
 * Repräsentiert eine einzelne Aufgabe in einem Projekt.
 *
 * <p>Implementiert {@link Manageable}, um eine einheitliche
 * Schnittstelle für alle verwaltbaren Elemente zu gewährleisten.</p>
 */
public class Task implements Manageable {

    private int id;
    private int projectId;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private LocalDate deadline;

    // ── Konstruktoren ──────────────────────────────────────────────────────────

    /** Konstruktor für neue Aufgaben. */
    public Task(int projectId, String title, String description,
                Priority priority, LocalDate deadline) {
        this.projectId   = projectId;
        this.title       = title;
        this.description = description;
        this.priority    = priority;
        this.deadline    = deadline;
        this.status      = Status.TODO; // Standardstatus
    }

    /** Vollständiger Konstruktor (zum Laden aus der DB). */
    public Task(int id, int projectId, String title, String description,
                Priority priority, Status status, LocalDate deadline) {
        this(projectId, title, description, priority, deadline);
        this.id     = id;
        this.status = status;
    }

    // ── Geschäftslogik ─────────────────────────────────────────────────────────

    /**
     * Prüft, ob die Aufgabe überfällig ist.
     * Eine Aufgabe gilt als überfällig, wenn ihre Frist abgelaufen
     * ist und sie noch nicht abgeschlossen oder abgebrochen wurde.
     */
    public boolean isOverdue() {
        return deadline != null
            && LocalDate.now().isAfter(deadline)
            && status != Status.DONE
            && status != Status.CANCELLED;
    }

    /**
     * Berechnet die verbleibenden Tage bis zur Frist.
     * Negative Werte bedeuten, dass die Frist abgelaufen ist.
     *
     * @return Anzahl der verbleibenden Tage, oder Integer.MAX_VALUE wenn kein Datum
     */
    public long getDaysUntilDeadline() {
        if (deadline == null) return Long.MAX_VALUE;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), deadline);
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
        return String.format("Aufgabe[id=%d, titel='%s', priorität=%s, status=%s, fällig=%s]",
                id, title, priority, status, deadline);
    }

    // ── Getter / Setter ────────────────────────────────────────────────────────

    public int getProjectId()                  { return projectId; }
    public void setProjectId(int projectId)    { this.projectId = projectId; }
    public String getDescription()             { return description; }
    public void setDescription(String desc)    { this.description = desc; }
    public Priority getPriority()              { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public Status getStatus()                  { return status; }
    public void setStatus(Status status)       { this.status = status; }
    public LocalDate getDeadline()             { return deadline; }
    public void setDeadline(LocalDate date)    { this.deadline = date; }
    public void setId(int id)                  { this.id = id; }

    @Override
    public String toString() { return title; }
}
