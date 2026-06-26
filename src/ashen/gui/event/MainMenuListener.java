package ashen.gui.event;

import java.awt.Component;
import java.util.EventListener;

/**
 * Listener for actions raised from the main menu screen.
 */
public interface MainMenuListener extends EventListener {

    void onNewCharacterRequested();

    void onLoadCharacterRequested();

    void onHighScoresRequested(Component parent);

    void onExitRequested();
}
