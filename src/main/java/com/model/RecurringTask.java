package com.taskmanager.model;

import java.time.LocalDate;

/**
 * Erweitert {@link Task} um eine Wiederholungslogik.
 *
 * <p>Dieses Beispiel demonstriert Vererbung (Inheritance) in Java.
 * Eine wiederkehrende Aufgabe kennt ihr Intervall und kann
 * automatisch die nächste Frist berechnen.</p>
 */
public class RecurringTask extends Task {

    /** Mögliche Wiederholungsintervalle. */
    public enum Interval {
        DAILY("Täglich", 1),
        WEEKLY("Wöchentlich", 7),
        MONTHLY("Monatlich", 30);

        private final String label;
        private final int days;

        Interval(String label, int days) {
            this.label = label;
            this.days  = days;
        }

        public String getLabel() { return label; }
        public int getDays()     { return days; }
    }

    private Interval interval;
    private int occurrencesLeft; // -1 = unendlich

    // ── Konstruktoren ──────────────────────────────────────────────────────────

    public RecurringTask(int projectId, String title, String description,
                         Priority priority, LocalDate firstDeadline,
                         Interval interval, int occurrencesLeft) {
        super(projectId, title, description, priority, firstDeadline);
        this.interval        = interval;
        this.occurrencesLeft = occurrencesLeft;
    }

    // ── Erweiterte Logik ───────────────────────────────────────────────────────

    /**
     * Setzt die Aufgabe auf "Erledigt" und berechnet den nächsten Termin,
     * sofern noch Wiederholungen übrig sind.
     *
     * @return {@code true} wenn eine nächste Instanz geplant wurde,
     *         {@code false} wenn alle Wiederholungen abgeschlossen sind.
     */
    public boolean completeAndReschedule() {
        setStatus(Status.DONE);

        if (occurrencesLeft == 0) return false; // keine mehr

        // Nächsten Termin berechnen
        LocalDate nextDeadline = getDeadline().plusDays(interval.getDays());
        setDeadline(nextDeadline);
        setStatus(Status.TODO);

        if (occurrencesLeft > 0) occurrencesLeft--;
        return true;
    }

    /** Prüft, ob es noch weitere Wiederholungen gibt. */
    public boolean hasMoreOccurrences() {
        return occurrencesLeft != 0;
    }

    // ── Überschriebene Methoden ────────────────────────────────────────────────

    @Override
    public String getSummary() {
        return super.getSummary() + String.format(
                " | Wiederholung=%s, verbleibend=%s",
                interval.getLabel(),
                occurrencesLeft == -1 ? "∞" : occurrencesLeft);
    }

    // ── Getter / Setter ────────────────────────────────────────────────────────

    public Interval getInterval()               { return interval; }
    public void setInterval(Interval interval)  { this.interval = interval; }
    public int getOccurrencesLeft()             { return occurrencesLeft; }
    public void setOccurrencesLeft(int count)   { this.occurrencesLeft = count; }
}
