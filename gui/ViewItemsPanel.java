package gui;

import controller.LibraryManager;
import model.LibraryItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class ViewItemsPanel extends JPanel {
    private LibraryManager libraryManager;
    private DefaultTableModel tableModel;
    private JTable table;

    public ViewItemsPanel(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
        setLayout(new BorderLayout());

        String[] cols = {"ID", "Title", "Author", "Year", "Category", "Status", "Borrow Count"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setToolTipText("Library catalogue - displays all items currently in the system");

        // Custom cell renderer for the Status column (Advanced GUI Technique #1)
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if ("Available".equals(value)) {
                    c.setForeground(new Color(0, 128, 0));
                } else {
                    c.setForeground(Color.RED);
                }
                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setMnemonic('R');
        refreshBtn.setToolTipText("Reload the catalogue view");
        refreshBtn.addActionListener(e -> refresh());
        JPanel bottom = new JPanel();
        bottom.add(refreshBtn);
        add(bottom, BorderLayout.SOUTH);

        refresh();
    }

    public void refresh() {
        tableModel.setRowCount(0);
        for (LibraryItem item : libraryManager.getDatabase().getItems()) addRow(item);
    }

    public void displayList(List<LibraryItem> items) {
        tableModel.setRowCount(0);
        for (LibraryItem item : items) addRow(item);
    }

    private void addRow(LibraryItem item) {
        tableModel.addRow(new Object[]{
                item.getId(), item.getTitle(), item.getAuthor(), item.getYear(),
                item.getCategory(), item.isAvailable() ? "Available" : "Borrowed",
                item.getTimesBorrowed()
        });
    }
}
