package ashen.gui;

import ashen.gui.event.BattlePanelListener;
import ashen.gui.event.CharacterCreationListener;
import ashen.gui.event.DefeatPanelListener;
import ashen.gui.event.MainMenuListener;
import ashen.gui.event.VictoryPanelListener;
import ashen.model.GameCharacter;
import ashen.service.CharacterPersistenceService;
import ashen.service.HighScoreProvider;
import ashen.service.HighScoreService;
import ashen.service.SaveLoadService;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window that controls panel navigation.
 */

public class MainFrame extends JFrame implements MainMenuListener, CharacterCreationListener,
        BattlePanelListener, VictoryPanelListener, DefeatPanelListener {

    private StringBuilder campaignBattleLog = new StringBuilder();
    private final CharacterPersistenceService saveLoadService;
    private final HighScoreProvider highScoreService;

    /**
     * Creates the main frame and initializes the application layout.
     */

    public MainFrame() {
        this(new SaveLoadService(), new HighScoreService());
    }

    MainFrame(CharacterPersistenceService saveLoadService, HighScoreProvider highScoreService) {
        super("Ashen Shield");

        this.saveLoadService = saveLoadService;
        this.highScoreService = highScoreService;

        initFrame();
        showMainMenu();
    }

    private void initFrame() {
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Displays the defeat screen for the selected character.
     *
     * @param character character that was defeated in battle
     */

    public void showDefeatPanel(GameCharacter character) {
        clearShortcuts();
        clearMenuBar();
        setContentPane(new DefeatPanel(this, character, saveLoadService));
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
        BattlePanel battlePanel = new BattlePanel(
                this,
                getRootPane(),
                character,
                enemyIndex,
                saveLoadService,
                highScoreService
        );

        setJMenuBar(battlePanel.createBattleMenuBar());
        setContentPane(battlePanel);
        revalidate();
        repaint();
    }

    /**
     * Opens a file chooser, loads a saved character and starts a new battle.
     */

    public void loadCharacter() {
        GameCharacter character = CharacterLoadDialog.show(this, saveLoadService);

        if (character != null) {
            showBattle(character);
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
        setContentPane(new VictoryPanel(
                this,
                getRootPane(),
                character,
                defeatedEnemyIndex,
                defeatedEnemyName,
                saveLoadService,
                highScoreService
        ));
        revalidate();
        repaint();
    }

    @Override
    public void onNewCharacterRequested() {
        showCharacterCreation();
    }

    @Override
    public void onLoadCharacterRequested() {
        loadCharacter();
    }

    @Override
    public void onHighScoresRequested(Component parent) {
        GuiUtils.showHighScores(parent, highScoreService);
    }

    @Override
    public void onMainMenuRequested() {
        showMainMenu();
    }

    @Override
    public void onExitRequested() {
        System.exit(0);
    }

    @Override
    public void onCharacterCreationCancelled() {
        showMainMenu();
    }

    @Override
    public void onCharacterCreated(GameCharacter character) {
        showBattle(character);
    }

    @Override
    public void onBattleWon(GameCharacter character, int defeatedEnemyIndex, String defeatedEnemyName) {
        showVictoryPanel(character, defeatedEnemyIndex, defeatedEnemyName);
    }

    @Override
    public void onPlayerDefeated(GameCharacter character) {
        showDefeatPanel(character);
    }

    @Override
    public void onBattleLogCompleted(String battleLog) {
        appendToCampaignBattleLog(battleLog);
    }

    @Override
    public void onNextBattleRequested(GameCharacter character, int nextEnemyIndex) {
        showBattle(character, nextEnemyIndex);
    }

    @Override
    public void onCampaignLogUpdated(String logEntry) {
        appendToCampaignBattleLog(logEntry);
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
