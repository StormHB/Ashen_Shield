package ashen.app;

import ashen.gui.MainFrame;

import javax.swing.*;

/**
 * Entry point of the Ashen Shield application.
 * Starts the Swing user interface on the Event Dispatch Thread.
 */

public class AshenShieldApp {

    /**
     * Launches the application and displays the main frame.
     *
     * @param args command-line arguments; not used by this application
     */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }
}
