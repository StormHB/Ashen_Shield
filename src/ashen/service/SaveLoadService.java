package ashen.service;

import ashen.model.GameCharacter;

import java.io.*;

public class SaveLoadService {

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

    public GameCharacter loadCharacter(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            return (GameCharacter) inputStream.readObject();
        }
    }
}