package ashen.gui.event;

import java.awt.Component;
import java.util.EventListener;

/**
 * Listener for actions raised from the main menu screen.
 */
public interface MainMenuListener extends EventListener {

    /**
     * Handles starting a new character creation flow.
     */
    void onNewCharacterRequested();

    /**
     * Handles loading a saved character.
     */
    void onLoadCharacterRequested();

    /**
     * Handles displaying the high score dialog.
     *
     * @param parent component used as the dialog parent
     */
    void onHighScoresRequested(Component parent);

    /**
     * Handles exiting the application from the main menu.
     */
    void onExitRequested();
}
