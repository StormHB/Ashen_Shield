package ashen.service;

import ashen.model.GameCharacter;

import java.io.IOException;

/**
 * Abstraction for saving and loading characters.
 */
public interface CharacterPersistenceService {

    /**
     * Saves a character to a binary file.
     *
     * @param character character to save
     * @param filePath destination file path
     * @throws IOException if the character cannot be written
     */
    void saveCharacter(GameCharacter character, String filePath) throws IOException;

    /**
     * Loads a character from a binary file.
     *
     * @param filePath source file path
     * @return loaded character
     * @throws IOException if the file cannot be read
     * @throws ClassNotFoundException if the serialized class cannot be resolved
     */
    GameCharacter loadCharacter(String filePath) throws IOException, ClassNotFoundException;
}
