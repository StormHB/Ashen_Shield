package ashen.gui;

import ashen.creation.CharacterCreationRules;
import ashen.gui.event.CharacterCreationListener;
import ashen.model.Armor;
import ashen.model.CharacterClass;
import ashen.model.Difficulty;
import ashen.model.GameCharacter;
import ashen.model.Race;
import ashen.model.Stats;
import ashen.model.Weapon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Screen used to create a new player character.
 * Handles race, class, weapon, armor, difficulty selection and stat allocation.
 */

public class CharacterCreationPanel extends JPanel {

    private final CharacterCreationListener listener;
    private JTextField nameField;
    private JComboBox<Race> raceBox;
    private JComboBox<CharacterClass> classBox;
    private JTextArea classDescriptionArea;
    private JComboBox<Weapon> weaponBox;
    private JComboBox<Armor> armorBox;
    private JRadioButton normalDifficultyButton;
    private JRadioButton hardDifficultyButton;
    private JCheckBox hardcoreHpCheckBox;
    private JCheckBox hardcoreDamageCheckBox;

    private final StatAllocationPanel statAllocationPanel;
    private JLabel raceBonusLabel;

    /**
     * Creates the character creation panel.
     *
     * @param listener listener used for navigation after character creation
     */

    public CharacterCreationPanel(CharacterCreationListener listener) {
        this.listener = listener;
        this.statAllocationPanel = new StatAllocationPanel();
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
        leftPanel.add(statAllocationPanel, BorderLayout.NORTH);
        JPanel rightPanel = createSectionPanel("Character Options");
        addCharacterOptions(rightPanel);
        applyRaceBonuses();

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onCharacterCreationCancelled();
            }
        });
        JButton createButton = new JButton("Create Character");
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCharacter();
            }
        });

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
        raceBox = new JComboBox<>(CharacterCreationRules.getRaces());
        raceBonusLabel = new JLabel();
        raceBonusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        classBox = new JComboBox<>(CharacterCreationRules.getCharacterClasses());

        normalDifficultyButton = new JRadioButton(Difficulty.NORMAL.getDisplayName(), true);
        hardDifficultyButton = new JRadioButton(Difficulty.HARDCORE.getDisplayName());

        ButtonGroup difficultyGroup = new ButtonGroup();

        difficultyGroup.add(normalDifficultyButton);
        difficultyGroup.add(hardDifficultyButton);

        hardcoreHpCheckBox = new JCheckBox("+25% enemy HP");
        hardcoreDamageCheckBox = new JCheckBox("+25% enemy damage");

        hardcoreHpCheckBox.setEnabled(false);
        hardcoreDamageCheckBox.setEnabled(false);

        normalDifficultyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateHardcoreOptions();
            }
        });
        hardDifficultyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateHardcoreOptions();
            }
        });

        classDescriptionArea = new JTextArea(8, 24);
        classDescriptionArea.setEditable(false);
        classDescriptionArea.setLineWrap(true);
        classDescriptionArea.setWrapStyleWord(true);
        classDescriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        classDescriptionArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        classBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClassEquipment();
                updateClassDescription();
            }
        });

        weaponBox = new JComboBox<>();
        weaponBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateWeaponTooltip();
            }
        });
        armorBox = new JComboBox<>();
        armorBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateArmorTooltip();
            }
        });

        raceBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyRaceBonuses();
            }
        });

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
     * Applies race bonuses based on the selected race.
     */

    private void applyRaceBonuses() {
        Race selectedRace = (Race) raceBox.getSelectedItem();
        Stats raceBonuses = CharacterCreationRules.getRaceBonuses(selectedRace);

        statAllocationPanel.applyRaceBonuses(raceBonuses);
        raceBonusLabel.setText("Race Bonus: " + CharacterCreationRules.getRaceBonusDescription(selectedRace));
    }

    /**
     * Updates available weapons and armor based on the selected class.
     */

    private void updateClassEquipment() {
        CharacterClass selectedClass = (CharacterClass) classBox.getSelectedItem();

        weaponBox.removeAllItems();
        armorBox.removeAllItems();

        addWeapons(CharacterCreationRules.getWeaponsForClass(selectedClass));
        addArmor(CharacterCreationRules.getArmorForClass(selectedClass));

        updateWeaponTooltip();
        updateArmorTooltip();
    }

    /**
     * Adds weapon options to the weapon combo box.
     *
     * @param weapons weapon names to add
     */

    private void addWeapons(Weapon... weapons) {
        for (Weapon weapon : weapons) {
            weaponBox.addItem(weapon);
        }
    }

    /**
     * Adds armor options to the armor combo box.
     *
     * @param armorList armor names to add
     */

    private void addArmor(Armor... armorList) {
        for (Armor armor : armorList) {
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

        if (statAllocationPanel.getPointsRemaining() > 0) {
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

        CharacterClass selectedClass = (CharacterClass) classBox.getSelectedItem();
        Armor selectedArmor = (Armor) armorBox.getSelectedItem();

        Stats stats = CharacterCreationRules.applyEquipmentBonuses(
                selectedClass,
                selectedArmor,
                statAllocationPanel.toStats()
        );

        Difficulty difficulty =
                hardDifficultyButton.isSelected()
                        ? Difficulty.HARDCORE
                        : Difficulty.NORMAL;

        boolean hardcoreHpBonus =
                hardDifficultyButton.isSelected()
                        && hardcoreHpCheckBox.isSelected();

        boolean hardcoreDamageBonus =
                hardDifficultyButton.isSelected()
                        && hardcoreDamageCheckBox.isSelected();

        GameCharacter character = new GameCharacter(
                name,
                (Race) raceBox.getSelectedItem(),
                (CharacterClass) classBox.getSelectedItem(),
                stats,
                (Weapon) weaponBox.getSelectedItem(),
                (Armor) armorBox.getSelectedItem(),
                difficulty,
                hardcoreHpBonus,
                hardcoreDamageBonus
        );

        listener.onCharacterCreated(character);
    }

    /**
     * Updates the class description text for the selected class.
     */

    private void updateClassDescription() {
        CharacterClass selectedClass = (CharacterClass) classBox.getSelectedItem();

        classDescriptionArea.setText(CharacterCreationRules.getClassDescription(selectedClass));
    }

    /**
     * Updates the weapon tooltip for the currently selected weapon.
     */

    private void updateWeaponTooltip() {
        Weapon selectedWeapon = (Weapon) weaponBox.getSelectedItem();

        weaponBox.setToolTipText(CharacterCreationRules.getWeaponTooltip(selectedWeapon));
    }

    /**
     * Updates the armor tooltip for the currently selected armor.
     */

    private void updateArmorTooltip() {
        Armor selectedArmor = (Armor) armorBox.getSelectedItem();

        armorBox.setToolTipText(CharacterCreationRules.getArmorTooltip(selectedArmor));
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
