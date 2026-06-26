package ashen.gui;

import ashen.model.GameCharacter;
import ashen.service.CharacterPersistenceService;

import javax.swing.*;
import java.awt.Component;
import java.io.File;

/**
 * Utility class for loading saved characters through a file chooser.
 */
public final class CharacterLoadDialog {

    private CharacterLoadDialog() {
    }

    /**
     * Opens a file chooser and loads a serialized character.
     *
     * @param parent component used as the dialog parent
     * @param saveLoadService service used for loading the character
     * @return loaded character, or null when loading is cancelled or fails
     */
    public static GameCharacter show(Component parent, CharacterPersistenceService saveLoadService) {
        JFileChooser fileChooser = new JFileChooser("DATA");
        fileChooser.setAcceptAllFileFilterUsed(true);

        fileChooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "Character Saves (*.ser)",
                        "ser"
                )
        );

        int result = fileChooser.showOpenDialog(parent);

        if (result != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File selectedFile = fileChooser.getSelectedFile();

        try {
            GameCharacter character = saveLoadService.loadCharacter(selectedFile.getPath());

            character.restoreFullHp();

            return character;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Failed to load character.",
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return null;
        }
    }
}
