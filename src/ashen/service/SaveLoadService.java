package ashen.service;

import ashen.model.GameCharacter;

import java.io.*;

/**
 * Service class responsible for saving and loading characters with Java serialization.
 */

public class SaveLoadService {

    /**
     * Saves a character to the selected file path.
     * Creates the parent folder when it does not exist.
     *
     * @param character character to save
     * @param filePath destination file path
     * @throws IOException if saving fails
     */

    public void saveCharacter(GameCharacter character, String filePath) throws IOException {
        File saveFile = new File(filePath);
        File parentFolder = saveFile.getParentFile();

        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(saveFile))) {
            out.writeObject(character);
        }
    }

    /**
     * Loads a character from a serialized file.
     *
     * @param filePath source file path
     * @return loaded character
     * @throws IOException if reading the file fails
     * @throws ClassNotFoundException if the serialized class cannot be found
     */

    public GameCharacter loadCharacter(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            return (GameCharacter) inputStream.readObject();
        }
    }
}