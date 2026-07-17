package gui;

import controller.LibraryManager;
import controller.SearchEngine;
import controller.SortEngine;
import model.LibraryItem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SearchSortPanel extends JPanel {
    private LibraryManager libraryManager;
    private ViewItemsPanel viewItemsPanel;

    private JTextField searchField;
    private JComboBox<String> searchFieldCombo;
    private JComboBox<String> searchAlgoCombo;
    private JComboBox<String> sortFieldCombo;
    private JComboBox<String> sortAlgoCombo;

    private boolean sortedByTitle = false; // tracks state for binary search eligibility

    public SearchSortPanel(LibraryManager libraryManager, ViewItemsPanel viewItemsPanel) {
        this.libraryManager = libraryManager;
        this.viewItemsPanel = viewItemsPanel;

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Search Section ---
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Search:"), gbc);
        gbc.gridx = 1;
        searchField = new JTextField(15);
        searchField.setToolTipText("Enter search query");
        add(searchField, gbc);

        gbc.gridx = 2;
        add(new JLabel("in field:"), gbc);
        gbc.gridx = 3;
        searchFieldCombo = new JComboBox<>(new String[]{"Title", "Author", "Type"});
        searchFieldCombo.setToolTipText("Choose the field to search in");
        add(searchFieldCombo, gbc);

        gbc.gridx = 4;
        add(new JLabel("using:"), gbc);
        gbc.gridx = 5;
        searchAlgoCombo = new JComboBox<>(new String[]{"Linear Search", "Binary Search (title, sorted)", "Recursive Search (title)"});
        searchAlgoCombo.setToolTipText("Choose the search algorithm");
        add(searchAlgoCombo, gbc);

        gbc.gridx = 6;
        JButton searchBtn = new JButton("Search");
        searchBtn.setMnemonic('S');
        searchBtn.setToolTipText("Execute the search");
        searchBtn.addActionListener(e -> handleSearch());
        add(searchBtn, gbc);

        // --- Sort Section ---
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Sort by:"), gbc);
        gbc.gridx = 1;
        sortFieldCombo = new JComboBox<>(new String[]{"Title", "Author", "Year"});
        sortFieldCombo.setToolTipText("Choose the field to sort by");
        add(sortFieldCombo, gbc);

        gbc.gridx = 2;
        add(new JLabel("algorithm:"), gbc);
        gbc.gridx = 3;
        sortAlgoCombo = new JComboBox<>(new String[]{"Selection Sort", "Insertion Sort", "Merge Sort", "Quick Sort"});
        sortAlgoCombo.setToolTipText("Choose the sorting algorithm");
        add(sortAlgoCombo, gbc);

        gbc.gridx = 4;
        JButton sortBtn = new JButton("Sort");
        sortBtn.setMnemonic('O');
        sortBtn.setToolTipText("Sort the catalogue using the selected algorithm");
        sortBtn.addActionListener(e -> handleSort());
        add(sortBtn, gbc);

        gbc.gridx = 5;
        JButton resetBtn = new JButton("Reset View");
        resetBtn.setToolTipText("Reset to the original catalogue order");
        resetBtn.addActionListener(e -> { viewItemsPanel.refresh(); sortedByTitle = false; });
        add(resetBtn, gbc);
    }

    private void handleSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a search term.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String field = (String) searchFieldCombo.getSelectedItem();
        String algo = (String) searchAlgoCombo.getSelectedItem();
        List<LibraryItem> items = libraryManager.getDatabase().getItems();

        try {
            LibraryItem found = null;
            if (algo.startsWith("Linear")) {
                found = SearchEngine.linearSearch(items, query, field);
            } else if (algo.startsWith("Binary")) {
                if (!sortedByTitle) {
                    JOptionPane.showMessageDialog(this,
                            "Binary search requires the list sorted by Title first. Sort by Title, then retry.",
                            "Precondition Not Met", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                found = SearchEngine.binarySearch(items, query);
            } else {
                found = SearchEngine.recursiveSearch(items, query, 0);
            }

            if (found != null) {
                List<LibraryItem> result = new ArrayList<>();
                result.add(found);
                viewItemsPanel.displayList(result);
            } else {
                JOptionPane.showMessageDialog(this, "No matching item found.", "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSort() {
        String field = (String) sortFieldCombo.getSelectedItem();
        String algo = (String) sortAlgoCombo.getSelectedItem();
        List<LibraryItem> items = libraryManager.getDatabase().getItems();

        Comparator<LibraryItem> cmp;
        switch (field) {
            case "Author":
                cmp = SortEngine.byAuthor();
                break;
            case "Year":
                cmp = SortEngine.byYear();
                break;
            default:
                cmp = SortEngine.byTitle();
                break;
        }

        try {
            long startTime = System.nanoTime();

            switch (algo) {
                case "Selection Sort":
                    SortEngine.selectionSort(items, cmp);
                    break;
                case "Insertion Sort":
                    SortEngine.insertionSort(items, cmp);
                    break;
                case "Merge Sort":
                    SortEngine.mergeSort(items, cmp);
                    break;
                case "Quick Sort":
                    SortEngine.quickSort(items, cmp);
                    break;
            }

            long elapsed = System.nanoTime() - startTime;
            sortedByTitle = "Title".equals(field);
            viewItemsPanel.refresh();

            JOptionPane.showMessageDialog(this,
                    "Sorted " + items.size() + " items by " + field + " using " + algo +
                    "\nTime: " + (elapsed / 1000) + " microseconds",
                    "Sort Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Sort error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
