package controller;

import model.*;
import utils.FileHandler;
import java.util.List;

public class LibraryManager {
    private LibraryDatabase database;

    public LibraryManager(LibraryDatabase database) { this.database = database; }

    public LibraryDatabase getDatabase() { return database; }

    public void addItem(LibraryItem item) {
        database.addItem(item);
        database.getUndoStack().push(new AdminAction(AdminAction.ActionType.ADD, item));
        database.updateFrequentCache();
    }

    public boolean deleteItem(String id) {
        LibraryItem target = null;
        for (LibraryItem it : database.getItems()) {
            if (it.getId().equals(id)) { target = it; break; }
        }
        if (target == null) return false;
        boolean removed = database.removeItem(id);
        if (removed) database.getUndoStack().push(new AdminAction(AdminAction.ActionType.DELETE, target));
        return removed;
    }

    // Undo last admin operation using Stack (mandatory feature)
    public String undoLastAction() {
        if (database.getUndoStack().isEmpty()) return "Nothing to undo.";
        AdminAction last = database.getUndoStack().pop();
        if (last.getType() == AdminAction.ActionType.ADD) {
            database.removeItem(last.getItem().getId());
            return "Undo: removed last added item '" + last.getItem().getTitle() + "'";
        } else {
            database.addItem(last.getItem());
            return "Undo: restored deleted item '" + last.getItem().getTitle() + "'";
        }
    }

    // Polymorphism: one function that processes any LibraryItem type
    public String processItem(LibraryItem item) {
        return "Processing " + item.getCategory() + ": " + item.display();
    }

    public void persist() {
        FileHandler.saveItems(database.getItems());
        FileHandler.saveUsers(database.getUsers());
    }

    public void loadFromDisk() {
        List<LibraryItem> items = FileHandler.loadItems();
        for (LibraryItem i : items) database.addItem(i);
        List<UserAccount> users = FileHandler.loadUsers();
        for (UserAccount u : users) database.addUser(u);
        database.updateFrequentCache();
    }

    public String mostBorrowedReport() {
        database.updateFrequentCache();
        StringBuilder sb = new StringBuilder("Most Borrowed Items:\n");
        for (LibraryItem item : database.getFrequentCache()) {
            if (item != null) sb.append("- ").append(item.getTitle())
                    .append(" (").append(item.getTimesBorrowed()).append(" borrows)\n");
        }
        return sb.toString();
    }

    public String overdueUsersReport() {
        StringBuilder sb = new StringBuilder("Users With Overdue Items:\n");
        for (UserAccount u : database.getUsers()) {
            if (u.hasOverdue()) {
                sb.append("- ").append(u.getName())
                  .append(" | Charge: NGN ").append(u.computeOverdueCharges()).append("\n");
            }
        }
        return sb.toString();
    }

    public String categoryDistributionReport() {
        StringBuilder sb = new StringBuilder("Category Distribution:\n");
        database.countByCategory().forEach((cat, count) ->
                sb.append("- ").append(cat).append(": ").append(count).append("\n"));
        return sb.toString();
    }
}
