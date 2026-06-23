package ashen.gui;

import ashen.model.Enemy;
import ashen.model.GameCharacter;
import ashen.model.Stats;
import ashen.service.SaveLoadService;

import java.io.File;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;

public class BattlePanel extends JPanel {

    private GameCharacter character;
    private Enemy enemy;
    private boolean playerDefeated;
    private boolean currentBattleLogSaved;

    private String lastDamageFormula;
    private String lastDamageDescription;
    private boolean rogueSneakAttackUsed;
    private int rangerPoisonDamage;

    private int currentEnemyIndex = 0;
    private Enemy[] enemies;

    private JLabel playerHpLabel;
    private JLabel playerAcLabel;
    private JLabel enemyHpLabel;
    private JLabel enemyAcLabel;

    private JTextArea battleLogArea;
    private JButton attackButton;
    private JButton nextButton;

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
        mainFrame.setJMenuBar(createBattleMenuBar());
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

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

        attackButton = new JButton("Attack (A)");
        attackButton.addActionListener(e -> handleAttack());
        attackButton.setPreferredSize(new Dimension(120, 40));

        nextButton = new JButton("Next (N)");
        nextButton.setPreferredSize(new Dimension(120, 40));
        nextButton.setVisible(false);
        nextButton.addActionListener(e -> {
            if (playerDefeated) {
                mainFrame.showDefeatPanel(character);
            } else {
                mainFrame.showVictoryPanel(character, currentEnemyIndex, enemy.getName());
            }
        });

        actionPanel.add(attackButton);
        actionPanel.add(nextButton);

        add(actionPanel, BorderLayout.SOUTH);

        battleLogArea.append("Battle " + (currentEnemyIndex + 1) + "/" + enemies.length + " started!\n");
        battleLogArea.append(character.getName() + " encounters " + enemy.getName() + ".\n\n");

        setupBattleShortcuts();
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
        panel.add(new JLabel("Attack Bonus: " + Stats.formatModifier(calculateAttackBonus())));
        panel.add(new JLabel("Weapon: " + character.getWeapon()));
        panel.add(new JLabel("Armor: " + character.getArmor()));

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
        panel.add(new JLabel("Attack Bonus: " + Stats.formatModifier(enemy.getAttackBonus())));

        return panel;
    }

    private void handleAttack() {
        playerAttack(true);
        scrollBattleLogToBottom();
    }

    private void scrollBattleLogToBottom() {
        battleLogArea.setCaretPosition(
                battleLogArea.getDocument().getLength()
        );
    }

    private void playerAttack(boolean enemyResponds) {

        if ("Rogue".equals(character.getCharacterClass()) && !rogueSneakAttackUsed) {
            rogueSneakAttackUsed = true;

            int baseDamage = calculatePlayerDamage();
            int sneakRoll = rollDice(8);

            int damage = baseDamage + sneakRoll;

            if (damage < 1) {
                damage = 1;
            }

            enemy.takeDamage(damage);
            updateEnemyHpLabel();

            battleLogArea.append(character.getName() + " uses Sneak Attack!\n");
            battleLogArea.append("Automatic Hit!\n");
            battleLogArea.append(
                    "Damage Roll: "
                            + lastDamageFormula
                            + " + Sneak Attack "
                            + sneakRoll
                            + " = "
                            + damage
                            + "\n"
            );

            battleLogArea.append(enemy.getName() + " HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + "\n");

            checkEnemyDefeated();

            if (!enemy.isDefeated() && enemyResponds) {
                battleLogArea.append("\n");
                enemyTurn();
            } else {
                battleLogArea.append("\n");
            }

            return;
        }

        int d20Roll = rollDice(20);

        if ("Wizard".equals(character.getCharacterClass()) && d20Roll <= 5) {
            int oldRoll = d20Roll;
            d20Roll = rollDice(20);

            battleLogArea.append(
                    "Arcane Precision: rerolled "
                            + oldRoll
                            + " into "
                            + d20Roll
                            + ".\n"
            );
        }

        int attackBonus = calculateAttackBonus();
        int totalAttack = d20Roll + attackBonus;

        if ("Wizard".equals(character.getCharacterClass())) {
            battleLogArea.append(character.getName() + " casts Fireball at " + enemy.getName() + ".\n");
        } else {
            battleLogArea.append(character.getName() + " attacks " + enemy.getName() + ".\n");
        }

        if (d20Roll == 1) {
            int selfDamage = rollDice(4);

            damagePlayer(selfDamage);

            battleLogArea.append("Natural 1! Critical Miss!\n");
            battleLogArea.append(character.getName() + " takes " + selfDamage + " self-damage.\n");
            battleLogArea.append(character.getName() + " HP: " + character.getCurrentHp() + "/" + character.getMaxHp() + "\n\n");

            checkPlayerDefeated();

            if (!character.isDefeated() && enemyResponds) {
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

            if ("Ranger".equals(character.getCharacterClass())) {
                int poisonRoll = rollDice(2);
                rangerPoisonDamage += poisonRoll;

                battleLogArea.append(
                        "Poison Arrow: +"
                                + poisonRoll
                                + " poison damage. Total poison: "
                                + rangerPoisonDamage
                                + "\n"
                );
            }

            battleLogArea.append("Natural 20! Critical Hit!\n");
            battleLogArea.append("Critical Damage Roll: " + firstRoll + " + " + secondRoll + " + " + damageModifier + " = " + damage + "\n");
            battleLogArea.append(enemy.getName() + " HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + "\n");

            checkEnemyDefeated();

            if (!enemy.isDefeated()) {
                applyRangerPoison();
            }

            if (!enemy.isDefeated() && enemyResponds) {
                battleLogArea.append("\n");
                enemyTurn();
            } else {
                battleLogArea.append("\n");
            }

            return;
        }

        battleLogArea.append("Attack Roll: " + d20Roll + " + " + attackBonus + " = " + totalAttack + "\n");

        if (totalAttack >= enemy.getArmorClass()) {
            int damage = calculatePlayerDamage();

            if (damage < 1) {
                damage = 1;
            }

            battleLogArea.append("Hit!\n");
            battleLogArea.append("Damage Roll: " + lastDamageDescription + "\n");

            enemy.takeDamage(damage);
            updateEnemyHpLabel();

            if ("Ranger".equals(character.getCharacterClass())) {
                int poisonRoll = rollDice(2);
                rangerPoisonDamage += poisonRoll;

                battleLogArea.append(
                        "Poison Arrow: +"
                                + poisonRoll
                                + " poison damage. Total poison: "
                                + rangerPoisonDamage
                                + "\n"
                );
            }

            battleLogArea.append(enemy.getName() + " HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + "\n");

            checkEnemyDefeated();

            if (!enemy.isDefeated()) {
                applyRangerPoison();
            }

            if (!enemy.isDefeated() && enemyResponds) {
                battleLogArea.append("\n");
                enemyTurn();
            } else {
                battleLogArea.append("\n");
            }
        } else {
            battleLogArea.append("Miss!\n");

            applyRangerPoison();

            if (!enemy.isDefeated() && enemyResponds) {
                battleLogArea.append("\n");
                enemyTurn();
            } else {
                battleLogArea.append("\n");
            }
        }
    }

    private void updateEnemyHpLabel() {
        enemyHpLabel.setText("HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp());
    }

    private void checkEnemyDefeated() {
        if (enemy.isDefeated()) {
            battleLogArea.append(enemy.getName() + " has been defeated!\n");
            attackButton.setEnabled(false);
            nextButton.setVisible(true);
            scrollBattleLogToBottom();
            saveCurrentBattleLogToCampaignLog();
        }
    }

    private int calculateAbilityModifierForAttack() {
        String characterClass = character.getCharacterClass();

        if ("Fighter".equals(characterClass)) {
            return Stats.calculateModifier(character.getStats().getStrength());
        }

        if ("Rogue".equals(characterClass) || "Ranger".equals(characterClass)) {
            return Stats.calculateModifier(character.getStats().getDexterity());
        }

        if ("Wizard".equals(characterClass)) {
            return Stats.calculateModifier(character.getStats().getIntelligence());
        }

        if ("Druid".equals(characterClass)) {
            return Stats.calculateModifier(character.getStats().getWisdom());
        }

        return 0;
    }

    private int calculateAttackBonus() {
        int proficiencyBonus = 2;
        int attackBonus = proficiencyBonus + calculateAbilityModifierForAttack();

        if ("Dual Daggers".equals(character.getWeapon())) {
            attackBonus += 2;
        }

        return attackBonus;
    }

    private int calculatePlayerDamage() {

        int modifier = calculateAbilityModifierForAttack();
        String statName = getMainStatName();

        switch (character.getWeapon()) {
            case "Scimitar + Dagger": {
                int mainHandRoll = rollDice(6);
                int offHandRoll = rollDice(4);

                int damage = mainHandRoll + modifier + offHandRoll;

                lastDamageFormula =
                        "Main Hand "
                                + mainHandRoll
                                + " + "
                                + statName
                                + " "
                                + modifier
                                + " + Off Hand "
                                + offHandRoll;

                lastDamageDescription =
                        lastDamageFormula
                                + " = "
                                + damage;

                return damage;
            }

            case "Dual Daggers": {
                int mainHandRoll = rollDice(4);
                int offHandRoll = rollDice(4);

                int damage = mainHandRoll + modifier + offHandRoll;

                lastDamageFormula =
                        "Main Hand "
                                + mainHandRoll
                                + " + "
                                + statName
                                + " "
                                + modifier
                                + " + Off Hand "
                                + offHandRoll;

                lastDamageDescription =
                        lastDamageFormula
                                + " = "
                                + damage;

                return damage;
            }

            case "Rod + Spellbook": {
                int spellRoll = rollDice(getWeaponDamageDice());

                int damage = spellRoll + modifier;

                lastDamageFormula =
                        "Fireball "
                                + spellRoll
                                + " + "
                                + statName
                                + " "
                                + modifier;

                lastDamageDescription =
                        lastDamageFormula
                                + " = "
                                + damage;

                return damage;
            }

            default: {
                int weaponRoll = rollDice(getWeaponDamageDice());

                int damage = weaponRoll + modifier;

                lastDamageFormula =
                        "Weapon "
                                + weaponRoll
                                + " + "
                                + statName
                                + " "
                                + modifier;

                lastDamageDescription =
                        lastDamageFormula
                                + " = "
                                + damage;

                return damage;
            }
        }
    }

    private String getMainStatName() {

        switch (character.getCharacterClass()) {
            case "Fighter":
                return "STR";

            case "Rogue":
            case "Ranger":
                return "DEX";

            case "Wizard":
                return "INT";

            case "Druid":
                return "WIS";

            default:
                return "Modifier";
        }
    }

    private void applyRangerPoison() {

        if (!"Ranger".equals(character.getCharacterClass())) {
            return;
        }

        if (rangerPoisonDamage <= 0) {
            return;
        }

        enemy.takeDamage(rangerPoisonDamage);
        updateEnemyHpLabel();

        battleLogArea.append(
                "Poison Arrows deal "
                        + rangerPoisonDamage
                        + " poison damage.\n"
        );

        battleLogArea.append(
                enemy.getName()
                        + " HP: "
                        + enemy.getCurrentHp()
                        + "/"
                        + enemy.getMaxHp()
                        + "\n"
        );

        checkEnemyDefeated();
    }

    private int calculateAC() {
        int ac;

        switch (character.getArmor()) {
            case "Cloth Robe":
                ac = 10;
                break;

            case "Leather Tunic":
                ac = 12;
                break;

            case "Leather Armor":
                ac = 14;
                break;

            case "Hide Armor":
                ac = 16;
                break;

            case "Chain Mail":
                ac = 15;
                break;

            case "Plate Armor":
                ac = 17;
                break;

            default:
                ac = 10;
        }

        if ("Druid".equals(character.getCharacterClass())) {
            ac += 2;
        }

        if ("Longsword + Shield".equals(character.getWeapon())) {
            ac += 2;
        }

        return ac;
    }

    private int rollDice(int sides) {
        return (int) (Math.random() * sides) + 1;
    }

    private int getWeaponDamageDice() {
        switch (character.getWeapon()) {
            case "Longsword + Shield":
                return 8;
            case "Greatsword":
                return 12;
            case "Scimitar + Dagger":
                return 6;
            case "Dual Daggers":
                return 4;
            case "Rod + Spellbook":
                return 12;
            case "Quarterstaff":
                return 10;
            case "Longbow":
                return 10;
            default:
                return 4;
        }
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
            int baseDamage = firstRoll + secondRoll + damageModifier;
            int damage = applyDifficultyDamage(baseDamage);
            damagePlayer(damage);

            battleLogArea.append("Natural 20! Critical Hit!\n");
            if (character.hasHardcoreDamageBonus()) {

                battleLogArea.append(
                        "Critical Damage Roll: "
                                + firstRoll
                                + " + "
                                + secondRoll
                                + " + "
                                + damageModifier
                                + " = "
                                + baseDamage
                                + "\n"
                );

                battleLogArea.append(
                        "Total Damage (Hardcore bonus): "
                                + damage
                                + "\n"
                );

            } else {

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
            }
            battleLogArea.append(character.getName() + " HP: " + character.getCurrentHp() + "/" + character.getMaxHp() + "\n");

            checkPlayerDefeated();
            battleLogArea.append("\n");
            return;
        }

        if (totalAttack >= calculateAC()) {
            int damageRoll = rollDice(6);
            int damageModifier = calculateEnemyDamageModifier();

            int baseDamage = damageRoll + damageModifier;
            int damage = applyDifficultyDamage(baseDamage);

            damagePlayer(damage);

            battleLogArea.append("Hit!\n");

            if (character.hasHardcoreDamageBonus()) {

                battleLogArea.append(
                        "Damage Roll: "
                                + damageRoll
                                + " + "
                                + damageModifier
                                + " = "
                                + baseDamage
                                + "\n"
                );

                battleLogArea.append(
                        "Total Damage (Hardcore bonus): "
                                + damage
                                + "\n"
                );

            } else {

                battleLogArea.append(
                        "Damage Roll: "
                                + damageRoll
                                + " + "
                                + damageModifier
                                + " = "
                                + damage
                                + "\n"
                );
            }

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

            battleLogArea.append(
                    character.getName()
                            + " has been defeated!\n"
            );

            playerDefeated = true;
            attackButton.setEnabled(false);
            nextButton.setVisible(true);
            nextButton.setText("Continue (N)");
            scrollBattleLogToBottom();
            saveCurrentBattleLogToCampaignLog();
        }
    }

    private void saveCharacter() {
        String filePath = "DATA/" + character.getName() + ".ser";
        saveCharacterToFile(filePath);
    }

    private void saveCharacterAs() {
        JFileChooser fileChooser = new JFileChooser(new File("DATA"));
        fileChooser.setDialogTitle("Save Character As");
        fileChooser.setSelectedFile(new File(character.getName() + ".ser"));

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            if (!filePath.endsWith(".ser")) {
                filePath += ".ser";
            }

            saveCharacterToFile(filePath);
        }
    }

    private void saveCharacterToFile(String filePath) {
        File saveFile = new File(filePath);

        if (saveFile.exists()) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "A save file with this name already exists.\nDo you want to overwrite it?",
                    "Overwrite Save",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            saveLoadService.saveCharacter(character, filePath);

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
                        "Class: " + character.getCharacterClass() + "\n" +
                        "Difficulty: " + getDifficultyDescription() + "\n\n" +

                        "HP: " + character.getCurrentHp() + "/" + character.getMaxHp() + "\n" +
                        "AC: " + calculateAC() + "\n" +
                        "Attack Bonus: " + Stats.formatModifier(calculateAttackBonus()) + "\n\n" +

                        "STR: " + character.getStats().getStrength() + " (" + Stats.formatModifier(Stats.calculateModifier(character.getStats().getStrength())) + ")\n" +
                        "DEX: " + character.getStats().getDexterity() + " (" + Stats.formatModifier(Stats.calculateModifier(character.getStats().getDexterity())) + ")\n" +
                        "CON: " + character.getStats().getConstitution() + " (" + Stats.formatModifier(Stats.calculateModifier(character.getStats().getConstitution())) + ")\n" +
                        "INT: " + character.getStats().getIntelligence() + " (" + Stats.formatModifier(Stats.calculateModifier(character.getStats().getIntelligence())) + ")\n" +
                        "WIS: " + character.getStats().getWisdom() + " (" + Stats.formatModifier(Stats.calculateModifier(character.getStats().getWisdom())) + ")\n" +
                        "LCK: " + character.getStats().getLuck() + " (" + Stats.formatModifier(Stats.calculateModifier(character.getStats().getLuck())) + ")\n\n" +

                        "Equipment\n" +
                        "---------\n" +
                        "Weapon: " + character.getWeapon()
                        + " ("
                        + getWeaponDamageDescription()
                        + ")\n" +
                        "Armor: " + character.getArmor() + "\n"
        );

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(sheetArea),
                "Character Sheet",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private void showEnemySheet() {
        JTextArea sheetArea = new JTextArea();
        sheetArea.setEditable(false);
        sheetArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        sheetArea.setText(
                "ENEMY SHEET\n" +
                        "-----------\n" +
                        "Name: " + enemy.getName() + "\n\n" +

                        "HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + "\n" +
                        "AC: " + enemy.getArmorClass() + "\n" +
                        "Attack Bonus: " + Stats.formatModifier(enemy.getAttackBonus()) + "\n" +
                        "Damage: 1d6 + " + calculateEnemyDamageModifier()
        );

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(sheetArea),
                "Enemy Sheet",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private String getRaceBonusDescription() {
        switch (character.getRace()) {
            case "Human":
                return "+1 STR, +1 DEX, +1 CON, +1 LCK";
            case "Elf":
                return "+2 DEX, +1 CON, +1 LCK";
            case "Dwarf":
                return "+1 STR, +1 CON, +2 WIS";
            case "Tiefling":
                return "+2 INT, +2 LCK";
            case "Dragonborn":
                return "+2 STR, +1 CON, +1 LCK";
            default:
                return "None";
        }
    }

    private String getWeaponDamageDescription() {

        switch (character.getWeapon()) {
            case "Longsword + Shield":
                return "1d8 + STR, +2 AC";

            case "Greatsword":
                return "1d12 + STR";

            case "Dual Daggers":
                return "1d4 + DEX + 1d4, +2 Attack Bonus";

            case "Scimitar + Dagger":
                return "1d6 + DEX + 1d4";

            case "Rod + Spellbook":
                return "Fireball 1d12 + INT, +2 INT";

            case "Quarterstaff":
                return "1d10 + WIS";

            case "Longbow":
                return "1d10 + DEX";

            default:
                return "?";
        }
    }

    private int applyDifficultyHp(int hp) {
        if (character.hasHardcoreHpBonus()) {
            return (int) Math.round(hp * 1.25);
        }

        return hp;
    }

    private void createEnemies() {
        enemies = new Enemy[]{
                new Enemy("Goblin", applyDifficultyHp(10), 12, 2),
                new Enemy("Skeleton", applyDifficultyHp(15), 13, 3),
                new Enemy("Orc", applyDifficultyHp(22), 14, 3),
                new Enemy("Hobgoblin", applyDifficultyHp(30), 15, 4),
                new Enemy("Young Dragon", applyDifficultyHp(40), 16, 5)
        };
    }

    private int calculateEnemyDamageModifier() {
        if ("Goblin".equals(enemy.getName())) {
            return 1;
        }

        if ("Skeleton".equals(enemy.getName())) {
            return 1;
        }

        if ("Orc".equals(enemy.getName())) {
            return 2;
        }

        if ("Hobgoblin".equals(enemy.getName())) {
            return 2;
        }

        if ("Young Dragon".equals(enemy.getName())) {
            return 4;
        }

        return 1;
    }

    private void exportBattleLog() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Export Battle Log");

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            File selectedFile = fileChooser.getSelectedFile();

            String filePath = selectedFile.getAbsolutePath();

            if (!filePath.endsWith(".txt")) {
                filePath += ".txt";
            }

            try (PrintWriter writer = new PrintWriter(filePath)) {

                String exportText = mainFrame.getCampaignBattleLog();

                if (!currentBattleLogSaved) {
                    exportText += battleLogArea.getText();
                }

                writer.print(exportText);
                JOptionPane.showMessageDialog(
                        this,
                        "Battle log exported successfully."
                );

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to export battle log.",
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void saveCurrentBattleLogToCampaignLog() {
        if (!currentBattleLogSaved) {
            mainFrame.appendToCampaignBattleLog(battleLogArea.getText());
            currentBattleLogSaved = true;
        }
    }

    private JMenuBar createBattleMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem mainMenuItem = new JMenuItem("Main Menu");

        mainMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_M,
                        InputEvent.CTRL_DOWN_MASK
                )
        );

        mainMenuItem.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Return to Main Menu? Current battle progress will be lost.",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                mainFrame.showMainMenu();
            }
        });

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveItem.addActionListener(e -> saveCharacter());

        JMenuItem saveAsItem = new JMenuItem("Save As");
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        saveAsItem.addActionListener(e -> saveCharacterAs());

        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
        loadItem.addActionListener(e -> mainFrame.loadCharacter());

        JMenuItem exportLogItem = new JMenuItem("Export Battle Log");
        exportLogItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
        exportLogItem.addActionListener(e -> exportBattleLog());

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(mainMenuItem);
        fileMenu.addSeparator();

        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(loadItem);
        fileMenu.add(exportLogItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu sheetsMenu = new JMenu("Sheets");

        JMenuItem characterSheetItem = new JMenuItem("Character Sheet");
        characterSheetItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        characterSheetItem.addActionListener(e -> showCharacterSheet());

        JMenuItem enemySheetItem = new JMenuItem("Enemy Sheet");
        enemySheetItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        enemySheetItem.addActionListener(e -> showEnemySheet());

        sheetsMenu.add(characterSheetItem);
        sheetsMenu.add(enemySheetItem);

        menuBar.add(fileMenu);
        menuBar.add(sheetsMenu);

        return menuBar;
    }

    private void setupBattleShortcuts() {

        JRootPane rootPane = mainFrame.getRootPane();

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_A, 0),
                "attack"
        );

        rootPane.getActionMap().put(
                "attack",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (attackButton.isEnabled()) {
                            handleAttack();
                        }
                    }
                }
        );

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_N, 0),
                "next"
        );

        rootPane.getActionMap().put(
                "next",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (nextButton.isVisible() && nextButton.isEnabled()) {
                            nextButton.doClick();
                        }
                    }
                }
        );

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK),
                "exit"
        );

        rootPane.getActionMap().put(
                "exit",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_C,
                        InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK
                ),
                "characterSheet"
        );

        rootPane.getActionMap().put(
                "characterSheet",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showCharacterSheet();
                    }
                }
        );

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK),
                "enemySheet"
        );

        rootPane.getActionMap().put(
                "enemySheet",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showEnemySheet();
                    }
                }
        );

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_M,
                        InputEvent.CTRL_DOWN_MASK
                ),
                "mainMenu"
        );

        rootPane.getActionMap().put(
                "mainMenu",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        int choice = JOptionPane.showConfirmDialog(
                                BattlePanel.this,
                                "Return to Main Menu? Current battle progress will be lost.",
                                "Confirm",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (choice == JOptionPane.YES_OPTION) {
                            mainFrame.showMainMenu();
                        }
                    }
                }
        );
    }

    private int applyDifficultyDamage(int damage) {
        if (character.hasHardcoreDamageBonus()) {
            return (int) Math.round(damage * 1.25);
        }

        return damage;
    }

    private String getDifficultyDescription() {
        if (!"Hardcore".equals(character.getDifficulty())) {
            return "Normal";
        }

        String description = "Hardcore";

        if (character.hasHardcoreHpBonus()) {
            description += " (+25% enemy HP)";
        }

        if (character.hasHardcoreDamageBonus()) {
            description += " (+25% enemy damage)";
        }

        return description;
    }
}