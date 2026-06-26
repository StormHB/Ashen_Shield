package ashen.gui.event;

import java.util.EventListener;

/**
 * Listener for battle menu actions.
 */
public interface BattleMenuListener extends EventListener {

    /**
     * Handles returning from battle to the main menu.
     */
    void onMainMenuRequestedFromBattle();

    /**
     * Handles saving the current character to the default file.
     */
    void onSaveRequested();

    /**
     * Handles saving the current character to a selected file.
     */
    void onSaveAsRequested();

    /**
     * Handles loading a saved character from the battle menu.
     */
    void onLoadRequested();

    /**
     * Handles exporting the battle log to a text file.
     */
    void onExportBattleLogRequested();

    /**
     * Handles showing high scores from the battle menu.
     */
    void onHighScoresRequestedFromBattle();

    /**
     * Handles exiting the application from the battle menu.
     */
    void onExitRequestedFromBattle();

    /**
     * Handles opening the character sheet dialog.
     */
    void onCharacterSheetRequested();

    /**
     * Handles opening the enemy sheet dialog.
     */
    void onEnemySheetRequested();
}
