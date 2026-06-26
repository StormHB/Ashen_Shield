package ashen.gui.event;

import ashen.model.GameCharacter;

import java.util.EventListener;

/**
 * Listener for battle screen events and battle-log integration.
 */
public interface BattlePanelListener extends EventListener {

    void onBattleWon(GameCharacter character, int defeatedEnemyIndex, String defeatedEnemyName);

    void onPlayerDefeated(GameCharacter character);

    void onMainMenuRequested();

    void onLoadCharacterRequested();

    void onExitRequested();

    void onBattleLogCompleted(String battleLog);

    String getCampaignBattleLog();
}
