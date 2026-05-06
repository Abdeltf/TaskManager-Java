package com.taskmanager.ui;

import com.taskmanager.model.Priority;
import com.taskmanager.model.Project;
import com.taskmanager.model.Status;
import com.taskmanager.model.Task;
import com.taskmanager.service.ProjectService;
import com.taskmanager.service.TaskService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * JavaFX-Controller für die Hauptansicht.
 *
 * <p>Verbindet die Benutzeroberfläche (FXML) mit der Service-Schicht.
 * Der Controller kennt nur Services – nie DAOs direkt.</p>
 */
public class MainController implements Initializable {

    // ── FXML-Injektionen (entsprechen den fx:id Attributen in main.fxml) ──────

    @FXML private ListView<Project>         projectListView;
    @FXML private TableView<Task>           taskTableView;
    @FXML private TableColumn<Task, String> colTitle;
    @FXML private TableColumn<Task, String> colPriority;
    @FXML private TableColumn<Task, String> colStatus;
    @FXML private TableColumn<Task, String> colDeadline;
    @FXML private TextField                 searchField;
    @FXML private ComboBox<String>          sortComboBox;
    @FXML private Label                     statsLabel;
    @FXML private Label                     progressLabel;

    // ── Services ───────────────────────────────────────────────────────────────

    private final TaskService    taskService    = new TaskService();
    private final ProjectService projectService = new ProjectService();

    // ── Zustand ────────────────────────────────────────────────────────────────

    private Project selectedProject;
    private ObservableList<Task> taskList = FXCollections.observableArrayList();

    // ── Initialisierung ────────────────────────────────────────────────────────

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        setupSortComboBox();
        setupProjectList();
        setupSearch();
        loadProjects();
    }

    /** Konfiguriert die Tabellenspalten mit Property-Bindings und Formatierung. */
    private void setupTableColumns() {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));

        // Priorität mit Farbkodierung
        colPriority.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getPriority().getLabel()));
        colPriority.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    Task task = getTableRow().getItem();
                    if (task != null) {
                        setTextFill(Color.web(task.getPriority().getHexColor()));
                        setStyle("-fx-font-weight: bold;");
                    }
                }
            }
        });

        colStatus.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getStatus().getLabel()));

        colDeadline.setCellValueFactory(cd -> {
            LocalDate d = cd.getValue().getDeadline();
            String text = d != null ? d.toString() : "—";
            if (cd.getValue().isOverdue()) text += " ⚠";
            return new SimpleStringProperty(text);
        });

        taskTableView.setItems(taskList);
    }

    private void setupSortComboBox() {
        sortComboBox.setItems(FXCollections.observableArrayList(
                "Priorität", "Frist", "Status", "Titel"));
        sortComboBox.getSelectionModel().selectFirst();
        sortComboBox.setOnAction(e -> handleSort());
    }

    private void setupProjectList() {
        projectListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Project p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getTitle());
            }
        });
        projectListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    selectedProject = newVal;
                    loadTasksForProject(newVal);
                });
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, old, neu) -> {
            if (neu.isBlank()) {
                loadTasksForProject(selectedProject);
            } else {
                List<Task> results = taskService.search(neu);
                taskList.setAll(results);
            }
        });
    }

    // ── Daten laden ────────────────────────────────────────────────────────────

    private void loadProjects() {
        List<Project> projects = projectService.getAllProjects();
        projectListView.setItems(FXCollections.observableArrayList(projects));
        updateStats();
    }

    private void loadTasksForProject(Project project) {
        taskList.clear();
        if (project == null) return;

        List<Task> tasks = taskService.getTasksByProject(project.getId());
        taskList.setAll(tasks);

        Project full = projectService.getProjectWithTasks(project.getId());
        progressLabel.setText("Fortschritt: " + full.getProgress() + "%");
        updateStats();
    }

    private void updateStats() {
        statsLabel.setText(taskService.getStatisticsSummary());
    }

    // ── Event-Handler ──────────────────────────────────────────────────────────

    @FXML
    private void handleSort() {
        String selected = sortComboBox.getSelectionModel().getSelectedItem();
        String criteria = switch (selected) {
            case "Priorität" -> "priority";
            case "Frist"     -> "deadline";
            case "Status"    -> "status";
            default          -> "title";
        };
        List<Task> sorted = taskService.sortBy(criteria);
        taskList.setAll(sorted);
    }

    @FXML
    private void handleNewTask() {
        if (selectedProject == null) {
            showAlert("Kein Projekt ausgewählt", "Bitte wählen Sie zuerst ein Projekt aus.");
            return;
        }
        // Beispiel: Neue Aufgabe mit Standardwerten erstellen
        Task newTask = new Task(
                selectedProject.getId(),
                "Neue Aufgabe",
                "Beschreibung...",
                Priority.MEDIUM,
                LocalDate.now().plusWeeks(1)
        );
        taskService.createTask(newTask);
        loadTasksForProject(selectedProject);
    }

    @FXML
    private void handleDeleteTask() {
        Task selected = taskTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Keine Auswahl", "Bitte wählen Sie eine Aufgabe aus.");
            return;
        }
        taskService.deleteTask(selected.getId());
        loadTasksForProject(selectedProject);
    }

    @FXML
    private void handleMarkDone() {
        Task selected = taskTableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        selected.setStatus(Status.DONE);
        taskService.updateTask(selected);
        loadTasksForProject(selectedProject);
    }

    // ── Hilfsmethoden ─────────────────────────────────────────────────────────

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
