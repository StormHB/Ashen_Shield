package ashen.gui;

import ashen.model.GameCharacter;
import ashen.service.SaveLoadService;

import javax.swing.*;
import java.awt.*;

public class VictoryPanel extends JPanel {

    private MainFrame mainFrame;
    private GameCharacter character;
    private int defeatedEnemyIndex;
    private String defeatedEnemyName;
    private SaveLoadService saveLoadService;

    private JButton shortRestButton;

    public VictoryPanel(MainFrame mainFrame, GameCharacter character,
                        int defeatedEnemyIndex, String defeatedEnemyName) {
        this.mainFrame = mainFrame;
        this.character = character;
        this.defeatedEnemyIndex = defeatedEnemyIndex;
        this.defeatedEnemyName = defeatedEnemyName;
        this.saveLoadService = new SaveLoadService();

        layoutComponents();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        boolean campaignCompleted = defeatedEnemyIndex == 4;

        JLabel topTitle = new JLabel("Victory", JLabel.CENTER);
        topTitle.setFont(new Font("SansSerif", Font.PLAIN, 20));
        topTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        add(topTitle, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());

        JLabel title = new JLabel("VICTORY!", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));

        JLabel message = new JLabel("You defeated " + defeatedEnemyName + ".", JLabel.CENTER);
        message.setFont(new Font("SansSerif", Font.PLAIN, 20));

        if (campaignCompleted) {
            title.setText("CAMPAIGN COMPLETED!");
            message.setText("You defeated the Young Dragon and completed the campaign.");
        }

        JLabel hpLabel = new JLabel(
                "Current HP: " + character.getCurrentHp() + "/" + character.getMaxHp(),
                JLabel.CENTER
        );

        JLabel progressLabel = new JLabel(
                "Campaign Progress: " + (defeatedEnemyIndex + 1) + "/5",
                JLabel.CENTER
        );

        shortRestButton = createMenuButton("Short Rest");
        JButton nextBattleButton = createMenuButton("Next Battle");
        JButton mainMenuButton = createMenuButton("Main Menu");
        JButton saveButton = createMenuButton("Save Character");
        JButton exitButton = createMenuButton("Exit");

        if (!campaignCompleted) {
            shortRestButton.addActionListener(e -> shortRest(hpLabel));
            nextBattleButton.addActionListener(e -> mainFrame.showBattle(character, defeatedEnemyIndex + 1));
        }

        mainMenuButton.addActionListener(e -> mainFrame.showMainMenu());
        saveButton.addActionListener(e -> saveCharacter());
        exitButton.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.gridy = 0;
        centerPanel.add(title, gbc);

        gbc.gridy = 1;
        centerPanel.add(message, gbc);

        gbc.gridy = 2;
        centerPanel.add(progressLabel, gbc);

        gbc.insets = new Insets(0, 0, 80, 0);
        gbc.gridy = 3;
        centerPanel.add(hpLabel, gbc);

        gbc.insets = new Insets(10, 0, 15, 0);

        if (campaignCompleted) {
            gbc.gridy = 4;
            centerPanel.add(mainMenuButton, gbc);

            gbc.gridy = 5;
            centerPanel.add(saveButton, gbc);

            gbc.gridy = 6;
            centerPanel.add(exitButton, gbc);
        } else {
            gbc.gridy = 4;
            centerPanel.add(shortRestButton, gbc);

            gbc.gridy = 5;
            centerPanel.add(nextBattleButton, gbc);

            gbc.gridy = 6;
            centerPanel.add(saveButton, gbc);

            gbc.gridy = 7;
            centerPanel.add(exitButton, gbc);
        }

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

    private void shortRest(JLabel hpLabel) {
        int missingHp = character.getMaxHp() - character.getCurrentHp();
        int healing = missingHp / 2;

        if (healing < 4 && missingHp > 0) {
            healing = 4;
        }

        character.heal(healing);

        hpLabel.setText("Current HP: " + character.getCurrentHp() + "/" + character.getMaxHp());
        shortRestButton.setEnabled(false);

        JOptionPane.showMessageDialog(
                this,
                "Short Rest recovered " + healing + " HP."
        );
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