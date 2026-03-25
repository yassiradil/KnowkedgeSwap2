package knowledgeswap2;

import knowledgeswap2.service.FeedbackSystem;
import knowledgeswap2.storage.DataStorage;
import knowledgeswap2.ui.MainFrame;
import knowledgeswap2.ui.UiTheme;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UiTheme.apply();

            DataStorage storage = new DataStorage();
            FeedbackSystem system;
            try {
                system = storage.load();
            } catch (Exception ex) {
                storage.backupBadDataFile(ex);
                system = new FeedbackSystem();
                JOptionPane.showMessageDialog(null,
                        "Could not load saved data.\nStarting with empty data.\nReason: " + ex,
                        "Load Error",
                        JOptionPane.WARNING_MESSAGE);
            }

            MainFrame frame = new MainFrame(system, storage);
            frame.setVisible(true);
        });
    }
}

