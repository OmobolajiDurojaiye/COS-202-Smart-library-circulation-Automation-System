package utils;

import model.*;
import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String ITEMS_FILE = "library_items.txt";
    private static final String USERS_FILE = "library_users.txt";

    public static void saveItems(List<LibraryItem> items) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ITEMS_FILE))) {
            for (LibraryItem item : items) {
                StringBuilder sb = new StringBuilder();
                sb.append(item.getCategory()).append("|")
                  .append(item.getId()).append("|")
                  .append(item.getTitle()).append("|")
                  .append(item.getAuthor()).append("|")
                  .append(item.getYear()).append("|");

                if (item instanceof Book) {
                    Book b = (Book) item;
                    sb.append(b.getIsbn()).append("|").append(b.getGenre());
                } else if (item instanceof Magazine) {
                    Magazine m = (Magazine) item;
                    sb.append(m.getIssueNumber()).append("|").append(m.getPublisher());
                } else if (item instanceof Journal) {
                    Journal j = (Journal) item;
                    sb.append(j.getVolume()).append("|").append(j.getFieldOfStudy());
                }
                sb.append("|").append(item.isAvailable())
                  .append("|").append(item.getTimesBorrowed());
                pw.println(sb.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving items: " + e.getMessage());
        }
    }

    public static List<LibraryItem> loadItems() {
        List<LibraryItem> items = new ArrayList<>();
        File file = new File(ITEMS_FILE);
        if (!file.exists()) return items;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\|");
                try {
                    LibraryItem item = null;
                    switch (p[0]) {
                        case "Book":
                            item = new Book(p[1], p[2], p[3], Integer.parseInt(p[4]), p[5], p[6]);
                            break;
                        case "Magazine":
                            item = new Magazine(p[1], p[2], p[3], Integer.parseInt(p[4]), p[5], p[6]);
                            break;
                        case "Journal":
                            item = new Journal(p[1], p[2], p[3], Integer.parseInt(p[4]), p[5], p[6]);
                            break;
                        default:
                            item = null;
                    }
                    if (item != null) {
                        item.setAvailable(Boolean.parseBoolean(p[7]));
                        for (int i = 0; i < Integer.parseInt(p[8]); i++) item.incrementTimesBorrowed();
                        items.add(item);
                    }
                } catch (Exception ex) {
                    System.err.println("Skipping corrupt line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading items: " + e.getMessage());
        }
        return items;
    }

    public static void saveUsers(List<UserAccount> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (UserAccount u : users) pw.println(u.getUserId() + "|" + u.getName());
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public static List<UserAccount> loadUsers() {
        List<UserAccount> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) return users;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\|");
                users.add(new UserAccount(p[0], p[1]));
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return users;
    }
}
