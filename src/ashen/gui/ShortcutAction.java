package ashen.gui;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
 * Adapter that connects a Swing Action to a simple command.
 */
final class ShortcutAction extends AbstractAction {

    private final Runnable command;

    ShortcutAction(Runnable command) {
        this.command = command;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        command.run();
    }
}
