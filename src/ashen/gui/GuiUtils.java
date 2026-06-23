package ashen.gui;

import ashen.model.GameCharacter;
import ashen.service.SaveLoadService;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.awt.*;

/**
 * Utility class for shared Swing helper methods used by multiple panels.
 */

public final class GuiUtils {

    private GuiUtils() {
    }

    /**
     * Creates a styled menu button used on menu-like screens.
     *
     * @param text button label
     * @return configured JButton instance
     */

    public static JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(280, 55));
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return button;
    }

    /**
     * Saves a character after asking for confirmation when the target file exists.
     *
     * @param parent panel used as the parent for dialog windows
     * @param saveLoadService service used for serialization
     * @param character character to save
     */

    public static void saveCharacterWithConfirmation(
            JPanel parent,
            SaveLoadService saveLoadService,
            GameCharacter character
    ) {
        try {
            String filePath = "DATA/" + character.getName() + ".ser";
            File saveFile = new File(filePath);

            if (saveFile.exists()) {
                int choice = JOptionPane.showConfirmDialog(
                        parent,
                        "A saved character with this name already exists.\nDo you want to overwrite it?",
                        "Overwrite Save?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (choice != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            saveLoadService.saveCharacter(character, filePath);

            JOptionPane.showMessageDialog(
                    parent,
                    "Character saved successfully.",
                    "Save Complete",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Failed to save character.",
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}