package ashen.gui;

import ashen.gui.event.VictoryPanelListener;
import ashen.gui.event.VictoryShortcutListener;
import ashen.model.GameCharacter;
import ashen.service.CharacterPersistenceService;
import ashen.service.HighScoreProvider;
import ashen.service.RestService;
import ashen.service.ShortRestResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Screen shown after the player defeats an enemy.
 * Handles short rest, moving to the next battle, saving and campaign completion.
 */

public class VictoryPanel extends JPanel implements VictoryShortcutListener {

    private VictoryPanelListener listener;
    private JRootPane shortcutRootPane;
    private GameCharacter character;
    private int defeatedEnemyIndex;
    private String defeatedEnemyName;
    private CharacterPersistenceService saveLoadService;
    private HighScoreProvider highScoreService;
    private JButton saveScoreButton;

    private JButton shortRestButton;
    private JButton nextBattleButton;

    /**
     * Creates the victory panel after a completed battle.
     *
     * @param listener listener used for navigation
     * @param shortcutRootPane root pane used for keyboard shortcuts
     * @param character victorious character
     * @param defeatedEnemyIndex index of the defeated enemy
     * @param defeatedEnemyName name of the defeated enemy
     * @param saveLoadService service used for saving characters
     * @param highScoreService service used for saving high scores
     */

    public VictoryPanel(VictoryPanelListener listener, JRootPane shortcutRootPane, GameCharacter character,
                        int defeatedEnemyIndex, String defeatedEnemyName,
                        CharacterPersistenceService saveLoadService, HighScoreProvider highScoreService) {
        this.listener = listener;
        this.shortcutRootPane = shortcutRootPane;
        this.character = character;
        this.defeatedEnemyIndex = defeatedEnemyIndex;
        this.defeatedEnemyName = defeatedEnemyName;
        this.saveLoadService = saveLoadService;
        this.highScoreService = highScoreService;

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
        nextBattleButton = GuiUtils.createMenuButton("Next Battle (N)");
        JButton mainMenuButton = GuiUtils.createMenuButton("Main Menu");
        JButton saveButton = GuiUtils.createMenuButton("Save Character");
        saveScoreButton = GuiUtils.createMenuButton("Save Score");
        JButton exitButton = GuiUtils.createMenuButton("Exit");

        if (!campaignCompleted) {
            setupVictoryShortcuts();
            shortRestButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    shortRest(hpLabel);
                }
            });
            nextBattleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    listener.onNextBattleRequested(character, defeatedEnemyIndex + 1);
                }
            });
        }

        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onMainMenuRequested();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCharacter();
            }
        });
        saveScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveHighScore();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onExitRequested();
            }
        });

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
        ShortRestResult restResult = RestService.shortRest(character);

        hpLabel.setText("Current HP: " + restResult.getHpAfter() + "/" + character.getMaxHp());
        shortRestButton.setEnabled(false);

        listener.onCampaignLogUpdated(restResult.toBattleLogEntry());

        JOptionPane.showMessageDialog(
                this,
                "Short Rest recovered " + restResult.getRecoveredHp() + " HP."
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
     */

    private void setupVictoryShortcuts() {
        VictoryShortcutInstaller.install(shortcutRootPane, this);
    }

    @Override
    public void onShortRestShortcut() {
        if (shortRestButton.isEnabled()) {
            shortRestButton.doClick();
        }
    }

    @Override
    public void onNextBattleShortcut() {
        if (nextBattleButton.isEnabled()) {
            nextBattleButton.doClick();
        }
    }
}
