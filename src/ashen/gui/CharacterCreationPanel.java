package ashen.gui;

import javax.swing.*;
import java.awt.*;
import ashen.model.GameCharacter;
import ashen.model.Stats;

public class CharacterCreationPanel extends JPanel {

    private MainFrame mainFrame;
    private JTextField nameField;
    private JComboBox<String> raceBox;
    private JComboBox<String> classBox;
    private JComboBox<String> weaponBox;
    private JComboBox<String> armorBox;

    private JLabel pointsRemainingLabel;
    private int pointsRemaining = 12;

    private int strengthBase = 10;
    private int dexterityBase = 10;
    private int constitutionBase = 10;
    private int intelligenceBase = 10;
    private int wisdomBase = 10;
    private int luckBase = 10;

    private JLabel strengthValueLabel;
    private JLabel dexterityValueLabel;
    private JLabel constitutionValueLabel;
    private JLabel intelligenceValueLabel;
    private JLabel wisdomValueLabel;
    private JLabel luckValueLabel;

    private JLabel strengthModifierLabel;
    private JLabel dexterityModifierLabel;
    private JLabel constitutionModifierLabel;
    private JLabel intelligenceModifierLabel;
    private JLabel wisdomModifierLabel;
    private JLabel luckModifierLabel;

    private int strengthRaceBonus;
    private int dexterityRaceBonus;
    private int constitutionRaceBonus;
    private int intelligenceRaceBonus;
    private int wisdomRaceBonus;
    private int luckRaceBonus;

    private JLabel raceBonusLabel;

    private final Color defaultStatColor = Color.BLACK;
    private final Color lightBonusColor = new Color(180, 140, 0);
    private final Color strongBonusColor = new Color(0, 180, 0);

    private JCheckBox shieldCheckBox;

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
        applyRaceBonuses();

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> mainFrame.showMainMenu());
        JButton createButton = new JButton("Create Character");
        createButton.addActionListener(e -> createCharacter());

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
        raceBonusLabel = new JLabel();
        raceBonusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        classBox = new JComboBox<>(new String[]{"Fighter", "Rogue", "Wizard", "Druid", "Ranger"});
        classBox.addActionListener(e -> updateClassEquipment());
        weaponBox = new JComboBox<>();
        weaponBox.addActionListener(e -> updateShieldAvailability());
        armorBox = new JComboBox<>();
        shieldCheckBox = new JCheckBox("Use Shield");

        raceBox.addActionListener(e -> applyRaceBonuses());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(formPanel, gbc, 0, "Name:", nameField);
        addFormRow(formPanel, gbc, 1, "Race:", raceBox);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        formPanel.add(raceBonusLabel, gbc);

        addFormRow(formPanel, gbc, 3, "Class:", classBox);
        addFormRow(formPanel, gbc, 4, "Weapon:", weaponBox);
        addFormRow(formPanel, gbc, 5, "Armor:", armorBox);
        addFormRow(formPanel, gbc, 6, "Shield:", shieldCheckBox);

        updateClassEquipment();
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

        strengthValueLabel = new JLabel(String.valueOf(strengthBase));
        dexterityValueLabel = new JLabel(String.valueOf(dexterityBase));
        constitutionValueLabel = new JLabel(String.valueOf(constitutionBase));
        intelligenceValueLabel = new JLabel(String.valueOf(intelligenceBase));
        wisdomValueLabel = new JLabel(String.valueOf(wisdomBase));
        luckValueLabel = new JLabel(String.valueOf(luckBase));

        strengthModifierLabel = new JLabel(formatModifier(calculateModifier(strengthBase)));
        dexterityModifierLabel = new JLabel(formatModifier(calculateModifier(dexterityBase)));
        constitutionModifierLabel = new JLabel(formatModifier(calculateModifier(constitutionBase)));
        intelligenceModifierLabel = new JLabel(formatModifier(calculateModifier(intelligenceBase)));
        wisdomModifierLabel = new JLabel(formatModifier(calculateModifier(wisdomBase)));
        luckModifierLabel = new JLabel(formatModifier(calculateModifier(luckBase)));

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
        addStatRow(statsPanel, gbc, 6, "LCK", luckValueLabel, luckModifierLabel);

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
        if (pointsRemaining <= 0) {
            return;
        }

        if (valueLabel == strengthValueLabel && strengthBase < 18) {
            strengthBase++;
            pointsRemaining--;
        } else if (valueLabel == dexterityValueLabel && dexterityBase < 18) {
            dexterityBase++;
            pointsRemaining--;
        } else if (valueLabel == constitutionValueLabel && constitutionBase < 18) {
            constitutionBase++;
            pointsRemaining--;
        } else if (valueLabel == intelligenceValueLabel && intelligenceBase < 18) {
            intelligenceBase++;
            pointsRemaining--;
        } else if (valueLabel == wisdomValueLabel && wisdomBase < 18) {
            wisdomBase++;
            pointsRemaining--;
        } else if (valueLabel == luckValueLabel && luckBase < 18) {
            luckBase++;
            pointsRemaining--;
        }

        updateStatLabels();
        updatePointsRemainingLabel();
        updateModifierLabels();
    }

    private void decreaseStat(JLabel valueLabel) {
        if (valueLabel == strengthValueLabel && strengthBase > 8) {
            strengthBase--;
            pointsRemaining++;
        } else if (valueLabel == dexterityValueLabel && dexterityBase > 8) {
            dexterityBase--;
            pointsRemaining++;
        } else if (valueLabel == constitutionValueLabel && constitutionBase > 8) {
            constitutionBase--;
            pointsRemaining++;
        } else if (valueLabel == intelligenceValueLabel && intelligenceBase > 8) {
            intelligenceBase--;
            pointsRemaining++;
        } else if (valueLabel == wisdomValueLabel && wisdomBase > 8) {
            wisdomBase--;
            pointsRemaining++;
        } else if (valueLabel == luckValueLabel && luckBase > 8) {
            luckBase--;
            pointsRemaining++;
        }

        updateStatLabels();
        updatePointsRemainingLabel();
        updateModifierLabels();
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
        luckModifierLabel.setText(formatModifier(calculateModifier(Integer.parseInt(luckValueLabel.getText()))));
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
                case 5 -> increaseStat(luckValueLabel);
            }
        }
    }

    private void resetStats() {
        pointsRemaining = 12;

        strengthBase = 10;
        dexterityBase = 10;
        constitutionBase = 10;
        intelligenceBase = 10;
        wisdomBase = 10;
        luckBase = 10;

        updateStatLabels();
        updatePointsRemainingLabel();
        updateModifierLabels();
    }

    private void updateStatLabels() {
        strengthValueLabel.setText(String.valueOf(Math.min(20, strengthBase + strengthRaceBonus)));
        dexterityValueLabel.setText(String.valueOf(Math.min(20, dexterityBase + dexterityRaceBonus)));
        constitutionValueLabel.setText(String.valueOf(Math.min(20, constitutionBase + constitutionRaceBonus)));
        intelligenceValueLabel.setText(String.valueOf(Math.min(20, intelligenceBase + intelligenceRaceBonus)));
        wisdomValueLabel.setText(String.valueOf(Math.min(20, wisdomBase + wisdomRaceBonus)));
        luckValueLabel.setText(String.valueOf(Math.min(20, luckBase + luckRaceBonus)));

        updateStatColors();
    }

    private void applyRaceBonuses() {
        resetRaceBonuses();

        String selectedRace = (String) raceBox.getSelectedItem();

        if ("Human".equals(selectedRace)) {
            strengthRaceBonus = 1;
            constitutionRaceBonus = 1;
            luckRaceBonus = 1;
            raceBonusLabel.setText("Race Bonus: +1 STR, +1 CON, +1 LCK");
        } else if ("Elf".equals(selectedRace)) {
            dexterityRaceBonus = 2;
            intelligenceRaceBonus = 1;
            raceBonusLabel.setText("Race Bonus: +2 DEX, +1 INT");
        } else if ("Dwarf".equals(selectedRace)) {
            constitutionRaceBonus = 2;
            wisdomRaceBonus = 1;
            raceBonusLabel.setText("Race Bonus: +2 CON, +1 WIS");
        } else if ("Half-Orc".equals(selectedRace)) {
            strengthRaceBonus = 2;
            constitutionRaceBonus = 1;
            raceBonusLabel.setText("Race Bonus: +2 STR, +1 CON");
        } else if ("Dragonborn".equals(selectedRace)) {
            strengthRaceBonus = 2;
            luckRaceBonus = 1;
            raceBonusLabel.setText("Race Bonus: +2 STR, +1 LCK");
        }

        updateStatLabels();
        updateModifierLabels();
    }

    private void resetRaceBonuses() {
        strengthRaceBonus = 0;
        dexterityRaceBonus = 0;
        constitutionRaceBonus = 0;
        intelligenceRaceBonus = 0;
        wisdomRaceBonus = 0;
        luckRaceBonus = 0;
    }

    private void updateStatColors() {
        setStatColor(strengthValueLabel, strengthRaceBonus);
        setStatColor(dexterityValueLabel, dexterityRaceBonus);
        setStatColor(constitutionValueLabel, constitutionRaceBonus);
        setStatColor(intelligenceValueLabel, intelligenceRaceBonus);
        setStatColor(wisdomValueLabel, wisdomRaceBonus);
        setStatColor(luckValueLabel, luckRaceBonus);
    }

    private void setStatColor(JLabel label, int bonus) {
        if (bonus >= 2) {
            label.setForeground(strongBonusColor);
        } else if (bonus == 1) {
            label.setForeground(lightBonusColor);
        } else {
            label.setForeground(defaultStatColor);
        }
    }

    private void updateClassEquipment() {
        String selectedClass = (String) classBox.getSelectedItem();

        weaponBox.removeAllItems();
        armorBox.removeAllItems();

        if ("Fighter".equals(selectedClass)) {
            addWeapons("Longsword");
            addArmor("Chain Mail", "Plate Armor");
        } else if ("Rogue".equals(selectedClass)) {
            addWeapons("Dagger", "Scimitar", "Shortbow");
            addArmor("Leather Armor");
        } else if ("Wizard".equals(selectedClass)) {
            addWeapons("Quarterstaff");
            addArmor("Cloth Robe");
        } else if ("Druid".equals(selectedClass)) {
            addWeapons("Quarterstaff", "Scimitar");
            addArmor("Leather Armor", "Hide Armor");
        } else if ("Ranger".equals(selectedClass)) {
            addWeapons("Longbow", "Shortbow", "Dagger");
            addArmor("Leather Armor", "Hide Armor");
        }

        updateShieldAvailability();
    }

    private void addWeapons(String... weapons) {
        for (String weapon : weapons) {
            weaponBox.addItem(weapon);
        }
    }

    private void addArmor(String... armorList) {
        for (String armor : armorList) {
            armorBox.addItem(armor);
        }
    }

    private void updateShieldAvailability() {
        String selectedClass = (String) classBox.getSelectedItem();
        String selectedWeapon = (String) weaponBox.getSelectedItem();

        boolean fighterSelected = "Fighter".equals(selectedClass);
        boolean oneHandedWeapon = "Longsword".equals(selectedWeapon);

        shieldCheckBox.setEnabled(fighterSelected && oneHandedWeapon);

        if (!shieldCheckBox.isEnabled()) {
            shieldCheckBox.setSelected(false);
        }
    }

    private void createCharacter() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Character name cannot be empty.",
                    "Invalid Character",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (pointsRemaining > 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "You must spend all stat points before creating a character.",
                    "Invalid Character",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Stats stats = new Stats(
                Integer.parseInt(strengthValueLabel.getText()),
                Integer.parseInt(dexterityValueLabel.getText()),
                Integer.parseInt(constitutionValueLabel.getText()),
                Integer.parseInt(intelligenceValueLabel.getText()),
                Integer.parseInt(wisdomValueLabel.getText()),
                Integer.parseInt(luckValueLabel.getText())
        );

        GameCharacter character = new GameCharacter(
                name,
                (String) raceBox.getSelectedItem(),
                (String) classBox.getSelectedItem(),
                stats,
                (String) weaponBox.getSelectedItem(),
                (String) armorBox.getSelectedItem(),
                shieldCheckBox.isSelected()
        );

        mainFrame.showBattle(character);
    }
}
