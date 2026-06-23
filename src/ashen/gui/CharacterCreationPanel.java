package ashen.gui;

import javax.swing.*;
import java.awt.*;
import ashen.model.GameCharacter;
import ashen.model.Stats;

/**
 * Screen used to create a new player character.
 * Handles race, class, weapon, armor, difficulty selection and stat allocation.
 */

public class CharacterCreationPanel extends JPanel {

    private MainFrame mainFrame;
    private JTextField nameField;
    private JComboBox<String> raceBox;
    private JComboBox<String> classBox;
    private JTextArea classDescriptionArea;
    private JComboBox<String> weaponBox;
    private JComboBox<String> armorBox;
    private JRadioButton normalDifficultyButton;
    private JRadioButton hardDifficultyButton;
    private JCheckBox hardcoreHpCheckBox;
    private JCheckBox hardcoreDamageCheckBox;

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

    /**
     * Creates the character creation panel.
     *
     * @param mainFrame main frame used for navigation after character creation
     */

    public CharacterCreationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        layoutComponents();
    }

    /**
     * Builds the character creation layout and initializes its controls.
     */

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

    /**
     * Creates a bordered section panel used by the character creation screen.
     *
     * @param title title displayed on the section border
     * @return configured section panel
     */

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), title));
        return panel;
    }

    /**
     * Adds name, race, class, equipment and difficulty controls to the options panel.
     *
     * @param panel panel that receives the controls
     */

    private void addCharacterOptions(JPanel panel) {
        JPanel formPanel = new JPanel(new GridBagLayout());

        nameField = new JTextField(15);
        raceBox = new JComboBox<>(new String[]{"Human", "Elf", "Dwarf", "Tiefling", "Dragonborn"});
        raceBonusLabel = new JLabel();
        raceBonusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        classBox = new JComboBox<>(new String[]{"Fighter", "Rogue", "Wizard", "Druid", "Ranger"});

        normalDifficultyButton = new JRadioButton("Normal", true);
        hardDifficultyButton = new JRadioButton("Hardcore");

        ButtonGroup difficultyGroup = new ButtonGroup();

        difficultyGroup.add(normalDifficultyButton);
        difficultyGroup.add(hardDifficultyButton);

        hardcoreHpCheckBox = new JCheckBox("+25% enemy HP");
        hardcoreDamageCheckBox = new JCheckBox("+25% enemy damage");

        hardcoreHpCheckBox.setEnabled(false);
        hardcoreDamageCheckBox.setEnabled(false);

        normalDifficultyButton.addActionListener(e -> updateHardcoreOptions());
        hardDifficultyButton.addActionListener(e -> updateHardcoreOptions());

        classDescriptionArea = new JTextArea(8, 24);
        classDescriptionArea.setEditable(false);
        classDescriptionArea.setLineWrap(true);
        classDescriptionArea.setWrapStyleWord(true);
        classDescriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        classDescriptionArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        classBox.addActionListener(e -> {
            updateClassEquipment();
            updateClassDescription();
        });

        weaponBox = new JComboBox<>();
        weaponBox.addActionListener(e -> {
            updateWeaponTooltip();
        });
        armorBox = new JComboBox<>();
        armorBox.addActionListener(e -> updateArmorTooltip());

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

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1;
        formPanel.add(new JScrollPane(classDescriptionArea), gbc);

        addFormRow(formPanel, gbc, 5, "Weapon:", weaponBox);
        addFormRow(formPanel, gbc, 6, "Armor:", armorBox);

        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        difficultyPanel.add(normalDifficultyButton);
        difficultyPanel.add(hardDifficultyButton);

        addFormRow(formPanel, gbc, 7, "Difficulty:", difficultyPanel);

        JPanel hardcoreOptionsPanel =
                new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        hardcoreOptionsPanel.add(hardcoreHpCheckBox);
        hardcoreOptionsPanel.add(hardcoreDamageCheckBox);

        addFormRow(formPanel, gbc, 8, "Hardcore Options:", hardcoreOptionsPanel);

        updateClassEquipment();
        updateClassDescription();
        updateWeaponTooltip();
        updateArmorTooltip();
        panel.add(formPanel, BorderLayout.NORTH);
    }

    /**
     * Adds a label and input component to a GridBagLayout form row.
     *
     * @param panel target panel
     * @param gbc layout constraints reused for the row
     * @param row row index
     * @param labelText text displayed by the row label
     * @param component input component added to the row
     */

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(component, gbc);
    }

    /**
     * Adds stat allocation controls to the stats panel.
     *
     * @param panel panel that receives stat controls
     */

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

        strengthModifierLabel = new JLabel(Stats.formatModifier(Stats.calculateModifier(strengthBase)));
        dexterityModifierLabel = new JLabel(Stats.formatModifier(Stats.calculateModifier(dexterityBase)));
        constitutionModifierLabel = new JLabel(Stats.formatModifier(Stats.calculateModifier(constitutionBase)));
        intelligenceModifierLabel = new JLabel(Stats.formatModifier(Stats.calculateModifier(intelligenceBase)));
        wisdomModifierLabel = new JLabel(Stats.formatModifier(Stats.calculateModifier(wisdomBase)));
        luckModifierLabel = new JLabel(Stats.formatModifier(Stats.calculateModifier(luckBase)));

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

    /**
     * Adds one ability score row with decrease and increase buttons.
     *
     * @param panel target panel
     * @param gbc layout constraints reused for the row
     * @param row row index
     * @param statName ability score name
     * @param valueLabel label that displays the ability score value
     * @param modifierLabel label that displays the ability modifier
     */

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

    /**
     * Increases a stat value when points are available.
     *
     * @param valueLabel label containing the stat value to increase
     */

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

    /**
     * Decreases a stat value and restores one available point.
     *
     * @param valueLabel label containing the stat value to decrease
     */

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

    /**
     * Recalculates all displayed stat modifiers.
     */

    private void updateModifierLabels() {
        strengthModifierLabel.setText(Stats.formatModifier(Stats.calculateModifier(Integer.parseInt(strengthValueLabel.getText()))));
        dexterityModifierLabel.setText(Stats.formatModifier(Stats.calculateModifier(Integer.parseInt(dexterityValueLabel.getText()))));
        constitutionModifierLabel.setText(Stats.formatModifier(Stats.calculateModifier(Integer.parseInt(constitutionValueLabel.getText()))));
        intelligenceModifierLabel.setText(Stats.formatModifier(Stats.calculateModifier(Integer.parseInt(intelligenceValueLabel.getText()))));
        wisdomModifierLabel.setText(Stats.formatModifier(Stats.calculateModifier(Integer.parseInt(wisdomValueLabel.getText()))));
        luckModifierLabel.setText(Stats.formatModifier(Stats.calculateModifier(Integer.parseInt(luckValueLabel.getText()))));
    }

    /**
     * Randomly distributes all available stat points among the ability scores.
     */

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

    /**
     * Resets all base stats and available points to their starting values.
     */

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

    /**
     * Updates displayed stat values and dependent modifier/color labels.
     */

    private void updateStatLabels() {
        strengthValueLabel.setText(String.valueOf(Math.min(20, strengthBase + strengthRaceBonus)));
        dexterityValueLabel.setText(String.valueOf(Math.min(20, dexterityBase + dexterityRaceBonus)));
        constitutionValueLabel.setText(String.valueOf(Math.min(20, constitutionBase + constitutionRaceBonus)));
        intelligenceValueLabel.setText(String.valueOf(Math.min(20, intelligenceBase + intelligenceRaceBonus)));
        wisdomValueLabel.setText(String.valueOf(Math.min(20, wisdomBase + wisdomRaceBonus)));
        luckValueLabel.setText(String.valueOf(Math.min(20, luckBase + luckRaceBonus)));

        updateStatColors();
    }

    /**
     * Applies race bonuses based on the selected race.
     */

    private void applyRaceBonuses() {
        resetRaceBonuses();

        String selectedRace = (String) raceBox.getSelectedItem();

        if ("Human".equals(selectedRace)) {
            strengthRaceBonus = 1;
            dexterityRaceBonus = 1;
            constitutionRaceBonus = 1;
            luckRaceBonus = 1;
            raceBonusLabel.setText("Race Bonus: +1 STR, +1 DEX, +1 CON, +1 LCK");
        } else if ("Elf".equals(selectedRace)) {
            dexterityRaceBonus = 2;
            constitutionRaceBonus = 1;
            luckRaceBonus = 1;
            raceBonusLabel.setText("Race Bonus: +2 DEX, +1 CON, +1 LCK");
        } else if ("Dwarf".equals(selectedRace)) {
            strengthRaceBonus = 1;
            constitutionRaceBonus = 1;
            wisdomRaceBonus = 2;
            raceBonusLabel.setText("Race Bonus: +1 STR, +1 CON, +2 WIS");
        } else if ("Tiefling".equals(selectedRace)) {
            intelligenceRaceBonus = 2;
            luckRaceBonus = 2;
            raceBonusLabel.setText("Race Bonus: +2 INT, +2 LCK");
        } else if ("Dragonborn".equals(selectedRace)) {
            strengthRaceBonus = 2;
            constitutionRaceBonus = 1;
            luckRaceBonus = 1;
            raceBonusLabel.setText("Race Bonus: +2 STR, +1 CON, +1 LCK");
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

    /**
     * Updates stat label colors to visually indicate race bonuses.
     */

    private void updateStatColors() {
        setStatColor(strengthValueLabel, strengthRaceBonus);
        setStatColor(dexterityValueLabel, dexterityRaceBonus);
        setStatColor(constitutionValueLabel, constitutionRaceBonus);
        setStatColor(intelligenceValueLabel, intelligenceRaceBonus);
        setStatColor(wisdomValueLabel, wisdomRaceBonus);
        setStatColor(luckValueLabel, luckRaceBonus);
    }

    /**
     * Sets a stat label color based on the size of its race bonus.
     *
     * @param label stat value label
     * @param bonus race bonus applied to the stat
     */

    private void setStatColor(JLabel label, int bonus) {
        if (bonus >= 2) {
            label.setForeground(strongBonusColor);
        } else if (bonus == 1) {
            label.setForeground(lightBonusColor);
        } else {
            label.setForeground(defaultStatColor);
        }
    }

    /**
     * Updates available weapons and armor based on the selected class.
     */

    private void updateClassEquipment() {
        String selectedClass = (String) classBox.getSelectedItem();

        weaponBox.removeAllItems();
        armorBox.removeAllItems();

        if ("Fighter".equals(selectedClass)) {
            addWeapons("Longsword + Shield", "Greatsword");
            addArmor("Chain Mail", "Plate Armor");
        } else if ("Rogue".equals(selectedClass)) {
            addWeapons("Scimitar + Dagger", "Dual Daggers");
            addArmor("Leather Armor", "Leather Tunic");
        } else if ("Wizard".equals(selectedClass)) {
            addWeapons("Rod + Spellbook");
            addArmor("Cloth Robe");
        } else if ("Druid".equals(selectedClass)) {
            addWeapons("Quarterstaff");
            addArmor("Leather Armor", "Hide Armor");
        } else if ("Ranger".equals(selectedClass)) {
            addWeapons("Longbow");
            addArmor("Leather Armor", "Leather Tunic");
        }

        updateWeaponTooltip();
        updateArmorTooltip();
    }

    /**
     * Adds weapon options to the weapon combo box.
     *
     * @param weapons weapon names to add
     */

    private void addWeapons(String... weapons) {
        for (String weapon : weapons) {
            weaponBox.addItem(weapon);
        }
    }

    /**
     * Adds armor options to the armor combo box.
     *
     * @param armorList armor names to add
     */

    private void addArmor(String... armorList) {
        for (String armor : armorList) {
            armorBox.addItem(armor);
        }
    }

    /**
     * Validates user input, applies final class/equipment bonuses and creates the player character.
     */

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

        if (!name.matches("[a-zA-Z0-9 _-]+")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Character name can only contain letters, numbers, spaces, underscores and hyphens.",
                    "Invalid Character Name",
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

        if (hardDifficultyButton.isSelected()
                && !hardcoreHpCheckBox.isSelected()
                && !hardcoreDamageCheckBox.isSelected()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Hardcore mode requires at least one hardcore option.",
                    "Invalid Difficulty",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int strength = Integer.parseInt(strengthValueLabel.getText());
        int dexterity = Integer.parseInt(dexterityValueLabel.getText());
        int constitution = Integer.parseInt(constitutionValueLabel.getText());
        int intelligence = Integer.parseInt(intelligenceValueLabel.getText());
        int wisdom = Integer.parseInt(wisdomValueLabel.getText());
        int luck = Integer.parseInt(luckValueLabel.getText());

        String selectedClass = (String) classBox.getSelectedItem();
        String selectedArmor = (String) armorBox.getSelectedItem();

        if ("Wizard".equals(selectedClass)) {
            intelligence += 2;
        }

        if ("Fighter".equals(selectedClass) && "Chain Mail".equals(selectedArmor)) {
            strength += 2;
        }

        if (("Rogue".equals(selectedClass) || "Ranger".equals(selectedClass))
                && "Leather Armor".equals(selectedArmor)) {
            dexterity += 1;
        }

        if (("Rogue".equals(selectedClass) || "Ranger".equals(selectedClass))
                && "Leather Tunic".equals(selectedArmor)) {
            dexterity += 2;
        }

        if ("Druid".equals(selectedClass) && "Leather Armor".equals(selectedArmor)) {
            wisdom += 1;
        }

        Stats stats = new Stats(
                strength,
                dexterity,
                constitution,
                intelligence,
                wisdom,
                luck
        );

        String difficulty =
                hardDifficultyButton.isSelected()
                        ? "Hardcore"
                        : "Normal";

        boolean hardcoreHpBonus =
                hardDifficultyButton.isSelected()
                        && hardcoreHpCheckBox.isSelected();

        boolean hardcoreDamageBonus =
                hardDifficultyButton.isSelected()
                        && hardcoreDamageCheckBox.isSelected();

        GameCharacter character = new GameCharacter(
                name,
                (String) raceBox.getSelectedItem(),
                (String) classBox.getSelectedItem(),
                stats,
                (String) weaponBox.getSelectedItem(),
                (String) armorBox.getSelectedItem(),
                difficulty,
                hardcoreHpBonus,
                hardcoreDamageBonus
        );

        mainFrame.showBattle(character);
    }

    /**
     * Updates the class description text for the selected class.
     */

    private void updateClassDescription() {
        String selectedClass = (String) classBox.getSelectedItem();

        if ("Fighter".equals(selectedClass)) {
            classDescriptionArea.setText(
                    "Primary Stat: Strength\n" +
                            "Recommended Stats: STR, CON\n\n" +
                            "Class Ability: Weapon Mastery\n" +
                            "Effect: Can choose between defensive and offensive weapon setups."
            );
        } else if ("Rogue".equals(selectedClass)) {
            classDescriptionArea.setText(
                    "Primary Stat: Dexterity\n" +
                            "Recommended Stats: DEX, CON\n\n" +
                            "Class Ability: Sneak Attack\n" +
                            "Effect: First attack each battle gains +1d8 damage."
            );
        } else if ("Wizard".equals(selectedClass)) {
            classDescriptionArea.setText(
                    "Primary Stat: Intelligence\n" +
                            "Recommended Stats: INT, CON\n\n" +
                            "Class Ability: Arcane Precision\n" +
                            "Effect: Reroll attack rolls of 5 or lower.\n\n" +
                            "Equipment Bonus: +2 INT from Spellbook."
            );
        } else if ("Druid".equals(selectedClass)) {
            classDescriptionArea.setText(
                    "Primary Stat: Wisdom\n" +
                            "Recommended Stats: WIS, CON\n\n" +
                            "Class Ability: Mark of the Wild\n" +
                            "Effect: Gain +2 Armor Class."
            );
        } else if ("Ranger".equals(selectedClass)) {
            classDescriptionArea.setText(
                    "Primary Stat: Dexterity\n" +
                            "Recommended Stats: DEX, CON\n\n" +
                            "Class Ability: Poison Arrows\n" +
                            "Effect: Successful hits add stacking poison damage.\n" +
                            "Poison triggers even if future attacks miss."
            );
        }
    }

    /**
     * Updates the weapon tooltip for the currently selected weapon.
     */

    private void updateWeaponTooltip() {
        String selectedWeapon = (String) weaponBox.getSelectedItem();

        if ("Longsword + Shield".equals(selectedWeapon)) {
            weaponBox.setToolTipText("Damage: 1d8 + STR | Shield: +2 AC");
        } else if ("Greatsword".equals(selectedWeapon)) {
            weaponBox.setToolTipText("Damage: 1d12 + STR | Two-handed | No shield");
        } else if ("Scimitar + Dagger".equals(selectedWeapon)) {
            weaponBox.setToolTipText("Damage: 1d6 + DEX + 1d4 off-hand");
        } else if ("Dual Daggers".equals(selectedWeapon)) {
            weaponBox.setToolTipText("Damage: 1d4 + DEX + 1d4 off-hand | +2 Attack Bonus");
        } else if ("Rod + Spellbook".equals(selectedWeapon)) {
            weaponBox.setToolTipText("Fireball: 1d12 + INT | Spellbook: +2 INT");
        } else if ("Quarterstaff".equals(selectedWeapon)) {
            weaponBox.setToolTipText("Damage: 1d10 + WIS | Two-handed");
        } else if ("Longbow".equals(selectedWeapon)) {
            weaponBox.setToolTipText("Damage: 1d10 + DEX | Poison Arrows");
        } else {
            weaponBox.setToolTipText(null);
        }
    }

    /**
     * Updates the armor tooltip for the currently selected armor.
     */

    private void updateArmorTooltip() {
        String selectedArmor = (String) armorBox.getSelectedItem();

        if ("Cloth Robe".equals(selectedArmor)) {
            armorBox.setToolTipText("AC: 10 | +2 INT");
        } else if ("Leather Armor".equals(selectedArmor)) {
            armorBox.setToolTipText("AC: 14 | +1 main stat");
        } else if ("Leather Tunic".equals(selectedArmor)) {
            armorBox.setToolTipText("AC: 12 | +2 main stat");
        } else if ("Hide Armor".equals(selectedArmor)) {
            armorBox.setToolTipText("AC: 16");
        } else if ("Chain Mail".equals(selectedArmor)) {
            armorBox.setToolTipText("AC: 15 | +2 STR");
        } else if ("Plate Armor".equals(selectedArmor)) {
            armorBox.setToolTipText("AC: 17");
        } else {
            armorBox.setToolTipText(null);
        }
    }

    /**
     * Enables or disables hardcore option check boxes based on selected difficulty.
     */

    private void updateHardcoreOptions() {
        boolean hardcore = hardDifficultyButton.isSelected();

        hardcoreHpCheckBox.setEnabled(hardcore);
        hardcoreDamageCheckBox.setEnabled(hardcore);

        if (!hardcore) {
            hardcoreHpCheckBox.setSelected(false);
            hardcoreDamageCheckBox.setSelected(false);
        }
    }
}
