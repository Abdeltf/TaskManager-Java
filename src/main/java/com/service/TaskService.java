package com.taskmanager.service;

import com.taskmanager.dao.TaskDAO;
import com.taskmanager.interfaces.Searchable;
import com.taskmanager.model.Priority;
import com.taskmanager.model.Status;
import com.taskmanager.model.Task;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service-Schicht für Aufgaben.
 *
 * <p>Enthält die Geschäftslogik: Suchen, Sortieren, Filtern.
 * Implementiert {@link Searchable} als generische Schnittstelle.</p>
 *
 * <p>Die Service-Schicht steht zwischen der DAO-Schicht (Datenbank)
 * und der UI-Schicht (JavaFX) – ein klassisches 3-Schichten-Modell.</p>
 */
public class TaskService implements Searchable<Task> {

    private final TaskDAO taskDAO;

    public TaskService() {
        this.taskDAO = new TaskDAO();
    }

    // ── CRUD-Delegierung ───────────────────────────────────────────────────────

    public boolean createTask(Task task)  { return taskDAO.insert(task); }
    public boolean updateTask(Task task)  { return taskDAO.update(task); }
    public boolean deleteTask(int id)     { return taskDAO.delete(id); }
    public List<Task> getAllTasks()       { return taskDAO.findAll(); }
    public List<Task> getTasksByProject(int projectId) {
        return taskDAO.findByProjectId(projectId);
    }

    // ── Searchable-Implementierung ─────────────────────────────────────────────

    /**
     * Sucht Aufgaben nach Schlüsselwort (Algorithmus: Stream + toLowerCase).
     * Die Suche ist case-insensitive und durchsucht Titel und Beschreibung.
     */
    @Override
    public List<Task> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return getAllTasks();
        return taskDAO.search(keyword.trim());
    }

    /**
     * Sortiert alle Aufgaben nach dem angegebenen Kriterium.
     *
     * <p>Unterstützte Kriterien:</p>
     * <ul>
     *   <li>"priority" – höchste zuerst (HIGH → MEDIUM → LOW)</li>
     *   <li>"deadline"  – früheste zuerst (null-Werte kommen ans Ende)</li>
     *   <li>"status"    – alphabetisch nach Status-Label</li>
     *   <li>"title"     – alphabetisch (A → Z)</li>
     * </ul>
     */
    @Override
    public List<Task> sortBy(String criteria) {
        List<Task> tasks = getAllTasks();

        Comparator<Task> comparator = switch (criteria.toLowerCase()) {
            case "priority" -> Comparator.comparingInt(t -> -t.getPriority().getWeight());
            case "deadline" -> Comparator.comparing(
                    Task::getDeadline,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            case "status"   -> Comparator.comparing(t -> t.getStatus().getLabel());
            case "title"    -> Comparator.comparing(Task::getTitle,
                    String.CASE_INSENSITIVE_ORDER);
            default         -> Comparator.comparing(Task::getId);
        };

        return tasks.stream().sorted(comparator).collect(Collectors.toList());
    }

    // ── Weitere Filter-Methoden ────────────────────────────────────────────────

    /** Gibt nur überfällige Aufgaben zurück. */
    public List<Task> getOverdueTasks() {
        return getAllTasks().stream()
                .filter(Task::isOverdue)
                .collect(Collectors.toList());
    }

    /** Filtert Aufgaben nach Status. */
    public List<Task> filterByStatus(Status status) {
        return getAllTasks().stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    /** Filtert Aufgaben nach Priorität. */
    public List<Task> filterByPriority(Priority priority) {
        return getAllTasks().stream()
                .filter(t -> t.getPriority() == priority)
                .collect(Collectors.toList());
    }

    /** Gibt eine Zusammenfassung als Text zurück (für Debugging / Logs). */
    public String getStatisticsSummary() {
        List<Task> all = getAllTasks();
        long done     = all.stream().filter(t -> t.getStatus() == Status.DONE).count();
        long overdue  = all.stream().filter(Task::isOverdue).count();
        long highPrio = all.stream().filter(t -> t.getPriority() == Priority.HIGH).count();

        return String.format(
                "Gesamt: %d | Erledigt: %d | Überfällig: %d | Hohe Priorität: %d",
                all.size(), done, overdue, highPrio
        );
    }
}
