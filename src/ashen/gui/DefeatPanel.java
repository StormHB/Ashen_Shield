package ashen.gui;

import ashen.gui.event.DefeatPanelListener;
import ashen.model.GameCharacter;
import ashen.service.CharacterPersistenceService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Screen shown when the player character is defeated.
 * Allows returning to the main menu, saving the character or exiting the game.
 */

public class DefeatPanel extends JPanel {

    /**
     * Listener notified when the user leaves the defeat screen.
     */
    private DefeatPanelListener listener;

    /**
     * Defeated character shown on this panel.
     */
    private GameCharacter character;

    /**
     * Service used when the defeated character is saved.
     */
    private CharacterPersistenceService saveLoadService;

    /**
     * Creates the defeat panel for the given character.
     *
     * @param listener listener used for navigation
     * @param character defeated character
     * @param saveLoadService service used for saving characters
     */

    public DefeatPanel(DefeatPanelListener listener, GameCharacter character, CharacterPersistenceService saveLoadService) {
        this.listener = listener;
        this.character = character;
        this.saveLoadService = saveLoadService;

        layoutComponents();
    }

    /**
     * Builds and arranges all Swing components on this panel.
     */

    private void layoutComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel topTitle = new JLabel("Defeat", JLabel.CENTER);
        topTitle.setFont(new Font("SansSerif", Font.PLAIN, 20));
        topTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        add(topTitle, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());

        JLabel title = new JLabel("DEFEAT", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));

        JLabel message = new JLabel("Your journey ends here.", JLabel.CENTER);
        message.setFont(new Font("SansSerif", Font.PLAIN, 20));

        JButton mainMenuButton = GuiUtils.createMenuButton("Main Menu");
        JButton saveButton = GuiUtils.createMenuButton("Save Character");
        JButton exitButton = GuiUtils.createMenuButton("Exit");

        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onMainMenuRequested();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCharacter();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onExitRequested();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.gridy = 0;
        centerPanel.add(title, gbc);

        gbc.insets = new Insets(0, 0, 80, 0);
        gbc.gridy = 1;
        centerPanel.add(message, gbc);

        gbc.insets = new Insets(10, 0, 15, 0);

        gbc.gridy = 2;
        centerPanel.add(mainMenuButton, gbc);

        gbc.gridy = 3;
        centerPanel.add(saveButton, gbc);

        gbc.gridy = 4;
        centerPanel.add(exitButton, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Saves the current character using the shared confirmation dialog.
     */

    private void saveCharacter() {
        GuiUtils.saveCharacterWithConfirmation(this, saveLoadService, character);
    }
}
