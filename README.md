<div align="center">

# 🚀 TaskManager

### Moderne Java Desktop-Anwendung für Aufgaben- & Projektmanagement

<p>
  <b>Entwickelt mit Java, JavaFX, SQLite & Maven</b>
</p>

<p>
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk" />
  <img src="https://img.shields.io/badge/JavaFX-21-blue?style=for-the-badge" />
  <img src="https://img.shields.io/badge/SQLite-Datenbank-green?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Maven-Build-red?style=for-the-badge&logo=apachemaven" />
</p>

<p>
  <i>Eine moderne und skalierbare Desktop-Anwendung zur Demonstration professioneller Softwareentwicklungs- und Architekturprinzipien.</i>
</p>

</div>

---

# 📌 Überblick

TaskManager ist eine professionelle Desktop-Anwendung, entwickelt mit <b>Java</b> und <b>JavaFX</b>.

Das Ziel dieses Projekts war es nicht nur, ein funktionales Aufgabenverwaltungssystem zu entwickeln, sondern ebenfalls folgende Fähigkeiten zu demonstrieren:

✅ Saubere Softwarearchitektur
✅ Objektorientierte Programmierung
✅ Datenbankintegration
✅ Wartbare Codestruktur
✅ Praxisnahe Softwareentwicklungsprinzipien

Dieses Projekt entstand im Rahmen meiner persönlichen Weiterentwicklung im Bereich Softwareentwicklung und zeigt meine Motivation für eine Ausbildung als:

<h3 align="center">🇩🇪 Fachinformatiker für Anwendungsentwicklung</h3>

---

# ✨ Funktionen

## 📁 Projektmanagement

* Projekte erstellen und organisieren
* Dynamische Fortschrittsanzeige
* Strukturierte Projektübersicht

## ✅ Aufgabenverwaltung

* Aufgaben erstellen, bearbeiten und löschen
* Aufgaben als erledigt markieren
* Statusverwaltung
* Prioritätensystem
* Deadline-Verwaltung
* Erkennung überfälliger Aufgaben

## 🔍 Suche & Sortierung

* Echtzeit-Aufgabensuche
* Sortierung nach:

  * Priorität
  * Deadline
  * Status
  * Titel

## 💾 Datenbankintegration

* SQLite-Datenbank
* Permanente Datenspeicherung
* Automatisches Datenbankmanagement
* Kein externer Server erforderlich

## 🎨 Moderne Desktop-Oberfläche

* JavaFX Benutzeroberfläche
* Responsives Layout
* CSS-Styling
* FXML-basierte UI-Struktur

---

# 🏗️ Softwarearchitektur

<div align="center">

```text
UI Layer (JavaFX / FXML)
        ↓
Service Layer
        ↓
DAO Layer
        ↓
SQLite Database
```

</div>

Diese mehrschichtige Architektur verbessert:

* Skalierbarkeit
* Wartbarkeit
* Lesbarkeit
* Testbarkeit
* Erweiterbarkeit

und orientiert sich an professionellen Entwicklungsstandards moderner Softwareprojekte.

---

# 🛠️ Verwendete Technologien

<table>
<tr>
<th>Technologie</th>
<th>Zweck</th>
</tr>

<tr>
<td><b>Java 17</b></td>
<td>Programmiersprache</td>
</tr>

<tr>
<td><b>JavaFX 21</b></td>
<td>Desktop-GUI-Framework</td>
</tr>

<tr>
<td><b>SQLite</b></td>
<td>Eingebettete Datenbank</td>
</tr>

<tr>
<td><b>Maven</b></td>
<td>Abhängigkeits- & Build-Management</td>
</tr>

<tr>
<td><b>FXML</b></td>
<td>Definition der Benutzeroberfläche</td>
</tr>

<tr>
<td><b>CSS</b></td>
<td>Design der Benutzeroberfläche</td>
</tr>

</table>

---

# 🧠 Objektorientierte Konzepte

Dieses Projekt demonstriert den praktischen Einsatz von:

* Kapselung
* Vererbung
* Abstraktion
* Interfaces
* Enums
* Mehrschichtiger Architektur
* Service-orientiertem Design
* Wiederverwendbaren Komponenten
* Clean-Code-Prinzipien

### Implementierte Interfaces

```java
Manageable
Searchable
```

---

# 📂 Projektstruktur

```text
src/main/java/com/taskmanager
│
├── dao
│   ├── DatabaseManager.java
│   ├── ProjectDAO.java
│   └── TaskDAO.java
│
├── interfaces
│   ├── Manageable.java
│   └── Searchable.java
│
├── model
│   ├── Project.java
│   ├── Task.java
│   ├── RecurringTask.java
│   ├── Priority.java
│   └── Status.java
│
├── service
│   ├── ProjectService.java
│   └── TaskService.java
│
├── ui
│   └── MainController.java
│
└── Main.java
```

---

# 🎯 Architekturentscheidungen

## Warum JavaFX?

JavaFX bietet einen modernen und strukturierten Ansatz für Desktop-Anwendungen mit klarer Trennung zwischen Benutzeroberfläche und Business-Logik.

## Warum SQLite?

SQLite ermöglicht eine leichte lokale Datenspeicherung ohne zusätzliche Infrastruktur und macht die Anwendung einfach portierbar.

## Warum eine mehrschichtige Architektur?

Eine klare Architektur verbessert die Organisation des Codes und orientiert sich an professionellen Entwicklungsstandards.

---

# 📈 Beispiel-Funktionen

## 🔥 Aufgabenpriorisierung

Aufgaben werden visuell nach Priorität kategorisiert:

* 🔴 Hohe Priorität
* 🟡 Mittlere Priorität
* 🟢 Niedrige Priorität

## ⏰ Deadline-Überwachung

Überfällige Aufgaben werden automatisch erkannt und hervorgehoben.

## 📊 Fortschrittsverfolgung

Der Projektfortschritt wird dynamisch anhand abgeschlossener Aufgaben berechnet.

---

# 🚀 Projekt starten

## Voraussetzungen

```bash
Java 17+
Maven
```

## Repository klonen

```bash
git clone https://github.com/yourusername/taskmanager.git
cd taskmanager
```

## Anwendung starten

```bash
mvn javafx:run
```

---

# 📚 Was ich gelernt habe

Während dieses Projekts konnte ich meine Kenntnisse erweitern in:

* Java Desktop-Entwicklung
* Softwarearchitektur
* JavaFX UI-Design
* Datenbankintegration
* Objektorientiertem Design
* MVC-inspirierter Struktur
* Clean-Code-Prinzipien
* Maven Dependency Management
* Modularer Anwendungsentwicklung

---

# 🔮 Geplante Erweiterungen

Geplante Funktionen für zukünftige Versionen:

* Benutzeranmeldung
* Kategorien & Tags für Aufgaben
* Drag & Drop Organisation
* Benachrichtigungen & Erinnerungen
* Export-/Import-Funktionen
* Unit- & Integrationstests
* REST-API Integration
* Dark Mode

---

# 🖼️ Screenshots

<div align="center">

<i>Screenshots können hier später hinzugefügt werden.</i>

</div>

```text
/docs/images/main-ui.png
/docs/images/task-management.png
```

---

# 💡 Motivation

Dieses Projekt spiegelt meine Leidenschaft für Softwareentwicklung sowie meine Motivation wider, mich kontinuierlich als Entwickler weiterzuentwickeln.

Während der Entwicklung lag mein Fokus besonders auf:

* verständlichem Code
* wartbaren Strukturen
* sauberen Softwaredesign-Prinzipien
* der Entwicklung einer praxisnahen Anwendung

---

# 🤝 Kontakt

Falls Sie dieses Projekt im Rahmen meiner Ausbildungsbewerbung ansehen:

Vielen Dank, dass Sie sich die Zeit nehmen, mein Projekt anzusehen.

Ich bin hoch motiviert, meine Kenntnisse weiter auszubauen, praktische Erfahrungen zu sammeln und mich professionell in einem realen Entwicklungsumfeld weiterzuentwickeln.

---

<div align="center">

# ⭐ Vielen Dank für Ihren Besuch

<p>
  <i>Falls Ihnen das Projekt gefällt, freue ich mich über einen ⭐ auf GitHub.</i>
</p>

</div>
