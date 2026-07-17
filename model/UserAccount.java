package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAccount {
    private String userId;
    private String name;
    private List<LibraryItem> currentlyBorrowed;
    private List<LibraryItem> borrowingHistory;
    private Map<String, Long> dueDates; // itemId -> due timestamp (millis)

    public UserAccount(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.currentlyBorrowed = new ArrayList<>();
        this.borrowingHistory = new ArrayList<>();
        this.dueDates = new HashMap<>();
    }

    public void addToBorrowed(LibraryItem item) {
        currentlyBorrowed.add(item);
        borrowingHistory.add(item);
        long due = System.currentTimeMillis() + (14L * 24 * 60 * 60 * 1000); // 14-day loan
        dueDates.put(item.getId(), due);
    }

    public void removeFromBorrowed(LibraryItem item) {
        currentlyBorrowed.remove(item);
        dueDates.remove(item.getId());
    }

    public boolean hasOverdue() {
        long now = System.currentTimeMillis();
        for (LibraryItem item : currentlyBorrowed) {
            Long due = dueDates.get(item.getId());
            if (due != null && now > due) return true;
        }
        return false;
    }

    // Recursive overdue charge computation (mandatory recursive component)
    public double computeOverdueCharges() {
        return computeOverdueChargesRecursive(currentlyBorrowed, 0);
    }

    private double computeOverdueChargesRecursive(List<LibraryItem> items, int index) {
        if (index >= items.size()) return 0.0; // base case
        LibraryItem item = items.get(index);
        double charge = 0.0;
        Long due = dueDates.get(item.getId());
        if (due != null) {
            long now = System.currentTimeMillis();
            if (now > due) {
                long daysLate = (now - due) / (24 * 60 * 60 * 1000);
                charge = daysLate * 10.0; // NGN 10/day
            }
        }
        return charge + computeOverdueChargesRecursive(items, index + 1); // recursive case
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public List<LibraryItem> getCurrentlyBorrowed() { return currentlyBorrowed; }
    public List<LibraryItem> getBorrowingHistory() { return borrowingHistory; }

    @Override
    public String toString() { return userId + " - " + name; }
}
