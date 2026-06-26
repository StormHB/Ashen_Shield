package ashen.gui.event;

import ashen.model.GameCharacter;

import java.util.EventListener;

/**
 * Listener for victory screen actions.
 */
public interface VictoryPanelListener extends EventListener {

    void onNextBattleRequested(GameCharacter character, int nextEnemyIndex);

    void onMainMenuRequested();

    void onExitRequested();

    void onCampaignLogUpdated(String logEntry);
}
