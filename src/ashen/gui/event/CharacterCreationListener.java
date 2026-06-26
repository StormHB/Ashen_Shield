package ashen.gui.event;

import ashen.model.GameCharacter;

import java.util.EventListener;

/**
 * Listener for character creation screen events.
 */
public interface CharacterCreationListener extends EventListener {

    /**
     * Handles cancellation of character creation.
     */
    void onCharacterCreationCancelled();

    /**
     * Handles a newly created character.
     *
     * @param character character created from the form
     */
    void onCharacterCreated(GameCharacter character);
}
