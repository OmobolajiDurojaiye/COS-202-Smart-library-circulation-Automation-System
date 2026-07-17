package gui;

import controller.BorrowController;
import controller.LibraryManager;
import model.UserAccount;
import utils.IDGenerator;

import javax.swing.*;
import java.awt.*;

public class BorrowPanel extends JPanel {
    private LibraryManager libraryManager;
    private BorrowController borrowController;
    private ViewItemsPanel viewItemsPanel;

    private JTextField itemIdField;
    private JTextField userNameField;
    private JTextArea logArea;

    public BorrowPanel(LibraryManager libraryManager, BorrowController borrowController, ViewItemsPanel viewItemsPanel) {
        this.libraryManager = libraryManager;
        this.borrowController = borrowController;
        this.viewItemsPanel = viewItemsPanel;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Item ID:"), gbc);
        gbc.gridx = 1;
        itemIdField = new JTextField(15);
        itemIdField.setToolTipText("Enter the item ID, e.g. ITM1000");
        form.add(itemIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("User Name:"), gbc);
        gbc.gridx = 1;
        userNameField = new JTextField(15);
        userNameField.setToolTipText("Enter the borrower's full name");
        form.add(userNameField, gbc);

        JButton borrowBtn = new JButton("Borrow");
        borrowBtn.setMnemonic('B');
        borrowBtn.setToolTipText("Borrow the specified item");
        JButton returnBtn = new JButton("Return");
        returnBtn.setMnemonic('T');
        returnBtn.setToolTipText("Return the specified item");

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(borrowBtn, gbc);
        gbc.gridx = 1;
        form.add(returnBtn, gbc);

        add(form, BorderLayout.NORTH);

        logArea = new JTextArea(10, 40);
        logArea.setEditable(false);
        logArea.setToolTipText("Activity log for borrow/return operations");
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        borrowBtn.addActionListener(e -> handleBorrow());
        returnBtn.addActionListener(e -> handleReturn());
    }

    private UserAccount resolveUser(String name) {
        for (UserAccount u : libraryManager.getDatabase().getUsers()) {
            if (u.getName().equalsIgnoreCase(name)) return u;
        }
        UserAccount newUser = new UserAccount(IDGenerator.generateUserId(), name);
        libraryManager.getDatabase().addUser(newUser);
        return newUser;
    }

    private void handleBorrow() {
        try {
            String itemId = itemIdField.getText().trim();
            String userName = userNameField.getText().trim();
            if (itemId.isEmpty() || userName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both Item ID and User Name.",
                        "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            UserAccount user = resolveUser(userName);
            log(borrowController.borrowItem(itemId, user));
            viewItemsPanel.refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleReturn() {
        try {
            String itemId = itemIdField.getText().trim();
            String userName = userNameField.getText().trim();
            if (itemId.isEmpty() || userName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both Item ID and User Name.",
                        "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            UserAccount user = resolveUser(userName);
            log(borrowController.returnItem(itemId, user));
            viewItemsPanel.refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void log(String msg) { logArea.append(msg + "\n"); }
}
