package controller;

import model.LibraryItem;
import java.util.List;

public class SearchEngine {

    // 1. Linear Search — works regardless of sort state
    public static LibraryItem linearSearch(List<LibraryItem> items, String query, String field) {
        for (LibraryItem item : items) {
            if (matches(item, query, field)) return item;
        }
        return null;
    }

    // 2. Binary Search — requires list pre-sorted by title
    public static LibraryItem binarySearch(List<LibraryItem> sortedByTitle, String title) {
        int low = 0, high = sortedByTitle.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = sortedByTitle.get(mid).getTitle().compareToIgnoreCase(title);
            if (cmp == 0) return sortedByTitle.get(mid);
            else if (cmp < 0) low = mid + 1;
            else high = mid - 1;
        }
        return null;
    }

    // 3. Recursive Search — by title (mandatory recursive component)
    public static LibraryItem recursiveSearch(List<LibraryItem> items, String title, int index) {
        if (index >= items.size()) return null; // base case
        if (items.get(index).getTitle().equalsIgnoreCase(title)) return items.get(index);
        return recursiveSearch(items, title, index + 1); // recursive case
    }

    private static boolean matches(LibraryItem item, String query, String field) {
        query = query.toLowerCase();
        switch (field.toLowerCase()) {
            case "author":
                return item.getAuthor().toLowerCase().contains(query);
            case "type":
            case "category":
                return item.getCategory().toLowerCase().contains(query);
            default:
                return item.getTitle().toLowerCase().contains(query);
        }
    }
}
