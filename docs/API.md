# API Documentation: Shared Expense Tracker

This document provides a high-level overview of the internal component interfaces and their interactions.

---

## 1. Logic Component
The Logic component is the primary interface between the UI and the data. It processes user input and returns system feedback.

### Key Methods
* `execute(String commandText)`
    * **Returns**: `CommandResult`
    * **Throws**: `ParseException`
    * **Role**: Orchestrates the parsing of user input and the execution of resulting commands against the Model.
* `getFilteredExpenseList()`
    * **Returns**: `ArrayList<Expense>`
    * **Role**: Provides a live-updating list of expenses for display in the main UI panel.
* `getBalances()`
    * **Returns**: `ArrayList<Balance>`
    * **Role**: Provides the calculated net totals for all persons based on the current filtered view.

---

## 2. Model Component
The Model maintains the state of the application and handles data integrity and calculations.

### Key Methods
* `addExpense(double e)`
    * **Role**: Appends a new expense to the internal list and triggers a debt recalculation.
* `deleteExpense(int i)`
    * **Role**: Removes the expense at the specified index and refreshes the balance list.
* `updateFilteredList(Predicate p)`
    * **Role**: Filters the visible data based on user search criteria or tags (e.g., `t/TurkeyTrip`).

---

## 3. Storage Component
The Storage component manages the loading and saving of the expense list.

### Key Methods
* `readExpenseTracker()`
    * **Role**: Parses the `data/tracker.txt` file and returns a `ReadOnlyExpenseTracker` object.
* `saveExpenseTracker(tracker)`
    * **Role**: Converts the current state into a pipe-delimited string format and writes it to disk.

---

## 4. Custom Exceptions
The system uses the following exceptions to maintain stability:

* **InvalidCommandException**: Used by the Logic component when user input does not match the expected command syntax.
* **IllegalValueException**: Used by the Model when certain rules are violated (e.g., sharing an expense with zero people).
* **CorruptedFileException**: Used by Storage when the local file is corrupted or unparseable.