package ashen.model;

import java.io.Serializable;

/**
 * Stores the six ability scores used by the character.
 * Also provides helper methods for calculating and formatting ability modifiers.
 */

public class Stats implements Serializable {

    private static final long serialVersionUID = 1L;

    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int luck;

    /**
     * Creates a complete set of ability scores.
     *
     * @param strength strength score
     * @param dexterity dexterity score
     * @param constitution constitution score
     * @param intelligence intelligence score
     * @param wisdom wisdom score
     * @param luck luck score
     */

    public Stats(int strength, int dexterity, int constitution,
                 int intelligence, int wisdom, int luck) {
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.luck = luck;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getConstitution() {
        return constitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public int getLuck() {
        return luck;
    }

    /**
     * Calculates the ability modifier for a given ability score.
     *
     * @param statValue ability score value
     * @return calculated ability modifier
     */

    public static int calculateModifier(int statValue) {
        return Math.floorDiv(statValue - 10, 2);
    }

    /**
     * Formats a modifier with a plus sign when it is positive or zero.
     *
     * @param modifier ability modifier
     * @return formatted modifier text
     */

    public static String formatModifier(int modifier) {
        if (modifier >= 0) {
            return "+" + modifier;
        }
        return String.valueOf(modifier);
    }
}