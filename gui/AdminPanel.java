package gui;

import controller.LibraryManager;
import model.*;
import utils.IDGenerator;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class AdminPanel extends JPanel {
    private LibraryManager libraryManager;
    private ViewItemsPanel viewItemsPanel;
    private MainWindow mainWindow;

    private JComboBox<String> typeCombo;
    private CardLayout cardLayout;
    private JPanel formCards;

    private JTextField titleField, authorField, yearField, extra1Field, extra2Field;
    private JTextField deleteIdField;

    public AdminPanel(LibraryManager libraryManager, ViewItemsPanel viewItemsPanel, MainWindow mainWindow) {
        this.libraryManager = libraryManager;
        this.viewItemsPanel = viewItemsPanel;
        this.mainWindow = mainWindow;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel addSection = new JPanel(new BorderLayout());
        addSection.setBorder(BorderFactory.createTitledBorder("Add New Item"));

        typeCombo = new JComboBox<>(new String[]{"Book", "Magazine", "Journal"});
        typeCombo.setToolTipText("Select the type of library item to add");
        typeCombo.addActionListener(e -> {
            cardLayout.show(formCards, (String) typeCombo.getSelectedItem());
            updateExtraLabels();
        });

        JPanel typePanel = new JPanel();
        typePanel.add(new JLabel("Item Type:"));
        typePanel.add(typeCombo);
        addSection.add(typePanel, BorderLayout.NORTH);

        titleField = new JTextField(15);
        titleField.setToolTipText("Enter item title");
        authorField = new JTextField(15);
        authorField.setToolTipText("Enter author/editor name");
        yearField = new JTextField(6);
        yearField.setToolTipText("Enter publication year (e.g. 2024)");
        extra1Field = new JTextField(12);
        extra2Field = new JTextField(12);

        // Dynamic form using CardLayout (switching fields based on item type)
        cardLayout = new CardLayout();
        formCards = new JPanel(cardLayout);
        formCards.add(buildDynamicForm("ISBN", "Genre"), "Book");
        formCards.add(buildDynamicForm("Issue Number", "Publisher"), "Magazine");
        formCards.add(buildDynamicForm("Volume", "Field of Study"), "Journal");
        addSection.add(formCards, BorderLayout.CENTER);

        JButton addBtn = new JButton("Add Item");
        addBtn.setMnemonic('A');
        addBtn.setToolTipText("Add the new item to the library catalogue");
        addBtn.addActionListener(e -> handleAdd());
        addSection.add(addBtn, BorderLayout.SOUTH);

        add(addSection, BorderLayout.NORTH);

        JPanel actionSection = new JPanel(new FlowLayout());
        deleteIdField = new JTextField(10);
        deleteIdField.setToolTipText("Enter the ID of the item to delete");
        JButton deleteBtn = new JButton("Delete Item");
        deleteBtn.setMnemonic('D');
        deleteBtn.setToolTipText("Delete the item with the specified ID");
        deleteBtn.addActionListener(e -> handleDelete());

        JButton undoBtn = new JButton("Undo Last Action");
        undoBtn.setMnemonic('U');
        undoBtn.setToolTipText("Undo the last admin add/delete action");
        undoBtn.addActionListener(e -> {
            mainWindow.setStatus(libraryManager.undoLastAction());
            viewItemsPanel.refresh();
        });

        JButton exportBtn = new JButton("Export Catalogue...");
        exportBtn.setToolTipText("Export the full catalogue to a text file");
        exportBtn.addActionListener(e -> handleExport());

        actionSection.add(new JLabel("Item ID to delete:"));
        actionSection.add(deleteIdField);
        actionSection.add(deleteBtn);
        actionSection.add(undoBtn);
        actionSection.add(exportBtn);

        add(actionSection, BorderLayout.CENTER);

        JPanel reportSection = new JPanel(new FlowLayout());
        reportSection.setBorder(BorderFactory.createTitledBorder("Reports"));
        JButton mostBorrowedBtn = new JButton("Most Borrowed Report");
        mostBorrowedBtn.setToolTipText("Show the top 5 most borrowed items");
        JButton overdueBtn = new JButton("Overdue Users Report");
        overdueBtn.setToolTipText("Show users with overdue items and charges");
        JButton categoryBtn = new JButton("Category Distribution Report");
        categoryBtn.setToolTipText("Show item counts by category");

        mostBorrowedBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, libraryManager.mostBorrowedReport(), "Most Borrowed Report", JOptionPane.INFORMATION_MESSAGE));
        overdueBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, libraryManager.overdueUsersReport(), "Overdue Users Report", JOptionPane.INFORMATION_MESSAGE));
        categoryBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, libraryManager.categoryDistributionReport(), "Category Distribution Report", JOptionPane.INFORMATION_MESSAGE));

        reportSection.add(mostBorrowedBtn);
        reportSection.add(overdueBtn);
        reportSection.add(categoryBtn);
        add(reportSection, BorderLayout.SOUTH);
    }

    private JPanel buildDynamicForm(String label1, String label2) {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; p.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; p.add(titleField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; p.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1; p.add(authorField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; p.add(new JLabel("Year:"), gbc);
        gbc.gridx = 1; p.add(yearField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; p.add(new JLabel(label1 + ":"), gbc);
        gbc.gridx = 1; p.add(extra1Field, gbc);
        gbc.gridx = 0; gbc.gridy = 4; p.add(new JLabel(label2 + ":"), gbc);
        gbc.gridx = 1; p.add(extra2Field, gbc);

        return p;
    }

    private void updateExtraLabels() {
        // Dynamic component update based on item type selection
        String type = (String) typeCombo.getSelectedItem();
        switch (type) {
            case "Book":
                extra1Field.setToolTipText("Enter ISBN number");
                extra2Field.setToolTipText("Enter genre (e.g. Fiction, Science)");
                break;
            case "Magazine":
                extra1Field.setToolTipText("Enter issue number");
                extra2Field.setToolTipText("Enter publisher name");
                break;
            case "Journal":
                extra1Field.setToolTipText("Enter volume number");
                extra2Field.setToolTipText("Enter field of study");
                break;
        }
    }

    private void handleAdd() {
        try {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String yearText = yearField.getText().trim();
            String extra1 = extra1Field.getText().trim();
            String extra2 = extra2Field.getText().trim();

            if (title.isEmpty() || author.isEmpty() || yearText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title, Author, and Year are required.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int year = Integer.parseInt(yearText);
            String type = (String) typeCombo.getSelectedItem();
            String id = IDGenerator.generateItemId();
            LibraryItem item = null;

            switch (type) {
                case "Book":
                    item = new Book(id, title, author, year, extra1, extra2);
                    break;
                case "Magazine":
                    item = new Magazine(id, title, author, year, extra1, extra2);
                    break;
                case "Journal":
                    item = new Journal(id, title, author, year, extra1, extra2);
                    break;
            }

            if (item != null) {
                libraryManager.addItem(item);
                viewItemsPanel.refresh();
                mainWindow.setStatus("Added: " + item.getTitle() + " (" + item.getId() + ")");
                clearForm();
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Year must be a valid number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        String id = deleteIdField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter an Item ID to delete.",
                    "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (libraryManager.deleteItem(id)) {
            mainWindow.setStatus("Deleted item " + id);
            viewItemsPanel.refresh();
        } else {
            JOptionPane.showMessageDialog(this, "Item ID not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleExport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export Catalogue");
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (java.io.PrintWriter pw = new java.io.PrintWriter(file)) {
                for (LibraryItem item : libraryManager.getDatabase().getItems()) pw.println(item.display());
                mainWindow.setStatus("Exported catalogue to " + file.getName());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        titleField.setText(""); authorField.setText(""); yearField.setText("");
        extra1Field.setText(""); extra2Field.setText("");
    }
}
