package ashen.gui;

import ashen.model.GameCharacter;
import ashen.service.SaveLoadService;

import javax.swing.*;
import java.awt.*;

public class DefeatPanel extends JPanel {

    private MainFrame mainFrame;
    private GameCharacter character;
    private SaveLoadService saveLoadService;

    public DefeatPanel(MainFrame mainFrame, GameCharacter character) {
        this.mainFrame = mainFrame;
        this.character = character;
        this.saveLoadService = new SaveLoadService();

        layoutComponents();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel topTitle = new JLabel("Defeat", JLabel.CENTER);
        topTitle.setFont(new Font("SansSerif", Font.PLAIN, 20));
        topTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        add(topTitle, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());

        JLabel title = new JLabel("DEFEAT", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));

        JLabel message = new JLabel("Your journey ends here.", JLabel.CENTER);
        message.setFont(new Font("SansSerif", Font.PLAIN, 20));

        JButton mainMenuButton = createMenuButton("Main Menu");
        JButton saveButton = createMenuButton("Save Character");
        JButton exitButton = createMenuButton("Exit");

        mainMenuButton.addActionListener(e -> mainFrame.showMainMenu());
        saveButton.addActionListener(e -> saveCharacter());
        exitButton.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.gridy = 0;
        centerPanel.add(title, gbc);

        gbc.insets = new Insets(0, 0, 80, 0);
        gbc.gridy = 1;
        centerPanel.add(message, gbc);

        gbc.insets = new Insets(10, 0, 15, 0);

        gbc.gridy = 2;
        centerPanel.add(mainMenuButton, gbc);

        gbc.gridy = 3;
        centerPanel.add(saveButton, gbc);

        gbc.gridy = 4;
        centerPanel.add(exitButton, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(280, 55));
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return button;
    }

    private void saveCharacter() {
        try {
            String filePath = "DATA/" + character.getName() + ".ser";
            saveLoadService.saveCharacter(character, filePath);

            JOptionPane.showMessageDialog(
                    this,
                    "Character saved successfully."
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to save character.",
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}