package ashen.gui;

import ashen.gui.event.MainMenuListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main menu screen of the application.
 * Provides actions for creating a new character, loading a saved character
 * viewing high scores and exiting the program.
 */

public class MainMenuPanel extends JPanel {

    private MainMenuListener listener;

    /**
     * Creates the main menu panel.
     *
     * @param listener listener that handles navigation actions
     */

    public MainMenuPanel(MainMenuListener listener) {
        this.listener = listener;
        layoutComponents();
    }

    /**
     * Builds and arranges all Swing components on this panel.
     */

    private void layoutComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel topTitle = new JLabel("Ashen Shield", JLabel.CENTER);
        topTitle.setFont(new Font("SansSerif", Font.PLAIN, 20));
        topTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        add(topTitle, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());

        JLabel title = new JLabel("ASHEN SHIELD", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));

        JLabel subtitle = new JLabel("Character Creation & Turn-Based Fantasy Combat", JLabel.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 20));

        JButton newCharacterButton = GuiUtils.createMenuButton("New Character");
        JButton loadCharacterButton = GuiUtils.createMenuButton("Load Character");
        JButton highScoresButton = GuiUtils.createMenuButton("High Scores");
        JButton exitButton = GuiUtils.createMenuButton("Exit");

        loadCharacterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onLoadCharacterRequested();
            }
        });
        highScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onHighScoresRequested(MainMenuPanel.this);
            }
        });
        newCharacterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onNewCharacterRequested();
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

        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.gridy = 0;
        centerPanel.add(title, gbc);

        gbc.insets = new Insets(0, 0, 100, 0);
        gbc.gridy = 1;
        centerPanel.add(subtitle, gbc);

        gbc.insets = new Insets(10, 0, 15, 0);
        gbc.gridy = 2;
        centerPanel.add(newCharacterButton, gbc);

        gbc.gridy = 3;
        centerPanel.add(loadCharacterButton, gbc);

        gbc.gridy = 4;
        centerPanel.add(highScoresButton, gbc);

        gbc.gridy = 5;
        centerPanel.add(exitButton, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }
}
