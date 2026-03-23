### Software Design Documentation (SDD)

## Table of Contents
- [1. System Overview](#1-system-overview)
- [2. Architecture Design](#2-architecture-design)
  - [2.1 Architectural Pattern](#architectural-pattern)
- [3. Major System Components](#3-major-system-components)
  - [Model Layer](#model-layer)
  - [Logic Layer](#logic-layer)
  - [UI Layer](#ui-layer)
  - [Storage layer](#storage-layer)
- [4. UML Diagrams](#4-uml-diagrams)
  - [4.1 Class Diagrams](#class-diagram)
  - [4.2 Sequence Diagrams](#sequence-diagram---add-expenses)
  - [4.3 Use Case Diagrams](#use-case-diagram)
- [5. Key Design Decisions](#5-key-design-decisions)
  - [5.1 Layered Architecture]( #layered-architecture)

## 1. System Overview
The Shared Expense Tracker is an application that enables groups of users to record, manage and settle shared expenses.
It targets friend groups, housemates and small teams who need to split costs without manual calculation.
**The system allows users to:**
- Record expenses with a payer, amount, description and list of involved
  participants using a command-line style input
- Automatically calculate how much each member owes using a debt
  simplification algorithm
- View a simplified balance summary showing who owes whom and how much
- Filter expenses by name, payer, participant or tag
- Persist all expense data locally between sessions
- Access a help window listing all available commands

## 2. Architecture Design
# Architectural Pattern
![Architectural Pattern Diagram](diagrams/ArchitecturalPatternDiagram.png)
Each layer communicates only with adjacent layers. The UI layer never directly addresses storage and the storage layer has no knowledge of the UI. 
This separation makes the system easier to test and maintain. 

## 3. Major System Components
### Model Layer
The model layer contains pure data classes with no business logic or UI
dependencies. Key classes:
- `Expense` — stores expense name, amount, payer, participants and tags
- `Person` — stores a person's name
- `Tag` — stores a tag name
- `Balance` — represents a directional debt between two persons
- `ExpenseList` — wraps an `ObservableList<Expense>` for JavaFX binding
- `ModelManager` — implements `Model`, manages `ExpenseList` and
  `FilteredList`

### Logic Layer
The logic layer processes all user commands. It follows the Command
Pattern — each command is parsed into a `Command` object and executed
against the `Model`.
- `LogicManager` — implements `Logic`, coordinates parsing and execution
- `FairShareParser` — parses the command word and delegates to the
  appropriate parser
- `AddCommandParser`, `DeleteCommandParser`, `FilterCommandParser` —
  parse arguments for each command
- `AddCommand`, `DeleteCommand`, `FilterCommand`, `ListCommand` —
  execute operations against the model
- `CommandResult` — wraps the feedback string returned after execution

### UI Layer
Built with JavaFX and FXML. Each component loads its own `.fxml` file
using `FXMLLoader`.
- `MainWindow` — root window, implements `Ui`, holds all sub-panels
- `ExpenseListPanel` — displays the filtered expense list as cards
- `ExpenseCard` — renders a single expense's details
- `BalancePanel` — displays the simplified debt summary as cards
- `BalanceCard` — renders a single balance entry
- `ResultDisplay` — shows command feedback messages
- `CommandBox` — accepts user text input, executes on Enter or button
- `HelpWindow` — separate popup window listing all available commands

### Storage Layer
Handles reading and writing all expense data to a local plain-text file.
- `StorageManager` — implements `Storage`, delegates to
  `TxtExpenseTrackerStorage`
- `TxtExpenseTrackerStorage` — reads and writes to `data/expenses.txt`
- `TxtSerializableExpenseTracker` — converts between `Expense` objects
  and text lines
- `TxtAdaptedExpense`, `TxtAdaptedPerson`, `TxtAdaptedTag` — storage
  representations of model objects

## 4. UML Diagrams
# Class Diagram
![Class Diagram](diagrams/ClassDiagram.png)
The class diagram above illustrates the structure of the Shared Expense Tracker and the relationships between its core classes.
- **ExpenseManager** sits at the top of the logic layer and coordinates all user operations. It manages the `Group`, calls `DebtCalculator` to recompute balances after every change and calls `Storage` to store the data between sessions.
- **Group** is the central class, composing a list of `Person` members and a list of `Expense` records. It does not exist without both.
- **Expense** records a single shared cost, referencing the `Person` who paid and the list of `Person` objects involved. It uses a `SplitType` enum to determine how the cost is divided.
- **DebtCalculator** computes each `Person`'s net balance and simplifies all debts into the minimum number of `Debt` transactions needed to settle up.
- **Debt** represents a single directional payment owed from one `Person` (debtor) to another (creditor) and tracks whether it has been settled.
- **Storage** handles saving and loading all the data, ensuring that data persists between sessions.

# Sequence Diagram - Add Expenses
![Sequence Diagram](diagrams/SequenceDiagram.png)
The sequence diagram above illustrates the interactions between components when a user adds a new expense.
1. The user fills in the expense form on `ExpensePanel` and clicks Add.
2. `ExpensePanel` calls `addExpense()` on `ExpenseManager`.
3. `ExpenseManager` validates the input, creates a new `Expense` object and adds it to the `Group`.
4. `ExpenseManager` calls `computeBalances()` on `DebtCalculator`, which updates each member's net balance(`update()`) and returns the simplified debt list.
5. `ExpenseManager` calls `saveData()` on `Storage` to persist the updated state to disk.
6. `ExpenseManager` calls `refreshUI()` on `ExpensePanel`, which displays the updated expense list  to the user.

# Use Case Diagram
![Use Case Diagram](diagrams/UseCaseDiagram.png)
The use case diagram above illustrates the set of sequence of actions that both the user and system perform in the Shared Expense Tracker. 

## 5. Key Design Decisions

### 5.1 Layered Architecture
**Decision:** Adopt a strict layered architecture (UI → Logic → Model →
Storage) with no cross-layer dependencies.
**Rationale:** Separating concerns allows team members to work on
different layers in parallel without conflicts and makes each layer
independently testable.

### 5.2 Command Pattern for Logic
**Decision:** Each user action is encapsulated as a `Command` object
parsed by a dedicated parser class.
**Rationale:** Adding new commands only requires creating a new
`Command` and `Parser` class without modifying existing code, following
the Open-Closed principle.

### 5.3 Plain-Text Storage
**Decision:** Persist data as a pipe-delimited `.txt` file using custom
serialisation.
**Rationale:** Simple to implement and debug without external
dependencies. Each expense is stored as one line in the format
`name|amount|payer|shares|tags`.

### 5.4 JavaFX ObservableList for UI Binding
**Decision:** Use `ObservableList` and `FilteredList` from JavaFX for
the expense list.
**Rationale:** JavaFX `ListView` automatically reflects changes to an
`ObservableList`, reducing the need for manual UI refresh calls.

----*End of document*----
