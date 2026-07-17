package model;

import java.util.*;

public class LibraryDatabase {
    private ArrayList<LibraryItem> items;                       // required: ArrayList
    private Map<String, Queue<UserAccount>> reservationQueues;  // required: Queue per item
    private Stack<AdminAction> undoStack;                        // required: Stack for undo
    private LibraryItem[] frequentCache;                         // required: fixed-size array cache
    private static final int CACHE_SIZE = 5;
    private List<UserAccount> users;

    public LibraryDatabase() {
        items = new ArrayList<>();
        reservationQueues = new HashMap<>();
        undoStack = new Stack<>();
        frequentCache = new LibraryItem[CACHE_SIZE];
        users = new ArrayList<>();
    }

    public ArrayList<LibraryItem> getItems() { return items; }
    public List<UserAccount> getUsers() { return users; }
    public Stack<AdminAction> getUndoStack() { return undoStack; }

    public void addItem(LibraryItem item) {
        items.add(item);
        reservationQueues.putIfAbsent(item.getId(), new LinkedList<>());
    }

    public boolean removeItem(String id) {
        LibraryItem toRemove = null;
        for (LibraryItem it : items) {
            if (it.getId().equals(id)) { toRemove = it; break; }
        }
        if (toRemove != null) {
            items.remove(toRemove);
            reservationQueues.remove(id);
            return true;
        }
        return false;
    }

    public void addUser(UserAccount user) { users.add(user); }

    public Queue<UserAccount> getReservationQueue(String itemId) {
        return reservationQueues.computeIfAbsent(itemId, k -> new LinkedList<>());
    }

    public void reserveItem(String itemId, UserAccount user) {
        getReservationQueue(itemId).offer(user);
    }

    public UserAccount pollNextReservation(String itemId) {
        return getReservationQueue(itemId).poll();
    }

    public void updateFrequentCache() {
        List<LibraryItem> sorted = new ArrayList<>(items);
        sorted.sort((a, b) -> b.getTimesBorrowed() - a.getTimesBorrowed());
        for (int i = 0; i < CACHE_SIZE; i++) {
            frequentCache[i] = (i < sorted.size()) ? sorted.get(i) : null;
        }
    }

    public LibraryItem[] getFrequentCache() { return frequentCache; }

    // Recursive: total resource count by category (mandatory recursive component)
    public Map<String, Integer> countByCategory() {
        Map<String, Integer> counts = new HashMap<>();
        countByCategoryRecursive(items, 0, counts);
        return counts;
    }

    private void countByCategoryRecursive(List<LibraryItem> list, int index, Map<String, Integer> counts) {
        if (index >= list.size()) return; // base case
        counts.merge(list.get(index).getCategory(), 1, Integer::sum);
        countByCategoryRecursive(list, index + 1, counts); // recursive case
    }
}
