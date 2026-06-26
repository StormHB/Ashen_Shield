package ashen.gui;

import ashen.gui.event.BattleMenuListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Factory for the battle menu bar.
 */
public final class BattleMenuFactory {

    private BattleMenuFactory() {
    }

    public static JMenuBar createBattleMenuBar(BattleMenuListener listener) {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createFileMenu(listener));
        menuBar.add(createSheetsMenu(listener));

        return menuBar;
    }

    private static JMenu createFileMenu(BattleMenuListener listener) {
        JMenu fileMenu = new JMenu("File");

        JMenuItem mainMenuItem = createMenuItem(
                "Main Menu",
                KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK),
                new Runnable() {
                    @Override
                    public void run() {
                        listener.onMainMenuRequestedFromBattle();
                    }
                }
        );

        JMenuItem saveItem = createMenuItem(
                "Save",
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK),
                new Runnable() {
                    @Override
                    public void run() {
                        listener.onSaveRequested();
                    }
                }
        );

        JMenuItem saveAsItem = createMenuItem(
                "Save As",
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
                new Runnable() {
                    @Override
                    public void run() {
                        listener.onSaveAsRequested();
                    }
                }
        );

        JMenuItem loadItem = createMenuItem(
                "Load",
                KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK),
                new Runnable() {
                    @Override
                    public void run() {
                        listener.onLoadRequested();
                    }
                }
        );

        JMenuItem exportLogItem = createMenuItem(
                "Export Battle Log",
                KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK),
                new Runnable() {
                    @Override
                    public void run() {
                        listener.onExportBattleLogRequested();
                    }
                }
        );

        JMenuItem highScoresItem = createMenuItem(
                "High Scores",
                KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK),
                new Runnable() {
                    @Override
                    public void run() {
                        listener.onHighScoresRequestedFromBattle();
                    }
                }
        );

        JMenuItem exitItem = createMenuItem(
                "Exit",
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK),
                new Runnable() {
                    @Override
                    public void run() {
                        listener.onExitRequestedFromBattle();
                    }
                }
        );

        fileMenu.add(mainMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(loadItem);
        fileMenu.add(exportLogItem);
        fileMenu.add(highScoresItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        return fileMenu;
    }

    private static JMenu createSheetsMenu(BattleMenuListener listener) {
        JMenu sheetsMenu = new JMenu("Sheets");

        JMenuItem characterSheetItem = createMenuItem(
                "Character Sheet",
                KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
                new Runnable() {
                    @Override
                    public void run() {
                        listener.onCharacterSheetRequested();
                    }
                }
        );

        JMenuItem enemySheetItem = createMenuItem(
                "Enemy Sheet",
                KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK),
                new Runnable() {
                    @Override
                    public void run() {
                        listener.onEnemySheetRequested();
                    }
                }
        );

        sheetsMenu.add(characterSheetItem);
        sheetsMenu.add(enemySheetItem);

        return sheetsMenu;
    }

    private static JMenuItem createMenuItem(String text, KeyStroke accelerator, Runnable action) {
        JMenuItem menuItem = new JMenuItem(text);

        menuItem.setAccelerator(accelerator);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });

        return menuItem;
    }
}
