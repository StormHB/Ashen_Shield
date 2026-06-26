package ashen.gui;

import ashen.combat.BattleRules;
import ashen.model.Enemy;
import ashen.model.GameCharacter;
import ashen.model.Stats;

import javax.swing.*;
import java.awt.Component;
import java.awt.Font;

/**
 * Utility methods for battle sheet dialogs.
 */
public final class BattleSheetDialogs {

    private BattleSheetDialogs() {
    }

    /**
     * Shows a dialog with detailed character combat information.
     *
     * @param parent component used as the dialog parent
     * @param character character to display
     */
    public static void showCharacterSheet(Component parent, GameCharacter character) {
        JTextArea sheetArea = createSheetTextArea();

        sheetArea.setText(
                "CHARACTER SHEET\n" +
                        "----------------\n" +
                        "Name: " + character.getName() + "\n" +
                        "Race: " + character.getRace() + "\n" +
                        "Race Bonus: " + BattleRules.getRaceBonusDescription(character) + "\n" +
                        "Class: " + character.getCharacterClass() + "\n" +
                        "Difficulty: " + BattleRules.getDifficultyDescription(character) + "\n\n" +

                        "HP: " + character.getCurrentHp() + "/" + character.getMaxHp() + "\n" +
                        "AC: " + BattleRules.calculateArmorClass(character) + "\n" +
                        "Attack Bonus: " + Stats.formatModifier(BattleRules.calculateAttackBonus(character)) + "\n\n" +

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
                        + BattleRules.getWeaponDamageDescription(character)
                        + ")\n" +
                        "Armor: " + character.getArmor() + "\n"
        );

        JOptionPane.showMessageDialog(
                parent,
                new JScrollPane(sheetArea),
                "Character Sheet",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    /**
     * Shows a dialog with detailed enemy combat information.
     *
     * @param parent component used as the dialog parent
     * @param enemy enemy to display
     */
    public static void showEnemySheet(Component parent, Enemy enemy) {
        JTextArea sheetArea = createSheetTextArea();

        sheetArea.setText(
                "ENEMY SHEET\n" +
                        "-----------\n" +
                        "Name: " + enemy.getName() + "\n\n" +

                        "HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + "\n" +
                        "AC: " + enemy.getArmorClass() + "\n" +
                        "Attack Bonus: " + Stats.formatModifier(enemy.getAttackBonus()) + "\n" +
                        "Damage: 1d6 + " + BattleRules.calculateEnemyDamageModifier(enemy)
        );

        JOptionPane.showMessageDialog(
                parent,
                new JScrollPane(sheetArea),
                "Enemy Sheet",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private static JTextArea createSheetTextArea() {
        JTextArea sheetArea = new JTextArea();

        sheetArea.setEditable(false);
        sheetArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        return sheetArea;
    }
}
