package ashen.gui;

import ashen.model.GameCharacter;
import ashen.service.CharacterPersistenceService;
import ashen.service.HighScoreProvider;

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
            CharacterPersistenceService saveLoadService,
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

    /**
     * Displays all high score categories in a tabbed dialog.
     *
     * @param parent parent component for the dialog
     * @param highScoreService service used to load high scores
     */

    public static void showHighScores(
            Component parent,
            HighScoreProvider highScoreService
    ) {
        try {
            JTabbedPane tabbedPane = new JTabbedPane();

            tabbedPane.addTab(
                    "Normal",
                    createHighScoreScrollPane(
                            highScoreService,
                            HighScoreProvider.NORMAL
                    )
            );

            tabbedPane.addTab(
                    "Hardcore - HP",
                    createHighScoreScrollPane(
                            highScoreService,
                            HighScoreProvider.HARDCORE_HP
                    )
            );

            tabbedPane.addTab(
                    "Hardcore - Damage",
                    createHighScoreScrollPane(
                            highScoreService,
                            HighScoreProvider.HARDCORE_DAMAGE
                    )
            );

            tabbedPane.addTab(
                    "Hardcore - Full",
                    createHighScoreScrollPane(
                            highScoreService,
                            HighScoreProvider.HARDCORE_FULL
                    )
            );

            JOptionPane.showMessageDialog(
                    parent,
                    tabbedPane,
                    "High Scores",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Failed to load high scores.",
                    "High Score Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Creates a scrollable text area containing all scores
     * from the specified category.
     *
     * @param highScoreService service used to load scores
     * @param category category to display
     * @return scroll pane containing formatted scores
     * @throws IOException if scores cannot be loaded
     */

    private static JScrollPane createHighScoreScrollPane(
            HighScoreProvider highScoreService,
            String category
    ) throws IOException {

        JTextArea highScoresArea = new JTextArea(
                highScoreService.loadHighScoresForCategory(category),
                15,
                40
        );

        highScoresArea.setEditable(false);
        highScoresArea.setLineWrap(true);
        highScoresArea.setWrapStyleWord(true);
        highScoresArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        return new JScrollPane(highScoresArea);
    }
}
