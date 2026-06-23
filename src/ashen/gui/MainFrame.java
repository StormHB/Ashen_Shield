package ashen.gui;

import ashen.model.GameCharacter;
import ashen.service.SaveLoadService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Main application window that controls panel navigation.
 * Uses a CardLayout to switch between the main menu, character creation,
 * battle, victory and defeat screens.
 */

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private Container contentPane;

    private StringBuilder campaignBattleLog = new StringBuilder();

    /**
     * Creates the main frame and initializes the application layout.
     */

    public MainFrame() {
        super("Ashen Shield");

        initFrame();
        initLayout();
        showMainMenu();
    }

    private void initFrame() {
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initLayout() {
        cardLayout = new CardLayout();
        contentPane = getContentPane();
        contentPane.setLayout(cardLayout);

        contentPane.add(new MainMenuPanel(this), "mainMenu");
        contentPane.add(new CharacterCreationPanel(this), "characterCreation");
    }

    /**
     * Displays the defeat screen for the selected character.
     *
     * @param character character that was defeated in battle
     */

    public void showDefeatPanel(GameCharacter character) {
        clearShortcuts();
        clearMenuBar();
        setContentPane(new DefeatPanel(this, character));
        revalidate();
        repaint();
    }

    /**
     * Displays the main menu and clears battle-specific menu actions.
     */

    public void showMainMenu() {
        clearShortcuts();
        clearMenuBar();
        setContentPane(new MainMenuPanel(this));
        revalidate();
        repaint();
    }

    /**
     * Displays the character creation screen.
     */

    public void showCharacterCreation() {
        clearShortcuts();
        clearMenuBar();
        setContentPane(new CharacterCreationPanel(this));
        revalidate();
        repaint();
    }

    /**
     * Starts a new battle sequence from the first enemy.
     *
     * @param character character used in combat
     */

    public void showBattle(GameCharacter character) {
        clearCampaignBattleLog();
        showBattle(character, 0);
    }

    /**
     * Displays the battle screen for a specific enemy index.
     *
     * @param character character used in combat
     * @param enemyIndex index of the enemy to fight
     */

    public void showBattle(GameCharacter character, int enemyIndex) {
        clearShortcuts();
        setContentPane(new BattlePanel(this, character, enemyIndex));
        revalidate();
        repaint();
    }

    /**
     * Opens a file chooser, loads a saved character and starts a new battle.
     */

    public void loadCharacter() {
        JFileChooser fileChooser = new JFileChooser("DATA");
        fileChooser.setAcceptAllFileFilterUsed(true);

        fileChooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "Character Saves (*.ser)",
                        "ser"
                )
        );

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                SaveLoadService saveLoadService = new SaveLoadService();
                GameCharacter character = saveLoadService.loadCharacter(selectedFile.getPath());

                character.restoreFullHp();

                showBattle(character);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to load character.",
                        "Load Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    /**
     * Displays the victory screen after an enemy is defeated.
     *
     * @param character character that won the battle
     * @param defeatedEnemyIndex index of the defeated enemy
     * @param defeatedEnemyName name of the defeated enemy
     */

    public void showVictoryPanel(GameCharacter character, int defeatedEnemyIndex, String defeatedEnemyName) {
        clearShortcuts();
        clearMenuBar();
        setContentPane(new VictoryPanel(this, character, defeatedEnemyIndex, defeatedEnemyName));
        revalidate();
        repaint();
    }

    /**
     * Adds text to the campaign battle log used for export.
     *
     * @param text log text to append
     */

    public void appendToCampaignBattleLog(String text) {
        campaignBattleLog.append(text);
    }

    /**
     * Returns the collected campaign battle log.
     *
     * @return complete campaign battle log text
     */

    public String getCampaignBattleLog() {
        return campaignBattleLog.toString();
    }

    /**
     * Clears the campaign battle log when starting a new run.
     */

    public void clearCampaignBattleLog() {
        campaignBattleLog.setLength(0);
    }

    private void clearMenuBar() {
        setJMenuBar(null);
    }

    private void clearShortcuts() {
        JRootPane rootPane = getRootPane();

        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        inputMap.clear();
        actionMap.clear();
    }
}
