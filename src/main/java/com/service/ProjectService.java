package com.taskmanager.service;

import com.taskmanager.dao.ProjectDAO;
import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.Project;
import com.taskmanager.model.Task;

import java.util.List;

/**
 * Service-Schicht für Projekte.
 * Koordiniert ProjectDAO und TaskDAO für zusammengesetzte Operationen.
 */
public class ProjectService {

    private final ProjectDAO projectDAO;
    private final TaskDAO    taskDAO;

    public ProjectService() {
        this.projectDAO = new ProjectDAO();
        this.taskDAO    = new TaskDAO();
    }

    // ── CRUD ───────────────────────────────────────────────────────────────────

    public boolean createProject(Project project) { return projectDAO.insert(project); }
    public boolean updateProject(Project project) { return projectDAO.update(project); }
    public boolean deleteProject(int id)          { return projectDAO.delete(id); }
    public List<Project> getAllProjects()          { return projectDAO.findAll(); }

    /**
     * Lädt ein Projekt mit allen zugehörigen Aufgaben.
     * Kombiniert zwei DAO-Abfragen zu einem vollständigen Objekt.
     *
     * @param projectId die ID des gewünschten Projekts
     * @return Projekt mit geladenen Tasks, oder null wenn nicht gefunden
     */
    public Project getProjectWithTasks(int projectId) {
        Project project = projectDAO.findById(projectId);
        if (project == null) return null;

        List<Task> tasks = taskDAO.findByProjectId(projectId);
        tasks.forEach(project::addTask);
        return project;
    }

    /**
     * Gibt den Fortschritt aller Projekte als formatierten Text zurück.
     * Nützlich für ein Dashboard oder eine Übersicht.
     */
    public String getProjectsOverview() {
        List<Project> projects = getAllProjects();
        if (projects.isEmpty()) return "Keine Projekte vorhanden.";

        StringBuilder sb = new StringBuilder("Projektübersicht:\n");
        for (Project p : projects) {
            Project full = getProjectWithTasks(p.getId());
            sb.append(String.format("  • %s — Fortschritt: %d%% %s%n",
                    p.getTitle(),
                    full.getProgress(),
                    full.isOverdue() ? "⚠ ÜBERFÄLLIG" : ""));
        }
        return sb.toString();
    }
}
