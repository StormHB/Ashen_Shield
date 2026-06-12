package ashen.gui;

import javax.swing.*;
import java.awt.*;

public class CharacterCreationPanel extends JPanel {

    private MainFrame mainFrame;

    public CharacterCreationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        layoutComponents();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Character Creation", JLabel.CENTER);
        JButton backButton = new JButton("Back");

        backButton.addActionListener(e -> mainFrame.showMainMenu());

        add(label, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }
}
