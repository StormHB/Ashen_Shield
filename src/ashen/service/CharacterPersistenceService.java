package ashen.service;

import ashen.model.GameCharacter;

import java.io.IOException;

/**
 * Abstraction for saving and loading characters.
 */
public interface CharacterPersistenceService {

    void saveCharacter(GameCharacter character, String filePath) throws IOException;

    GameCharacter loadCharacter(String filePath) throws IOException, ClassNotFoundException;
}
