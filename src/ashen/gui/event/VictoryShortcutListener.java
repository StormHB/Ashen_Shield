package ashen.gui.event;

import java.util.EventListener;

/**
 * Listener for keyboard shortcuts used on the victory screen.
 */
public interface VictoryShortcutListener extends EventListener {

    void onShortRestShortcut();

    void onNextBattleShortcut();
}
