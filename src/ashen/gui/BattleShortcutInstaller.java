package ashen.gui;

import ashen.gui.event.BattleShortcutListener;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Utility class for battle keyboard shortcuts.
 */
public final class BattleShortcutInstaller {

    private BattleShortcutInstaller() {
    }

    public static void install(
            JRootPane rootPane,
            BattleShortcutListener listener
    ) {
        register(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "attack", new Runnable() {
            @Override
            public void run() {
                listener.onAttackShortcut();
            }
        });
        register(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), "next", new Runnable() {
            @Override
            public void run() {
                listener.onNextShortcut();
            }
        });
        register(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK), "exit", new Runnable() {
            @Override
            public void run() {
                listener.onExitShortcut();
            }
        });
        register(
                rootPane,
                KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
                "characterSheet",
                new Runnable() {
                    @Override
                    public void run() {
                        listener.onCharacterSheetShortcut();
                    }
                }
        );
        register(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "enemySheet", new Runnable() {
            @Override
            public void run() {
                listener.onEnemySheetShortcut();
            }
        });
        register(rootPane, KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK), "mainMenu", new Runnable() {
            @Override
            public void run() {
                listener.onMainMenuShortcut();
            }
        });
    }

    private static void register(JRootPane rootPane, KeyStroke keyStroke, String name, Runnable action) {
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, name);
        rootPane.getActionMap().put(name, new ShortcutAction(action));
    }
}
