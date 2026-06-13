package ashen.gui;

import ashen.model.Enemy;
import ashen.model.GameCharacter;

import javax.swing.*;
import java.awt.*;

public class BattlePanel extends JPanel {

    private GameCharacter character;
    private Enemy enemy;

    private JLabel playerHpLabel;
    private JLabel playerAcLabel;
    private JLabel enemyHpLabel;
    private JLabel enemyAcLabel;

    private JTextArea battleLogArea;
    private JButton attackButton;

    public BattlePanel(GameCharacter character) {
        this.character = character;
        this.enemy = new Enemy("Goblin", 10, 12, 2);
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

        actionPanel.add(attackButton);

        add(actionPanel, BorderLayout.SOUTH);

        battleLogArea.append("Battle started!\n");
        battleLogArea.append(character.getName() + " encounters " + enemy.getName() + ".\n\n");
    }

    private JPanel createPlayerPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Player"
        ));

        playerHpLabel = new JLabel("HP: " + calculateHP() + "/" + calculateHP());
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
            battleLogArea.append("Natural 1! Critical Miss!\n\n");
            return;
        }

        if (d20Roll == 20) {
            int damage = calculateCriticalDamage();

            enemy.takeDamage(damage);
            updateEnemyHpLabel();

            battleLogArea.append("Natural 20! Critical Hit!\n");
            battleLogArea.append("Damage: " + damage + "\n");
            battleLogArea.append(enemy.getName() + " HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + "\n");

            checkEnemyDefeated();

            battleLogArea.append("\n");
            return;
        }

        battleLogArea.append("Attack Roll: " + d20Roll + " + " + attackBonus + " = " + totalAttack + "\n");

        if (totalAttack >= enemy.getArmorClass()) {
            int damage = calculateDamage();

            enemy.takeDamage(damage);
            updateEnemyHpLabel();

            battleLogArea.append("Hit! Damage: " + damage + "\n");
            battleLogArea.append(enemy.getName() + " HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + "\n");

            checkEnemyDefeated();

            battleLogArea.append("\n");
        } else {
            battleLogArea.append("Miss!\n\n");
        }
    }

    private void updateEnemyHpLabel() {
        enemyHpLabel.setText("HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp());
    }

    private void checkEnemyDefeated() {
        if (enemy.isDefeated()) {
            battleLogArea.append(enemy.getName() + " has been defeated!\n");
            attackButton.setEnabled(false);
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
}