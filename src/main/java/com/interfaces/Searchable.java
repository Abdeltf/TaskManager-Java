package com.taskmanager.interfaces;

import java.util.List;

/**
 * Generische Schnittstelle für suchbare und filterbare Listen.
 *
 * @param <T> der Typ der Elemente in der Liste (z.B. Task, Project)
 */
public interface Searchable<T> {

    /**
     * Sucht nach Elementen, die das Schlüsselwort im Titel oder der
     * Beschreibung enthalten (Groß-/Kleinschreibung wird ignoriert).
     *
     * @param keyword das Suchwort
     * @return gefilterte Liste der passenden Elemente
     */
    List<T> search(String keyword);

    /**
     * Sortiert die Elemente nach einem bestimmten Kriterium.
     *
     * @param criteria "priority", "deadline", "status" oder "title"
     * @return sortierte Liste
     */
    List<T> sortBy(String criteria);
}
