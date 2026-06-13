package ashen.service;

import ashen.model.GameCharacter;

import java.io.*;

public class SaveLoadService {

    public void saveCharacter(GameCharacter character, String filePath) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(character);
        }
    }

    public GameCharacter loadCharacter(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            return (GameCharacter) inputStream.readObject();
        }
    }
}