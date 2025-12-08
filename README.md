# RoomShare

A simple desktop application that helps people living in the same flat or hostel room manage their shared chores and common bills in one place. Built with JavaFX and SQLite.

## 📋 Overview

RoomShare helps avoid confusion like "who cleaned last time" or "who still owes money" by keeping everything stored clearly in a local SQLite database with an easy JavaFX interface.

## ✨ Planned Features

- **Roommate & House Setup** – Add roommates with basic details and define which bills are shared by everyone (rent, wifi, gas, groceries etc.)
- **Chore Board** – Create a weekly list of chores and assign or mark them as completed by specific roommates
- **Shared Bill Tracker** – Enter each shared expense with total amount, payer, category and date
- **Balance & Settlement View** – Shows how much each person has paid and who currently owes money to whom
- **History & Filters** – Filter chores and expenses by month, category, or roommate
- **Reports & Simple Charts** – Basic summaries and charts for expenses and chores

## 🚀 Current Status

**Phase 1 - Initial Pages (Frontend Only)**
- ✅ Welcome/Home page with navigation buttons
- ✅ Setup page for adding roommates and shared bills (UI only, no database yet)

## 🛠️ Technology Stack

- **Java**: Core programming language
- **JavaFX 21**: Modern UI framework for desktop applications
- **SQLite**: Lightweight, serverless database (to be integrated)
- **Maven**: Dependency management and build tool
- **FXML**: Declarative UI design

## 📦 Prerequisites

- **Java JDK 21** or higher
- **Maven** (optional, for building from source)
- **IntelliJ IDEA** or any Java IDE (recommended)

## 🚀 Running the Application

1. **Open in IntelliJ IDEA**:
   - File → Open → Select the project folder
   - IntelliJ will automatically detect it as a Maven project

2. **Run the Application**:
   - Navigate to `src/main/java/com/example/roomshare/Launcher.java`
   - Right-click → Run 'Launcher.main()'
   - Or use the run button in your IDE

3. **Alternative: Command Line** (if Maven is installed):
   ```bash
   mvn clean javafx:run
   ```

## 📁 Project Structure

```
RoomShare/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/roomshare/
│   │   │       ├── RoomShareApplication.java    # Main application entry
│   │   │       ├── Launcher.java                 # Application launcher
│   │   │       ├── WelcomeController.java        # Welcome screen controller
│   │   │       └── SetupController.java          # Setup page controller
│   │   └── resources/
│   │       └── com/example/roomshare/
│   │           ├── welcome-view.fxml            # Welcome screen UI
│   │           └── setup-view.fxml               # Setup page UI
├── pom.xml                                      # Maven configuration
└── README.md                                    # This file
```

## 📝 Notes

- Currently, the application is in Phase 1 with only frontend pages
- Database integration will be added in the next phase
- Data entered in the Setup page is stored temporarily in memory (will be persisted to SQLite later)

