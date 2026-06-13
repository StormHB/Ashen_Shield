package ashen.gui;

import ashen.model.Enemy;
import ashen.model.GameCharacter;
import ashen.service.SaveLoadService;

import java.io.File;

import javax.swing.*;
import java.awt.*;

public class BattlePanel extends JPanel {

    private GameCharacter character;
    private Enemy enemy;

    private int currentEnemyIndex = 0;
    private Enemy[] enemies;

    private JLabel playerHpLabel;
    private JLabel playerAcLabel;
    private JLabel enemyHpLabel;
    private JLabel enemyAcLabel;

    private JTextArea battleLogArea;
    private JButton attackButton;

    private SaveLoadService saveLoadService;

    private MainFrame mainFrame;

    public BattlePanel(MainFrame mainFrame, GameCharacter character, int enemyIndex) {
        this.mainFrame = mainFrame;
        this.character = character;
        this.saveLoadService = new SaveLoadService();

        createEnemies();
        this.currentEnemyIndex = enemyIndex;
        this.enemy = enemies[currentEnemyIndex];

        layoutComponents();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel title = new JLabel("Battle Screen", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        add(title, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        mainPanel.add(createPlayerPanel());
        mainPanel.add(createEnemyPanel());

        add(mainPanel, BorderLayout.NORTH);

        battleLogArea = new JTextArea();
        battleLogArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        battleLogArea.setEditable(false);
        battleLogArea.setLineWrap(true);
        battleLogArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(battleLogArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Battle Log"
        ));

        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));

        attackButton = new JButton("Attack");
        attackButton.addActionListener(e -> handleAttack());
        attackButton.setPreferredSize(new Dimension(120, 40));

        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.addActionListener(e -> saveCharacter());

        JButton saveAsButton = new JButton("Save As");
        saveAsButton.setPreferredSize(new Dimension(120, 40));
        saveAsButton.addActionListener(e -> saveCharacterAs());

        JButton characterSheetButton = new JButton("Character Sheet");
        characterSheetButton.setPreferredSize(new Dimension(160, 40));
        characterSheetButton.addActionListener(e -> showCharacterSheet());

        actionPanel.add(attackButton);
        actionPanel.add(saveButton);
        actionPanel.add(saveAsButton);
        actionPanel.add(characterSheetButton);

        add(actionPanel, BorderLayout.SOUTH);

        battleLogArea.append("Battle " + (currentEnemyIndex + 1) + "/" + enemies.length + " started!\n");
        battleLogArea.append(character.getName() + " encounters " + enemy.getName() + ".\n\n");
    }

    private JPanel createPlayerPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Player"
        ));

        playerHpLabel = new JLabel("HP: " + character.getCurrentHp() + "/" + character.getMaxHp());
        playerAcLabel = new JLabel("AC: " + calculateAC());

        panel.add(new JLabel("Name: " + character.getName()));
        panel.add(new JLabel("Race: " + character.getRace()));
        panel.add(new JLabel("Class: " + character.getCharacterClass()));
        panel.add(playerHpLabel);
        panel.add(playerAcLabel);
        panel.add(new JLabel("Attack Bonus: " + formatModifier(calculateAttackBonus())));
        panel.add(new JLabel("Weapon: " + character.getWeapon()));
        panel.add(new JLabel("Armor: " + character.getArmor()));
        panel.add(new JLabel("Shield: " + (character.hasShield() ? "Yes" : "No")));

        return panel;
    }

    private JPanel createEnemyPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Enemy"
        ));

        enemyHpLabel = new JLabel("HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp());
        enemyAcLabel = new JLabel("AC: " + enemy.getArmorClass());

        panel.add(new JLabel("Name: " + enemy.getName()));
        panel.add(enemyHpLabel);
        panel.add(enemyAcLabel);
        panel.add(new JLabel("Attack Bonus: " + formatModifier(enemy.getAttackBonus())));

        return panel;
    }

    private void handleAttack() {
        int d20Roll = rollDice(20);
        int attackBonus = calculateAttackBonus();
        int totalAttack = d20Roll + attackBonus;

        battleLogArea.append(character.getName() + " attacks " + enemy.getName() + ".\n");

        if (d20Roll == 1) {
            int selfDamage = rollDice(4);

            damagePlayer(selfDamage);

            battleLogArea.append("Natural 1! Critical Miss!\n");
            battleLogArea.append(character.getName() + " takes " + selfDamage + " self-damage.\n");
            battleLogArea.append(character.getName() + " HP: " + character.getCurrentHp() + "/" + character.getMaxHp() + "\n\n");

            checkPlayerDefeated();

            if (!character.isDefeated()) {
                enemyTurn();
            }

            return;
        }

        if (d20Roll == 20) {
            int weaponDice = getWeaponDamageDice();
            int firstRoll = rollDice(weaponDice);
            int secondRoll = rollDice(weaponDice);
            int damageModifier = calculateAbilityModifierForAttack();

            int damage = firstRoll + secondRoll + damageModifier;

            if (damage < 1) {
                damage = 1;
            }

            enemy.takeDamage(damage);
            updateEnemyHpLabel();

            battleLogArea.append("Natural 20! Critical Hit!\n");
            battleLogArea.append(
                    "Critical Damage Roll: "
                            + firstRoll
                            + " + "
                            + secondRoll
                            + " + "
                            + damageModifier
                            + " = "
                            + damage
                            + "\n"
            );
            battleLogArea.append(enemy.getName() + " HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + "\n");

            checkEnemyDefeated();

            if (!enemy.isDefeated()) {
                battleLogArea.append("\n");
                enemyTurn();
            } else {
                battleLogArea.append("\n");
            }

            return;
        }

        battleLogArea.append("Attack Roll: " + d20Roll + " + " + attackBonus + " = " + totalAttack + "\n");

        if (totalAttack >= enemy.getArmorClass()) {
            int weaponDice = getWeaponDamageDice();
            int damageRoll = rollDice(weaponDice);
            int damageModifier = calculateAbilityModifierForAttack();

            int damage = damageRoll + damageModifier;

            if (damage < 1) {
                damage = 1;
            }

            enemy.takeDamage(damage);
            updateEnemyHpLabel();

            battleLogArea.append("Hit!\n");
            battleLogArea.append(
                    "Damage Roll: "
                            + damageRoll
                            + " + "
                            + damageModifier
                            + " = "
                            + damage
                            + "\n"
            );
            battleLogArea.append(enemy.getName() + " HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + "\n");

            checkEnemyDefeated();

            if (!enemy.isDefeated()) {
                battleLogArea.append("\n");
                enemyTurn();
            } else {
                battleLogArea.append("\n");
            }
        } else {
            battleLogArea.append("Miss!\n\n");
            enemyTurn();
        }
    }

    private void updateEnemyHpLabel() {
        enemyHpLabel.setText("HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp());
    }

    private void checkEnemyDefeated() {
        if (enemy.isDefeated()) {
            battleLogArea.append(enemy.getName() + " has been defeated!\n");
            attackButton.setEnabled(false);
            mainFrame.showVictoryPanel(character, currentEnemyIndex, enemy.getName());
        }
    }

    private int calculateModifier(int statValue) {
        return Math.floorDiv(statValue - 10, 2);
    }

    private int calculateHP() {
        return 10 + character.getStats().getConstitution();
    }

    private int calculateAbilityModifierForAttack() {
        String characterClass = character.getCharacterClass();

        if ("Fighter".equals(characterClass)) {
            return calculateModifier(character.getStats().getStrength());
        }

        if ("Rogue".equals(characterClass) || "Ranger".equals(characterClass)) {
            return calculateModifier(character.getStats().getDexterity());
        }

        if ("Wizard".equals(characterClass)) {
            return calculateModifier(character.getStats().getIntelligence());
        }

        if ("Druid".equals(characterClass)) {
            return calculateModifier(character.getStats().getWisdom());
        }

        return 0;
    }

    private int calculateAttackBonus() {
        int proficiencyBonus = 2;
        return proficiencyBonus + calculateAbilityModifierForAttack();
    }

    private int calculateAC() {
        int dexModifier = calculateModifier(character.getStats().getDexterity());
        int ac;

        switch (character.getArmor()) {
            case "Cloth Robe":
                ac = 10 + dexModifier;
                break;
            case "Leather Armor":
                ac = 11 + dexModifier;
                break;
            case "Hide Armor":
                ac = 12 + Math.min(2, dexModifier);
                break;
            case "Chain Mail":
                ac = 16;
                break;
            case "Plate Armor":
                ac = 18;
                break;
            default:
                ac = 10;
        }

        if (character.hasShield()) {
            ac += 2;
        }

        if ("Druid".equals(character.getCharacterClass())
                && "Quarterstaff".equals(character.getWeapon())) {
            ac += 1;
        }

        return ac;
    }

    private int rollDice(int sides) {
        return (int) (Math.random() * sides) + 1;
    }

    private int getWeaponDamageDice() {
        switch (character.getWeapon()) {
            case "Longsword":
                return 8;
            case "Dagger":
                return 4;
            case "Scimitar":
            case "Quarterstaff":
            case "Shortbow":
                return 6;
            case "Longbow":
                return 8;
            default:
                return 4;
        }
    }

    private int calculateDamage() {
        int damageRoll = rollDice(getWeaponDamageDice());
        int damageModifier = calculateAbilityModifierForAttack();

        int damage = damageRoll + damageModifier;

        if (damage < 1) {
            damage = 1;
        }

        return damage;
    }

    private int calculateCriticalDamage() {
        int weaponDice = getWeaponDamageDice();

        int firstRoll = rollDice(weaponDice);
        int secondRoll = rollDice(weaponDice);
        int damageModifier = calculateAbilityModifierForAttack();

        int damage = firstRoll + secondRoll + damageModifier;

        if (damage < 1) {
            damage = 1;
        }

        return damage;
    }

    private String formatModifier(int modifier) {
        if (modifier >= 0) {
            return "+" + modifier;
        }

        return String.valueOf(modifier);
    }

    private void enemyTurn() {
        int d20Roll = rollDice(20);
        int attackBonus = enemy.getAttackBonus();
        int totalAttack = d20Roll + attackBonus;

        battleLogArea.append(enemy.getName() + " attacks " + character.getName() + ".\n");
        battleLogArea.append("Attack Roll: " + d20Roll + " + " + attackBonus + " = " + totalAttack + "\n");

        if (d20Roll == 1) {
            int selfDamage = rollDice(4);

            enemy.takeDamage(selfDamage);
            updateEnemyHpLabel();

            battleLogArea.append("Natural 1! Critical Miss!\n");
            battleLogArea.append(enemy.getName() + " takes " + selfDamage + " self-damage.\n");
            battleLogArea.append(enemy.getName() + " HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + "\n");

            checkEnemyDefeated();

            battleLogArea.append("\n");
            return;
        }

        if (d20Roll == 20) {
            int firstRoll = rollDice(6);
            int secondRoll = rollDice(6);
            int damageModifier = enemy.getAttackBonus();
            int damage = firstRoll + secondRoll + damageModifier;

            damagePlayer(damage);

            battleLogArea.append("Natural 20! Critical Hit!\n");
            battleLogArea.append(
                    "Critical Damage Roll: "
                            + firstRoll
                            + " + "
                            + secondRoll
                            + " + "
                            + damageModifier
                            + " = "
                            + damage
                            + "\n"
            );
            battleLogArea.append(character.getName() + " HP: " + character.getCurrentHp() + "/" + character.getMaxHp() + "\n");

            checkPlayerDefeated();
            battleLogArea.append("\n");
            return;
        }

        if (totalAttack >= calculateAC()) {
            int damageRoll = rollDice(6);
            int damageModifier = enemy.getAttackBonus();
            int damage = damageRoll + damageModifier;

            damagePlayer(damage);

            battleLogArea.append("Hit!\n");
            battleLogArea.append(
                    "Damage Roll: "
                            + damageRoll
                            + " + "
                            + damageModifier
                            + " = "
                            + damage
                            + "\n"
            );
            battleLogArea.append(character.getName() + " HP: " + character.getCurrentHp() + "/" + character.getMaxHp() + "\n");

            checkPlayerDefeated();
            battleLogArea.append("\n");
        } else {
            battleLogArea.append("Miss!\n\n");
        }
    }

    private void damagePlayer(int damage) {
        character.takeDamage(damage);
        updatePlayerHpLabel();
    }

    private void updatePlayerHpLabel() {
        playerHpLabel.setText("HP: " + character.getCurrentHp() + "/" + character.getMaxHp());
    }

    private void checkPlayerDefeated() {
        if (character.isDefeated()) {
            battleLogArea.append(character.getName() + " has been defeated!\n");
            attackButton.setEnabled(false);
        }
    }

    private void saveCharacter() {

        try {

            String filePath =
                    "DATA/" + character.getName() + ".ser";

            saveLoadService.saveCharacter(
                    character,
                    filePath
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Character saved successfully."
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to save character."
            );
        }
    }

    private void saveCharacterAs() {

        JFileChooser fileChooser = new JFileChooser(new File("DATA"));

        fileChooser.setDialogTitle("Save Character");

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            File selectedFile = fileChooser.getSelectedFile();

            String filePath = selectedFile.getAbsolutePath();

            if (!filePath.endsWith(".ser")) {
                filePath += ".ser";
            }

            try {

                saveLoadService.saveCharacter(
                        character,
                        filePath
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Character saved successfully."
                );

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to save character.",
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void showCharacterSheet() {
        JTextArea sheetArea = new JTextArea();
        sheetArea.setEditable(false);
        sheetArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        sheetArea.setText(
                "CHARACTER SHEET\n" +
                        "----------------\n" +
                        "Name: " + character.getName() + "\n" +
                        "Race: " + character.getRace() + "\n" +
                        "Race Bonus: " + getRaceBonusDescription() + "\n" +
                        "Class: " + character.getCharacterClass() + "\n\n" +

                        "HP: " + character.getCurrentHp() + "/" + character.getMaxHp() + "\n" +
                        "AC: " + calculateAC() + "\n" +
                        "Attack Bonus: " + formatModifier(calculateAttackBonus()) + "\n\n" +

                        "STR: " + character.getStats().getStrength() + " (" + formatModifier(calculateModifier(character.getStats().getStrength())) + ")\n" +
                        "DEX: " + character.getStats().getDexterity() + " (" + formatModifier(calculateModifier(character.getStats().getDexterity())) + ")\n" +
                        "CON: " + character.getStats().getConstitution() + " (" + formatModifier(calculateModifier(character.getStats().getConstitution())) + ")\n" +
                        "INT: " + character.getStats().getIntelligence() + " (" + formatModifier(calculateModifier(character.getStats().getIntelligence())) + ")\n" +
                        "WIS: " + character.getStats().getWisdom() + " (" + formatModifier(calculateModifier(character.getStats().getWisdom())) + ")\n" +
                        "LCK: " + character.getStats().getLuck() + " (" + formatModifier(calculateModifier(character.getStats().getLuck())) + ")\n\n" +

                        "Equipment\n" +
                        "---------\n" +
                        "Weapon: " + character.getWeapon()
                        + " ("
                        + getWeaponDamageDescription()
                        + ")\n" +
                        "Armor: " + character.getArmor() + "\n" +
                        "Shield: " + (character.hasShield() ? "Yes" : "No")
        );

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(sheetArea),
                "Character Sheet",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private String getRaceBonusDescription() {

        switch (character.getRace()) {
            case "Human":
                return "+1 STR, +1 CON, +1 LCK";

            case "Elf":
                return "+2 DEX, +1 INT";

            case "Dwarf":
                return "+2 CON, +1 WIS";

            case "Half-Orc":
                return "+2 STR, +1 CON";

            case "Dragonborn":
                return "+2 STR, +1 LCK";

            default:
                return "None";
        }
    }

    private String getWeaponDamageDescription() {

        switch (character.getWeapon()) {
            case "Dagger":
                return "1d4";

            case "Scimitar":
            case "Quarterstaff":
            case "Shortbow":
                return "1d6";

            case "Longsword":
            case "Longbow":
                return "1d8";

            default:
                return "?";
        }
    }

    private void createEnemies() {
        enemies = new Enemy[]{
                new Enemy("Goblin", 10, 12, 2),
                new Enemy("Skeleton", 15, 13, 3),
                new Enemy("Orc", 22, 14, 3),
                new Enemy("Hobgoblin", 30, 15, 4),
                new Enemy("Young Dragon", 45, 17, 5)
        };
    }
}