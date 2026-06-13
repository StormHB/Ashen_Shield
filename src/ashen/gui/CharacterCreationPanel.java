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
}
