package controller;

import model.*;

public class BorrowController {
    private LibraryDatabase database;

    public BorrowController(LibraryDatabase database) { this.database = database; }

    public String borrowItem(String itemId, UserAccount user) {
        LibraryItem item = findItem(itemId);
        if (item == null) return "Item not found.";
        if (item instanceof Borrowable) {
            Borrowable borrowable = (Borrowable) item;
            if (item.isAvailable()) {
                borrowable.borrow(user);
                database.updateFrequentCache();
                return user.getName() + " borrowed '" + item.getTitle() + "'.";
            } else {
                database.reserveItem(itemId, user);
                return item.getTitle() + " is unavailable. " + user.getName() + " added to reservation queue.";
            }
        }
        return "This item type cannot be borrowed.";
    }

    public String returnItem(String itemId, UserAccount user) {
        LibraryItem item = findItem(itemId);
        if (item == null) return "Item not found.";
        if (item instanceof Borrowable) {
            Borrowable borrowable = (Borrowable) item;
            borrowable.returnItem(user);
            UserAccount next = database.pollNextReservation(itemId);
            if (next != null) {
                borrowable.borrow(next);
                return item.getTitle() + " returned and auto-assigned to " + next.getName() + " from waitlist.";
            }
            return item.getTitle() + " returned successfully.";
        }
        return "This item type cannot be returned.";
    }

    private LibraryItem findItem(String id) {
        for (LibraryItem item : database.getItems()) {
            if (item.getId().equals(id)) return item;
        }
        return null;
    }
}
