package ashen.app;

import ashen.gui.MainFrame;

import javax.swing.*;

public class AshenShieldApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
