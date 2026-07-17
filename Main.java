import gui.MainWindow;

import javax.swing.*;

/**
 * Main entry point for the Smart Library Circulation & Automation System (SLCAS).
 * Launches the GUI application on the Event Dispatch Thread.
 */
public class Main {
    public static void main(String[] args) {
        // Set a nicer look and feel if available
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // fallback to default look and feel
        }

        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
