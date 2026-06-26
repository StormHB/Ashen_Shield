package ashen.gui.event;

import java.util.EventListener;

/**
 * Listener for keyboard shortcuts used on the victory screen.
 */
public interface VictoryShortcutListener extends EventListener {

    /**
     * Handles the short rest shortcut.
     */
    void onShortRestShortcut();

    /**
     * Handles the next battle shortcut.
     */
    void onNextBattleShortcut();
}
