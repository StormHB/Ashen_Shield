package ashen.gui;

import ashen.model.GameCharacter;
import ashen.model.Enemy;
import javax.swing.*;
import java.awt.*;


public class BattlePanel extends JPanel {

    private GameCharacter character;
    private Enemy enemy;

    public BattlePanel(GameCharacter character) {
        this.character = character;
        this.enemy = new Enemy("Goblin", 10, 12, 2);
        layoutComponents();
    }

    private void layoutComponents() {

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Battle Screen", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));

        add(title, BorderLayout.NORTH);

        JTextArea infoArea = new JTextArea();

        infoArea.setEditable(false);

        infoArea.setText(
                "Name: " + character.getName() + "\n" +
                        "Race: " + character.getRace() + "\n" +
                        "Class: " + character.getCharacterClass() + "\n\n" +

                        "HP: " + calculateHP() + "\n" +
                        "AC: " + calculateAC() + "\n" +
                        "Attack Bonus: " +
                        formatModifier(calculateAttackBonus()) + "\n\n" +

                        "STR: " + character.getStats().getStrength() + "\n" +
                        "DEX: " + character.getStats().getDexterity() + "\n" +
                        "CON: " + character.getStats().getConstitution() + "\n" +
                        "INT: " + character.getStats().getIntelligence() + "\n" +
                        "WIS: " + character.getStats().getWisdom() + "\n" +
                        "LCK: " + character.getStats().getLuck() + "\n\n" +

                        "Equipment\n" +
                        "---------\n" +
                        "Weapon: " + character.getWeapon() + "\n" +
                        "Armor: " + character.getArmor() + "\n" +
                        "Shield: " +
                        (character.hasShield() ? "Yes" : "No") +

                        "\n\nEnemy\n" +
                        "---------\n" +
                        "Name: " + enemy.getName() + "\n" +
                        "HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + "\n" +
                        "AC: " + enemy.getArmorClass() + "\n" +
                        "Attack Bonus: " + formatModifier(enemy.getAttackBonus())
        );

        add(new JScrollPane(infoArea), BorderLayout.CENTER);
    }

    private int calculateModifier(int statValue) {
        return Math.floorDiv(statValue - 10, 2);
    }

    private int calculateHP() {
        return 10 + character.getStats().getConstitution();
    }

    private int calculateAttackBonus() {

        String characterClass = character.getCharacterClass();

        if ("Fighter".equals(characterClass)) {
            return calculateModifier(character.getStats().getStrength());
        }

        if ("Rogue".equals(characterClass)) {
            return calculateModifier(character.getStats().getDexterity());
        }

        if ("Ranger".equals(characterClass)) {
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

    private int calculateAC() {

        int dexModifier =
                calculateModifier(character.getStats().getDexterity());

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

    private String formatModifier(int modifier) {
        if (modifier >= 0) {
            return "+" + modifier;
        }

        return String.valueOf(modifier);
    }
}