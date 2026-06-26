package ashen.gui.event;

import java.util.EventListener;

/**
 * Listener for keyboard shortcuts used on the battle screen.
 */
public interface BattleShortcutListener extends EventListener {

    void onAttackShortcut();

    void onNextShortcut();

    void onCharacterSheetShortcut();

    void onEnemySheetShortcut();

    void onMainMenuShortcut();

    void onExitShortcut();
}
