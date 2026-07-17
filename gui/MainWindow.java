package gui;

import controller.*;
import model.*;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private LibraryManager libraryManager;
    private BorrowController borrowController;
    private JLabel statusBar;
    private Timer overdueTimer;

    public MainWindow() {
        LibraryDatabase db = new LibraryDatabase();
        libraryManager = new LibraryManager(db);
        libraryManager.loadFromDisk();
        borrowController = new BorrowController(db);

        setTitle("Smart Library Circulation & Automation System (SLCAS)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        ViewItemsPanel viewPanel = new ViewItemsPanel(libraryManager);
        BorrowPanel borrowPanel = new BorrowPanel(libraryManager, borrowController, viewPanel);
        AdminPanel adminPanel = new AdminPanel(libraryManager, viewPanel, this);
        SearchSortPanel searchSortPanel = new SearchSortPanel(libraryManager, viewPanel);

        tabs.addTab("View Items", viewPanel);
        tabs.addTab("Borrow/Return", borrowPanel);
        tabs.addTab("Admin", adminPanel);
        tabs.addTab("Search & Sort", searchSortPanel);

        add(tabs, BorderLayout.CENTER);

        statusBar = new JLabel(" Ready.");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        add(statusBar, BorderLayout.SOUTH);

        // Timer-triggered overdue reminders
        overdueTimer = new Timer(60000, e -> checkOverdueItems());
        overdueTimer.start();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                libraryManager.persist();
            }
        });

        setStatus("System loaded. " + db.getItems().size() + " items in catalogue.");
    }

    private void checkOverdueItems() {
        StringBuilder overdue = new StringBuilder();
        for (UserAccount u : libraryManager.getDatabase().getUsers()) {
            if (u.hasOverdue()) overdue.append(u.getName()).append(" ");
        }
        if (overdue.length() > 0) setStatus("Overdue reminder: " + overdue + "have overdue items!");
    }

    public void setStatus(String msg) { statusBar.setText(" " + msg); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
