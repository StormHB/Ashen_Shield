package ashen.gui.event;

import ashen.model.GameCharacter;

import java.util.EventListener;

/**
 * Listener for battle screen events and battle-log integration.
 */
public interface BattlePanelListener extends EventListener {

    /**
     * Handles transition from battle to the victory screen.
     *
     * @param character victorious character
     * @param defeatedEnemyIndex index of the defeated enemy
     * @param defeatedEnemyName name of the defeated enemy
     */
    void onBattleWon(GameCharacter character, int defeatedEnemyIndex, String defeatedEnemyName);

    /**
     * Handles transition from battle to the defeat screen.
     *
     * @param character defeated character
     */
    void onPlayerDefeated(GameCharacter character);

    /**
     * Handles returning to the main menu.
     */
    void onMainMenuRequested();

    /**
     * Handles loading a saved character.
     */
    void onLoadCharacterRequested();

    /**
     * Handles exiting the application.
     */
    void onExitRequested();

    /**
     * Adds a finished battle log to the campaign log.
     *
     * @param battleLog completed battle log text
     */
    void onBattleLogCompleted(String battleLog);

    /**
     * Returns the campaign log collected across battles.
     *
     * @return complete campaign battle log
     */
    String getCampaignBattleLog();
}
