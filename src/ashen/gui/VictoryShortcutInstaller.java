package ashen.gui;

import ashen.gui.event.VictoryShortcutListener;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

/**
 * Utility class for victory screen keyboard shortcuts.
 */
public final class VictoryShortcutInstaller {

    private VictoryShortcutInstaller() {
    }

    public static void install(JRootPane rootPane, VictoryShortcutListener listener) {
        register(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "shortRest", new Runnable() {
            @Override
            public void run() {
                listener.onShortRestShortcut();
            }
        });
        register(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), "nextBattle", new Runnable() {
            @Override
            public void run() {
                listener.onNextBattleShortcut();
            }
        });
    }

    private static void register(JRootPane rootPane, KeyStroke keyStroke, String name, Runnable action) {
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, name);
        rootPane.getActionMap().put(name, new ShortcutAction(action));
    }
}
