package ashen.gui.event;

import java.util.EventListener;

/**
 * Listener for keyboard shortcuts used on the battle screen.
 */
public interface BattleShortcutListener extends EventListener {

    /**
     * Handles the attack shortcut.
     */
    void onAttackShortcut();

    /**
     * Handles the next-screen shortcut.
     */
    void onNextShortcut();

    /**
     * Handles the character sheet shortcut.
     */
    void onCharacterSheetShortcut();

    /**
     * Handles the enemy sheet shortcut.
     */
    void onEnemySheetShortcut();

    /**
     * Handles the main menu shortcut.
     */
    void onMainMenuShortcut();

    /**
     * Handles the exit shortcut.
     */
    void onExitShortcut();
}
