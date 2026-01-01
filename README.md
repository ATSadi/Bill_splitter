# RoomShare

A desktop application that helps people living in the same flat or hostel room manage their shared chores and common bills. Built with JavaFX and SQLite.

## 📋 Overview

RoomShare helps avoid confusion like "who cleaned last time" or "who still owes money" by keeping everything organized in one place. Currently in Phase 1 with frontend pages only - database integration coming next.

## 🚀 How to Use

### Running the Application

1. Open the project in IntelliJ IDEA
2. Navigate to `src/main/java/com/example/roomshare/Launcher.java`
3. Right-click → Run 'Launcher.main()'
4. Or use command line: `mvn clean javafx:run`

### Page Guide

#### 1. **Welcome Page** (Home)
- **Purpose**: Main navigation hub
- **What you see**: 6 buttons to access different features
- **Action**: Click any button to navigate to that feature

#### 2. **Setup Roommates & Bills**
- **Purpose**: Add roommates and define **bill categories** (templates for recurring bills)
- **What it does**:
  - **Add Roommate**: Registers who lives in the shared space
    - **Example**: Name: "John", Email: "john@email.com", Phone: "1234567890"
    - **What happens**: Roommate appears in the list (used later for splitting expenses)
  - **Add Bill Category**: Defines types of recurring bills everyone shares
    - **Example**: Name: "Rent", Amount: "500" (monthly rent everyone shares)
    - **Example**: Name: "WiFi", Amount: "50" (monthly WiFi bill)
    - **What happens**: Bill category appears in the list (this is just a template, not an actual expense)
- **Important**: This page defines WHO lives there and WHAT bills they share. It does NOT track actual payments.

#### 3. **Chore Board**
- **Purpose**: Create and track chores assigned to roommates
- **How to use**:
  - **Add Chore**: Enter chore name and assign to a roommate, click "Add Chore"
    - **Example**: Chore: "Clean kitchen", Assigned to: "John"
    - **What happens**: Chore appears in list with "[ ]" (pending) status
  - **Mark Complete**: Select a chore from list, click "Mark Selected as Complete"
    - **What happens**: "[ ]" changes to "[X]" indicating completed
- **Note**: Chores stored in memory - will persist to database later

#### 4. **Shared Bill Tracker**
- **Purpose**: Track **actual expenses** (one-time transactions) and split costs
- **Difference from Setup page**:
  - **Setup page**: Defines bill CATEGORIES (templates like "Rent $500/month")
  - **Bill Tracker**: Records ACTUAL EXPENSES (transactions like "Groceries $100 paid today")
- **How to use**:
  - Enter expense name, amount, who paid, and how many people to split between
  - **Example**: Name: "Groceries", Amount: "100", Paid by: "Sarah", Split: "3"
  - **What happens**: Expense is added showing total amount, payer, and per-person cost ($33.33 each)
  - **Split count**: Number of people sharing this expense (currently just a number, not connected to specific roommates)
- **Current limitation**: 
  - Split count is just a number (e.g., "3") - it doesn't specify WHICH 3 roommates
  - If you have 3 roommates but split "2", it just divides by 2, not by specific people
  - **Future**: With database, you'll select which specific roommates to split between
- **Note**: Expenses stored in memory - will calculate balances from database later

#### 5. **Balance & Settlement**
- **Purpose**: View who owes money to whom
- **How to use**: Click "Calculate Balance" to see summary
- **What you see**: Explanation of how balances work and current status
- **Note**: Actual calculations will be available when database is integrated

#### 6. **History & Filters**
- **Purpose**: View and filter past chores and expenses
- **How to use**:
  - Enter filter text (name, category, etc.), click "Filter"
  - **Example**: Filter: "kitchen" shows only kitchen-related items
  - Click "Clear" to show all items again
- **Note**: Currently shows sample data - will show actual history from database later

#### 7. **Reports & Charts**
- **Purpose**: View statistics and summaries
- **How to use**: Click "Generate Report" to see statistics
- **What you see**: Chore statistics, expense statistics, and fairness metrics
- **Note**: Actual statistics will be available when database is integrated

## 📝 Current Status

- ✅ All 7 pages functional with UI
- ✅ Data input and validation working
- ✅ Navigation between pages working
- ⏳ Database integration pending (data stored in memory)
- ⏳ Data persistence pending (data lost when app closes)

## 🛠️ Technology Stack

- **Java**: Core programming language
- **JavaFX 21**: UI framework
- **SQLite**: Database (to be integrated)
- **Maven**: Build tool
- **FXML**: UI design

## 📁 Project Structure

```
RoomShare/
├── src/main/java/com/example/roomshare/
│   ├── Launcher.java              # Entry point
│   ├── RoomShareApplication.java # Main application
│   ├── WelcomeController.java    # Home page logic
│   ├── SetupController.java      # Setup page logic
│   ├── ChoreController.java      # Chore board logic
│   ├── BillController.java       # Bill tracker logic
│   ├── BalanceController.java    # Balance page logic
│   ├── HistoryController.java    # History page logic
│   └── ReportController.java     # Reports page logic
└── src/main/resources/com/example/roomshare/
    └── *.fxml files              # UI designs for each page
```

## 🔗 Connection Between Pages

### Setup Page vs Bill Tracker - Understanding the Difference

**Setup Page - "Shared Bills"**:
- These are **BILL CATEGORIES** (templates/recurring bills)
- Examples: "Rent $500", "WiFi $50", "Gas $30"
- Purpose: Define what types of recurring monthly bills everyone shares
- These are NOT actual expenses - just categories for reference
- Think of it as: "We all share these types of bills every month"

**Bill Tracker - "Expenses"**:
- These are **ACTUAL EXPENSES** (one-time transactions)
- Examples: "Groceries $100 paid by Sarah", "Restaurant $60 paid by John"
- Purpose: Track who paid what and split the cost
- These are real expenses that happened on a specific date
- Think of it as: "This is what someone actually paid today"

### Example Scenario

**Step 1 - Setup Page**:
- Add Roommates: John, Sarah, Mike (3 people)
- Add Bill Categories: Rent $500, WiFi $50

**Step 2 - Bill Tracker**:
- Add Expense: "Groceries $100, Paid by: Sarah, Split: 3"
- Result: Shows "$33.33 each" (100 ÷ 3)
- **Current limitation**: The "3" is just a number - it doesn't know it means John, Sarah, and Mike specifically

**Step 3 - Balance Page**:
- Calculates: Sarah paid $100, so others owe her $66.67 total
- John owes: $33.33
- Mike owes: $33.33

### Current Limitations (Without Database)

- **Split count is just a number**: 
  - If you have 3 roommates but enter "split 2", it divides by 2
  - It doesn't know which 2 specific roommates you mean
  - Example: 3 roommates (John, Sarah, Mike), split "2" → just divides by 2, not by John+Sarah or Sarah+Mike
  
- **No connection between pages**:
  - Setup page roommates and Bill Tracker expenses are separate
  - Bill Tracker doesn't know who the roommates are from Setup page
  
- **Manual entry required**:
  - You must type roommate names manually in Bill Tracker
  - No dropdown to select from Setup page roommates

### How It Will Work (Future with Database)

1. **Setup Page**: Define roommates (John, Sarah, Mike) and bill categories (Rent, WiFi)
2. **Bill Tracker**: When adding an expense:
   - Dropdown to select roommates from Setup page
   - Checkboxes to select which specific roommates to split between
   - Example: Select John and Sarah only → splits between just those 2
3. **Balance Page**: Calculates who owes what based on all expenses and selected roommates

## 🔄 Data Flow (Current)

1. **User Input** → Text fields on pages
2. **Validation** → Check if input is valid
3. **Storage** → Added to ListView (in memory)
4. **Display** → Shown in list on same page
5. **Limitation** → Data lost when app closes, pages not connected

**Future**: 
- Data will flow to SQLite database for persistence
- Setup page roommates will be available in Bill Tracker for splitting
- Balance calculations will use actual roommate data
