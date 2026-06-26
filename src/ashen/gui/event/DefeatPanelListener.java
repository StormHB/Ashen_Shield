package ashen.gui.event;

import java.util.EventListener;

/**
 * Listener for defeat screen actions.
 */
public interface DefeatPanelListener extends EventListener {

    void onMainMenuRequested();

    void onExitRequested();
}
