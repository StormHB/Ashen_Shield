package ashen.gui;

import ashen.model.GameCharacter;
import ashen.service.SaveLoadService;
import ashen.service.HighScoreService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Screen shown after the player defeats an enemy.
 * Handles short rest, moving to the next battle, saving and campaign completion.
 */

public class VictoryPanel extends JPanel {

    private MainFrame mainFrame;
    private GameCharacter character;
    private int defeatedEnemyIndex;
    private String defeatedEnemyName;
    private SaveLoadService saveLoadService;
    private HighScoreService highScoreService;
    private JButton saveScoreButton;

    private JButton shortRestButton;

    /**
     * Creates the victory panel after a completed battle.
     *
     * @param mainFrame main frame used for navigation
     * @param character victorious character
     * @param defeatedEnemyIndex index of the defeated enemy
     * @param defeatedEnemyName name of the defeated enemy
     */

    public VictoryPanel(MainFrame mainFrame, GameCharacter character,
                        int defeatedEnemyIndex, String defeatedEnemyName) {
        this.mainFrame = mainFrame;
        this.character = character;
        this.defeatedEnemyIndex = defeatedEnemyIndex;
        this.defeatedEnemyName = defeatedEnemyName;
        this.saveLoadService = new SaveLoadService();
        this.highScoreService = new HighScoreService();

        layoutComponents();
    }

    /**
     * Builds and arranges all Swing components on this panel.
     */

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

        shortRestButton = GuiUtils.createMenuButton("Short Rest (R)");
        JButton nextBattleButton = GuiUtils.createMenuButton("Next Battle (N)");
        JButton mainMenuButton = GuiUtils.createMenuButton("Main Menu");
        JButton saveButton = GuiUtils.createMenuButton("Save Character");
        saveScoreButton = GuiUtils.createMenuButton("Save Score");
        JButton exitButton = GuiUtils.createMenuButton("Exit");

        if (!campaignCompleted) {
            setupVictoryShortcuts(shortRestButton, nextBattleButton);
            shortRestButton.addActionListener(e -> shortRest(hpLabel));
            nextBattleButton.addActionListener(e -> mainFrame.showBattle(character, defeatedEnemyIndex + 1));
        }

        mainMenuButton.addActionListener(e -> mainFrame.showMainMenu());
        saveButton.addActionListener(e -> saveCharacter());
        saveScoreButton.addActionListener(e -> saveHighScore());
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
            centerPanel.add(saveScoreButton, gbc);

            gbc.gridy = 5;
            centerPanel.add(mainMenuButton, gbc);

            gbc.gridy = 6;
            centerPanel.add(saveButton, gbc);

            gbc.gridy = 7;
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

    /**
     * Restores part of the missing HP and updates the displayed HP label.
     *
     * @param hpLabel label that displays current HP
     */

    private void shortRest(JLabel hpLabel) {

        int hpBefore = character.getCurrentHp();

        int missingHp = character.getMaxHp() - character.getCurrentHp();
        int healing = (missingHp + 1) / 2;

        if (healing < 4 && missingHp > 0) {
            healing = 4;
        }

        character.heal(healing);

        int hpAfter = character.getCurrentHp();

        hpLabel.setText("Current HP: " + hpAfter + "/" + character.getMaxHp());
        shortRestButton.setEnabled(false);

        mainFrame.appendToCampaignBattleLog(
                "\nShort Rest\n"
                        + "Recovered "
                        + (hpAfter - hpBefore)
                        + " HP ("
                        + hpBefore
                        + " -> "
                        + hpAfter
                        + ")\n\n"
        );

        JOptionPane.showMessageDialog(
                this,
                "Short Rest recovered " + (hpAfter - hpBefore) + " HP."
        );
    }

    /**
     * Saves the current character using the shared confirmation dialog.
     */

    private void saveCharacter() {
        GuiUtils.saveCharacterWithConfirmation(this, saveLoadService, character);
    }

    /**
     * Saves the completed campaign score to the high score text file.
     */
    private void saveHighScore() {
        try {
            highScoreService.saveHighScore(character);

            saveScoreButton.setEnabled(false);

            JOptionPane.showMessageDialog(
                    this,
                    "Score saved successfully.",
                    "High Score Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to save high score.",
                    "High Score Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Registers keyboard shortcuts for short rest and moving to the next battle.
     *
     * @param shortRestButton button activated by the R shortcut
     * @param nextBattleButton button activated by the N shortcut
     */

    private void setupVictoryShortcuts(
            JButton shortRestButton,
            JButton nextBattleButton
    ) {

        JRootPane rootPane = mainFrame.getRootPane();

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_R, 0),
                "shortRest"
        );

        rootPane.getActionMap().put(
                "shortRest",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (shortRestButton.isEnabled()) {
                            shortRestButton.doClick();
                        }
                    }
                }
        );

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_N, 0),
                "nextBattle"
        );

        rootPane.getActionMap().put(
                "nextBattle",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (nextBattleButton.isEnabled()) {
                            nextBattleButton.doClick();
                        }
                    }
                }
        );
    }
}