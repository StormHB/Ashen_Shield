package ashen.gui.event;

import java.util.EventListener;

/**
 * Listener for battle menu actions.
 */
public interface BattleMenuListener extends EventListener {

    void onMainMenuRequestedFromBattle();

    void onSaveRequested();

    void onSaveAsRequested();

    void onLoadRequested();

    void onExportBattleLogRequested();

    void onHighScoresRequestedFromBattle();

    void onExitRequestedFromBattle();

    void onCharacterSheetRequested();

    void onEnemySheetRequested();
}
