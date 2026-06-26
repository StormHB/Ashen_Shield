package ashen.gui.event;

import ashen.model.GameCharacter;

import java.util.EventListener;

/**
 * Listener for character creation screen events.
 */
public interface CharacterCreationListener extends EventListener {

    void onCharacterCreationCancelled();

    void onCharacterCreated(GameCharacter character);
}
