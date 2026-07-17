# COS-202-Smart-library-circulation-Automation-System

## Smart Library Circulation & Automation System (SLCAS)

A comprehensive Java Swing desktop application for university library management. The system handles cataloguing of books, magazines, and journals, borrow/return workflows with reservation queues, automated overdue reminders, and interactive GUI dashboards with reporting.

---

## Features

- **Add, Delete & View** library items (Books, Magazines, Journals)
- **Borrow & Return** items with automatic user registration
- **Reservation Queue** — waitlist for unavailable items (FIFO)
- **Undo** last admin action using a Stack
- **Search** by title, author, or type using Linear Search, Binary Search, or Recursive Search
- **Sort** by title, author, or year using Selection Sort, Insertion Sort, Merge Sort, or Quick Sort
- **Reports** — Most Borrowed Items, Overdue Users with charges, Category Distribution
- **Data Persistence** — saves/loads data to text files across sessions
- **Export Catalogue** to a file via File Chooser dialog
- **Overdue Reminders** — automatic timer-based alerts in the status bar

## GUI Highlights

- Tabbed interface: View Items, Borrow/Return, Admin, Search & Sort
- Custom cell renderer (colour-coded Available/Borrowed status)
- CardLayout for dynamic form switching
- Input validation with dialog popups
- Keyboard shortcuts (mnemonics) on all major buttons
- Tooltips on all interactive elements
- Status bar with system messages

---

## Project Structure

```
├── Main.java                    # Application entry point
├── model/
│   ├── LibraryItem.java         # Abstract base class
│   ├── Book.java                # Book subclass
│   ├── Magazine.java            # Magazine subclass
│   ├── Journal.java             # Journal subclass
│   ├── Borrowable.java          # Borrowable interface
│   ├── UserAccount.java         # User with borrowing history
│   ├── AdminAction.java         # Undo action record
│   └── LibraryDatabase.java     # Central data store
├── controller/
│   ├── LibraryManager.java      # Business logic controller
│   ├── BorrowController.java    # Borrow/return operations
│   ├── SearchEngine.java        # Search algorithms
│   └── SortEngine.java          # Sort algorithms
├── gui/
│   ├── MainWindow.java          # Main application frame
│   ├── ViewItemsPanel.java      # Catalogue display panel
│   ├── BorrowPanel.java         # Borrow/return panel
│   ├── AdminPanel.java          # Admin operations panel
│   └── SearchSortPanel.java     # Search & sort panel
└── utils/
    ├── IDGenerator.java         # Unique ID generation
    └── FileHandler.java         # File I/O persistence
```

---

## Data Structures Used

| Data Structure | Purpose |
|---|---|
| **ArrayList** | Primary storage for library items and users |
| **Queue (LinkedList)** | Reservation waitlist per item (FIFO) |
| **Stack** | Undo stack for admin actions (LIFO) |
| **Array (fixed-size)** | Cache for top 5 most frequently borrowed items |
| **HashMap** | Maps item IDs to reservation queues and due dates |

## Algorithms Implemented

### Search
1. **Linear Search** — O(n), works on unsorted data
2. **Binary Search** — O(log n), requires sorted-by-title data
3. **Recursive Search** — O(n), demonstrates recursion

### Sorting
1. **Selection Sort** — O(n²), in-place
2. **Insertion Sort** — O(n²), adaptive
3. **Merge Sort** — O(n log n), stable, divide-and-conquer
4. **Quick Sort** — O(n log n) average, in-place partitioning

### Recursive Components
1. Recursive search in the library catalogue
2. Recursive overdue charge computation
3. Recursive category count for distribution report

---

## How to Compile & Run

### Prerequisites
- Java JDK 8 or higher

### Compile
```bash
javac -d out model/*.java utils/*.java controller/*.java gui/*.java Main.java
```

### Run
```bash
java -cp out Main
```

The GUI window will open with four tabs: View Items, Borrow/Return, Admin, and Search & Sort.

---

## Class Hierarchy

```
                «interface»
                 Borrowable
                /    |    \
  «abstract»   /     |     \
  LibraryItem--      |      
   /    |    \       |
  /     |     \      |
Book  Magazine  Journal

LibraryDatabase ──contains──▶ ArrayList, Queue, Stack, Array
UserAccount ──contains──▶ List, Map
AdminAction ──references──▶ LibraryItem

LibraryManager ──uses──▶ LibraryDatabase
BorrowController ──uses──▶ LibraryDatabase

MainWindow
 ├── ViewItemsPanel
 ├── BorrowPanel
 ├── AdminPanel
 └── SearchSortPanel
```

---

## Technologies

- **Language:** Java
- **GUI Framework:** Java Swing
- **Persistence:** Text file I/O (pipe-delimited format)
