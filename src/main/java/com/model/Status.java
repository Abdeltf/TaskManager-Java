package com.taskmanager.model;

/**
 * Mögliche Zustände einer Aufgabe im Workflow.
 */
public enum Status {

    TODO("Offen"),
    IN_PROGRESS("In Bearbeitung"),
    DONE("Abgeschlossen"),
    CANCELLED("Abgebrochen");

    private final String label;

    Status(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}
