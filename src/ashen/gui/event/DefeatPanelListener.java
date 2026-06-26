package ashen.gui.event;

import java.util.EventListener;

/**
 * Listener for defeat screen actions.
 */
public interface DefeatPanelListener extends EventListener {

    /**
     * Handles returning to the main menu from the defeat screen.
     */
    void onMainMenuRequested();

    /**
     * Handles exiting the application from the defeat screen.
     */
    void onExitRequested();
}
