package ashen.gui;

import ashen.combat.BattleController;
import ashen.combat.BattleRules;
import ashen.combat.event.BattleEventListener;
import ashen.gui.event.BattleMenuListener;
import ashen.gui.event.BattlePanelListener;
import ashen.gui.event.BattleShortcutListener;
import ashen.model.Enemy;
import ashen.model.GameCharacter;
import ashen.model.Stats;
import ashen.service.BattleLogService;
import ashen.service.CharacterPersistenceService;
import ashen.service.HighScoreProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Main combat screen for Ashen Shield.
 * Displays battle state and delegates combat rules to BattleController.
 */
public class BattlePanel extends JPanel implements BattleEventListener, BattleShortcutListener, BattleMenuListener {

    private final BattlePanelListener listener;
    private final JRootPane shortcutRootPane;
    private final BattleController battleController;
    private final CharacterPersistenceService saveLoadService;
    private final HighScoreProvider highScoreService;

    private boolean currentBattleLogSaved;

    private JLabel playerHpLabel;
    private JLabel enemyHpLabel;

    private JTextArea battleLogArea;
    private JButton attackButton;
    private JButton nextButton;

    /**
     * Creates a battle panel for the selected character and enemy.
     *
     * @param listener listener used for battle navigation and campaign log updates
     * @param shortcutRootPane root pane used for keyboard shortcuts
     * @param character player character used in combat
     * @param enemyIndex index of the enemy to fight
     * @param saveLoadService service used for saving characters
     * @param highScoreService service used for displaying high scores
     */
    public BattlePanel(
            BattlePanelListener listener,
            JRootPane shortcutRootPane,
            GameCharacter character,
            int enemyIndex,
            CharacterPersistenceService saveLoadService,
            HighScoreProvider highScoreService
    ) {
        this.listener = listener;
        this.shortcutRootPane = shortcutRootPane;
        this.saveLoadService = saveLoadService;
        this.highScoreService = highScoreService;
        this.battleController = new BattleController(character, enemyIndex, this);

        layoutComponents();
    }

    /**
     * Builds the battle screen layout, including player data, enemy data,
     * battle log, action buttons and keyboard shortcuts.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        mainPanel.add(createPlayerPanel());
        mainPanel.add(createEnemyPanel());

        add(mainPanel, BorderLayout.NORTH);

        battleLogArea = new JTextArea();
        battleLogArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        battleLogArea.setEditable(false);
        battleLogArea.setLineWrap(true);
        battleLogArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(battleLogArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Battle Log"
        ));

        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));

        attackButton = new JButton("Attack (A)");
        attackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAttack();
            }
        });
        attackButton.setPreferredSize(new Dimension(120, 40));

        nextButton = new JButton("Next (N)");
        nextButton.setPreferredSize(new Dimension(120, 40));
        nextButton.setVisible(false);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateAfterBattle();
            }
        });

        actionPanel.add(attackButton);
        actionPanel.add(nextButton);

        add(actionPanel, BorderLayout.SOUTH);

        appendBattleLog(
                "Battle "
                        + (battleController.getCurrentEnemyIndex() + 1)
                        + "/"
                        + battleController.getEnemyCount()
                        + " started!\n"
        );
        appendBattleLog(
                battleController.getCharacter().getName()
                        + " encounters "
                        + battleController.getEnemy().getName()
                        + ".\n\n"
        );

        setupBattleShortcuts();
    }

    /**
     * Creates the panel that displays player combat information.
     *
     * @return configured player information panel
     */
    private JPanel createPlayerPanel() {
        GameCharacter character = battleController.getCharacter();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Player"
        ));

        playerHpLabel = new JLabel("HP: " + character.getCurrentHp() + "/" + character.getMaxHp());
        JLabel playerAcLabel = new JLabel("AC: " + BattleRules.calculateArmorClass(character));

        panel.add(new JLabel("Name: " + character.getName()));
        panel.add(new JLabel("Race: " + character.getRace()));
        panel.add(new JLabel("Class: " + character.getCharacterClass()));
        panel.add(playerHpLabel);
        panel.add(playerAcLabel);
        panel.add(new JLabel("Attack Bonus: " + Stats.formatModifier(BattleRules.calculateAttackBonus(character))));
        panel.add(new JLabel("Weapon: " + character.getWeapon()));
        panel.add(new JLabel("Armor: " + character.getArmor()));

        return panel;
    }

    /**
     * Creates the panel that displays enemy combat information.
     *
     * @return configured enemy information panel
     */
    private JPanel createEnemyPanel() {
        Enemy enemy = battleController.getEnemy();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Enemy"
        ));

        enemyHpLabel = new JLabel("HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp());
        JLabel enemyAcLabel = new JLabel("AC: " + enemy.getArmorClass());

        panel.add(new JLabel("Name: " + enemy.getName()));
        panel.add(enemyHpLabel);
        panel.add(enemyAcLabel);
        panel.add(new JLabel("Attack Bonus: " + Stats.formatModifier(enemy.getAttackBonus())));

        return panel;
    }

    /**
     * Handles the attack button action and scrolls the battle log after combat text is added.
     */
    private void handleAttack() {
        battleController.playerAttack(true);
        scrollBattleLogToBottom();
    }

    private void navigateAfterBattle() {
        GameCharacter character = battleController.getCharacter();

        if (battleController.isPlayerDefeated()) {
            listener.onPlayerDefeated(character);
        } else {
            listener.onBattleWon(
                    character,
                    battleController.getCurrentEnemyIndex(),
                    battleController.getEnemy().getName()
            );
        }
    }

    private void scrollBattleLogToBottom() {
        battleLogArea.setCaretPosition(
                battleLogArea.getDocument().getLength()
        );
    }

    @Override
    public void appendBattleLog(String text) {
        battleLogArea.append(text);
    }

    @Override
    public void updatePlayerHp(int currentHp, int maxHp) {
        playerHpLabel.setText("HP: " + currentHp + "/" + maxHp);
    }

    @Override
    public void updateEnemyHp(int currentHp, int maxHp) {
        enemyHpLabel.setText("HP: " + currentHp + "/" + maxHp);
    }

    @Override
    public void enemyDefeated() {
        attackButton.setEnabled(false);
        nextButton.setVisible(true);
        scrollBattleLogToBottom();
        saveCurrentBattleLogToCampaignLog();
    }

    @Override
    public void playerDefeated() {
        attackButton.setEnabled(false);
        nextButton.setVisible(true);
        nextButton.setText("Continue (N)");
        scrollBattleLogToBottom();
        saveCurrentBattleLogToCampaignLog();
    }

    /**
     * Saves the current character to the default DATA folder path.
     */
    private void saveCharacter() {
        GameCharacter character = battleController.getCharacter();
        String filePath = "DATA/" + character.getName() + ".ser";

        saveCharacterToFile(filePath);
    }

    /**
     * Opens a file chooser and saves the current character to a selected file.
     */
    private void saveCharacterAs() {
        GameCharacter character = battleController.getCharacter();

        JFileChooser fileChooser = new JFileChooser(new File("DATA"));
        fileChooser.setDialogTitle("Save Character As");
        fileChooser.setSelectedFile(new File(character.getName() + ".ser"));

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            if (!filePath.endsWith(".ser")) {
                filePath += ".ser";
            }

            saveCharacterToFile(filePath);
        }
    }

    /**
     * Saves the current character to a specific file path after overwrite confirmation.
     *
     * @param filePath destination save file path
     */
    private void saveCharacterToFile(String filePath) {
        File saveFile = new File(filePath);

        if (saveFile.exists()) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "A save file with this name already exists.\nDo you want to overwrite it?",
                    "Overwrite Save",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            saveLoadService.saveCharacter(battleController.getCharacter(), filePath);

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

    /**
     * Opens a dialog window with detailed character information.
     */
    private void showCharacterSheet() {
        BattleSheetDialogs.showCharacterSheet(this, battleController.getCharacter());
    }

    /**
     * Opens a dialog window with detailed enemy information.
     */
    private void showEnemySheet() {
        BattleSheetDialogs.showEnemySheet(this, battleController.getEnemy());
    }

    /**
     * Exports the campaign battle log to a text file selected by the user.
     */
    private void exportBattleLog() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Export Battle Log");

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            if (!filePath.endsWith(".txt")) {
                filePath += ".txt";
            }

            try {
                String exportText = listener.getCampaignBattleLog();

                if (!currentBattleLogSaved) {
                    exportText += battleLogArea.getText();
                }

                BattleLogService.exportBattleLog(exportText, filePath);
                JOptionPane.showMessageDialog(
                        this,
                        "Battle log exported successfully."
                );
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to export battle log.",
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    /**
     * Adds the current battle log to the shared campaign log once per battle.
     */
    private void saveCurrentBattleLogToCampaignLog() {
        if (!currentBattleLogSaved) {
            listener.onBattleLogCompleted(battleLogArea.getText());
            currentBattleLogSaved = true;
        }
    }

    /**
     * Creates the battle menu bar with save, load, sheet, export and exit actions.
     *
     * @return configured battle menu bar
     */
    JMenuBar createBattleMenuBar() {
        return BattleMenuFactory.createBattleMenuBar(this);
    }

    /**
     * Registers keyboard shortcuts used during battle.
     */
    private void setupBattleShortcuts() {
        BattleShortcutInstaller.install(
                shortcutRootPane,
                this
        );
    }

    @Override
    public void onAttackShortcut() {
        if (attackButton.isEnabled()) {
            handleAttack();
        }
    }

    @Override
    public void onNextShortcut() {
        if (nextButton.isVisible() && nextButton.isEnabled()) {
            nextButton.doClick();
        }
    }

    @Override
    public void onCharacterSheetShortcut() {
        showCharacterSheet();
    }

    @Override
    public void onEnemySheetShortcut() {
        showEnemySheet();
    }

    @Override
    public void onMainMenuShortcut() {
        confirmAndReturnToMainMenu();
    }

    @Override
    public void onExitShortcut() {
        listener.onExitRequested();
    }

    @Override
    public void onMainMenuRequestedFromBattle() {
        confirmAndReturnToMainMenu();
    }

    @Override
    public void onSaveRequested() {
        saveCharacter();
    }

    @Override
    public void onSaveAsRequested() {
        saveCharacterAs();
    }

    @Override
    public void onLoadRequested() {
        listener.onLoadCharacterRequested();
    }

    @Override
    public void onExportBattleLogRequested() {
        exportBattleLog();
    }

    @Override
    public void onHighScoresRequestedFromBattle() {
        GuiUtils.showHighScores(this, highScoreService);
    }

    @Override
    public void onExitRequestedFromBattle() {
        listener.onExitRequested();
    }

    @Override
    public void onCharacterSheetRequested() {
        showCharacterSheet();
    }

    @Override
    public void onEnemySheetRequested() {
        showEnemySheet();
    }

    private void confirmAndReturnToMainMenu() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Return to Main Menu? Current battle progress will be lost.",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            listener.onMainMenuRequested();
        }
    }
}
