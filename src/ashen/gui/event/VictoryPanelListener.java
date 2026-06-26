package ashen.gui.event;

import ashen.model.GameCharacter;

import java.util.EventListener;

/**
 * Listener for victory screen actions.
 */
public interface VictoryPanelListener extends EventListener {

    /**
     * Handles moving from the victory screen to the next battle.
     *
     * @param character victorious character
     * @param nextEnemyIndex index of the next enemy
     */
    void onNextBattleRequested(GameCharacter character, int nextEnemyIndex);

    /**
     * Handles returning to the main menu.
     */
    void onMainMenuRequested();

    /**
     * Handles exiting the application.
     */
    void onExitRequested();

    /**
     * Adds victory-screen events such as short rests to the campaign log.
     *
     * @param logEntry text to append to the campaign log
     */
    void onCampaignLogUpdated(String logEntry);
}
