package com.taskmanager.model;

/**
 * Prioritätsstufen für Aufgaben.
 * Jede Stufe hat eine Farbe (für die JavaFX-UI) und eine
 * numerische Bewertung (für die Sortierung).
 */
public enum Priority {

    LOW("Niedrig", "#4CAF50", 1),
    MEDIUM("Mittel", "#FF9800", 2),
    HIGH("Hoch", "#F44336", 3);

    private final String label;
    private final String hexColor;
    private final int weight;

    Priority(String label, String hexColor, int weight) {
        this.label = label;
        this.hexColor = hexColor;
        this.weight = weight;
    }

    public String getLabel()    { return label; }
    public String getHexColor() { return hexColor; }
    public int getWeight()      { return weight; }

    @Override
    public String toString() { return label; }
}
