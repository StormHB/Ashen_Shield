package ashen.gui;

import javax.swing.*;
import java.awt.*;

public class CharacterCreationPanel extends JPanel {

    private MainFrame mainFrame;
    private JTextField nameField;
    private JComboBox<String> raceBox;
    private JComboBox<String> classBox;
    private JComboBox<String> weaponBox;
    private JComboBox<String> armorBox;

    private JLabel pointsRemainingLabel;
    private int pointsRemaining = 12;

    private int strength = 10;
    private int dexterity = 10;
    private int constitution = 10;
    private int intelligence = 10;
    private int wisdom = 10;
    private int charisma = 10;

    private JLabel strengthValueLabel;
    private JLabel dexterityValueLabel;
    private JLabel constitutionValueLabel;
    private JLabel intelligenceValueLabel;
    private JLabel wisdomValueLabel;
    private JLabel charismaValueLabel;

    private JLabel strengthModifierLabel;
    private JLabel dexterityModifierLabel;
    private JLabel constitutionModifierLabel;
    private JLabel intelligenceModifierLabel;
    private JLabel wisdomModifierLabel;
    private JLabel charismaModifierLabel;

    public CharacterCreationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        layoutComponents();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel topTitle = new JLabel("Character Creation", JLabel.CENTER);
        topTitle.setFont(new Font("SansSerif", Font.PLAIN, 20));
        topTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        add(topTitle, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel leftPanel = createSectionPanel("Stats");
        addStatsPanel(leftPanel);
        JPanel rightPanel = createSectionPanel("Character Options");
        addCharacterOptions(rightPanel);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));

        JButton backButton = new JButton("Back");
        JButton createButton = new JButton("Create Character");

        backButton.addActionListener(e -> mainFrame.showMainMenu());

        bottomPanel.add(backButton);
        bottomPanel.add(createButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), title));
        return panel;
    }

    private void addCharacterOptions(JPanel panel) {
        JPanel formPanel = new JPanel(new GridBagLayout());

        nameField = new JTextField(15);
        raceBox = new JComboBox<>(new String[]{"Human", "Elf", "Dwarf", "Half-Orc", "Dragonborn"});
        classBox = new JComboBox<>(new String[]{"Fighter", "Rogue", "Wizard", "Druid", "Ranger"});
        weaponBox = new JComboBox<>(new String[] {"Longsword", "Dagger", "Quarterstaff", "Scimitar", "Longbow", "Shortbow"});
        armorBox = new JComboBox<>(new String[] {"Cloth Robe", "Leather Armor", "Hide Armor", "Chain Mail", "Plate Armor"});

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(formPanel, gbc, 0, "Name:", nameField);
        addFormRow(formPanel, gbc, 1, "Race:", raceBox);
        addFormRow(formPanel, gbc, 2, "Class:", classBox);
        addFormRow(formPanel, gbc, 3, "Weapon", weaponBox);
        addFormRow(formPanel, gbc, 4, "Armor", armorBox);

        panel.add(formPanel, BorderLayout.NORTH);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(component, gbc);
    }

    private void addStatsPanel(JPanel panel) {
        JPanel statsPanel = new JPanel(new GridBagLayout());

        pointsRemainingLabel = new JLabel("Points Remaining: " + pointsRemaining);
        pointsRemainingLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        strengthValueLabel = new JLabel(String.valueOf(strength));
        dexterityValueLabel = new JLabel(String.valueOf(dexterity));
        constitutionValueLabel = new JLabel(String.valueOf(constitution));
        intelligenceValueLabel = new JLabel(String.valueOf(intelligence));
        wisdomValueLabel = new JLabel(String.valueOf(wisdom));
        charismaValueLabel = new JLabel(String.valueOf(charisma));

        strengthModifierLabel = new JLabel(formatModifier(calculateModifier(strength)));
        dexterityModifierLabel = new JLabel(formatModifier(calculateModifier(dexterity)));
        constitutionModifierLabel = new JLabel(formatModifier(calculateModifier(constitution)));
        intelligenceModifierLabel = new JLabel(formatModifier(calculateModifier(intelligence)));
        wisdomModifierLabel = new JLabel(formatModifier(calculateModifier(wisdom)));
        charismaModifierLabel = new JLabel(formatModifier(calculateModifier(charisma)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        statsPanel.add(pointsRemainingLabel, gbc);

        gbc.gridwidth = 1;

        addStatRow(statsPanel, gbc, 1, "STR", strengthValueLabel, strengthModifierLabel);
        addStatRow(statsPanel, gbc, 2, "DEX", dexterityValueLabel, dexterityModifierLabel);
        addStatRow(statsPanel, gbc, 3, "CON", constitutionValueLabel, constitutionModifierLabel);
        addStatRow(statsPanel, gbc, 4, "INT", intelligenceValueLabel, intelligenceModifierLabel);
        addStatRow(statsPanel, gbc, 5, "WIS", wisdomValueLabel, wisdomModifierLabel);
        addStatRow(statsPanel, gbc, 6, "CHA", charismaValueLabel, charismaModifierLabel);

        panel.add(statsPanel, BorderLayout.NORTH);

        JButton randomizeButton = new JButton("Randomize Stats");
        randomizeButton.addActionListener(e -> randomizeStats());

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 5;
        gbc.insets = new Insets(20, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        statsPanel.add(randomizeButton, gbc);
    }

    private void addStatRow(JPanel panel, GridBagConstraints gbc, int row, String statName, JLabel valueLabel, JLabel modifierLabel) {
        JButton minusButton = new JButton("-");
        JButton plusButton = new JButton("+");

        minusButton.addActionListener(e -> decreaseStat(valueLabel));
        plusButton.addActionListener(e -> increaseStat(valueLabel));

        gbc.gridy = row;

        gbc.gridx = 0;
        panel.add(new JLabel(statName), gbc);

        gbc.gridx = 1;
        panel.add(minusButton, gbc);

        gbc.gridx = 2;
        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        valueLabel.setPreferredSize(new Dimension(30, 25));
        panel.add(valueLabel, gbc);

        gbc.gridx = 3;
        panel.add(plusButton, gbc);

        gbc.gridx = 4;
        modifierLabel.setPreferredSize(new Dimension(30, 25));
        panel.add(modifierLabel, gbc);
    }

    private void increaseStat(JLabel valueLabel) {
        int currentValue = Integer.parseInt(valueLabel.getText());

        if (pointsRemaining > 0 && currentValue < 18) {
            currentValue++;
            pointsRemaining--;
            valueLabel.setText(String.valueOf(currentValue));
            updatePointsRemainingLabel();
            updateModifierLabels();
        }
    }

    private void decreaseStat(JLabel valueLabel) {
        int currentValue = Integer.parseInt(valueLabel.getText());

        if (currentValue > 8) {
            currentValue--;
            pointsRemaining++;
            valueLabel.setText(String.valueOf(currentValue));
            updatePointsRemainingLabel();
            updateModifierLabels();
        }
    }

    private void updatePointsRemainingLabel() {
        pointsRemainingLabel.setText("Points Remaining: " + pointsRemaining);
    }

    private int calculateModifier(int statValue) {
        return Math.floorDiv(statValue - 10, 2);
    }

    private String formatModifier(int modifier) {
        if (modifier >= 0) {
            return "+" + modifier;
        }
        return String.valueOf(modifier);
    }

    private void updateModifierLabels() {
        strengthModifierLabel.setText(formatModifier(calculateModifier(Integer.parseInt(strengthValueLabel.getText()))));
        dexterityModifierLabel.setText(formatModifier(calculateModifier(Integer.parseInt(dexterityValueLabel.getText()))));
        constitutionModifierLabel.setText(formatModifier(calculateModifier(Integer.parseInt(constitutionValueLabel.getText()))));
        intelligenceModifierLabel.setText(formatModifier(calculateModifier(Integer.parseInt(intelligenceValueLabel.getText()))));
        wisdomModifierLabel.setText(formatModifier(calculateModifier(Integer.parseInt(wisdomValueLabel.getText()))));
        charismaModifierLabel.setText(formatModifier(calculateModifier(Integer.parseInt(charismaValueLabel.getText()))));
    }

    private void randomizeStats() {
        resetStats();

        while (pointsRemaining > 0) {
            int randomStat = (int)(Math.random() * 6);

            switch(randomStat) {
                case 0 -> increaseStat(strengthValueLabel);
                case 1 -> increaseStat(dexterityValueLabel);
                case 2 -> increaseStat(constitutionValueLabel);
                case 3 -> increaseStat(intelligenceValueLabel);
                case 4 -> increaseStat(wisdomValueLabel);
                case 5 -> increaseStat(charismaValueLabel);
            }
        }
    }

    private void resetStats() {
        pointsRemaining = 12;

        strengthValueLabel.setText("10");
        dexterityValueLabel.setText("10");
        constitutionValueLabel.setText("10");
        intelligenceValueLabel.setText("10");
        wisdomValueLabel.setText("10");
        charismaValueLabel.setText("10");

        updatePointsRemainingLabel();
        updateModifierLabels();
    }
}
