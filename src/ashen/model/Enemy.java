package ashen.model;

import java.io.Serializable;

/**
 * Represents an enemy encountered during combat.
 * Stores combat values such as hit points, armor class and attack bonus.
 */

public class Enemy implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private int maxHp;
    private int currentHp;
    private int armorClass;
    private int attackBonus;

    /**
     * Creates a new enemy with full HP.
     *
     * @param name enemy name
     * @param maxHp maximum hit points
     * @param armorClass armor class used for hit checks
     * @param attackBonus bonus added to enemy attack rolls
     */

    public Enemy(String name, int maxHp, int armorClass, int attackBonus) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.armorClass = armorClass;
        this.attackBonus = attackBonus;
    }

    /**
     * Returns the enemy name.
     *
     * @return enemy name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the enemy maximum HP.
     *
     * @return maximum HP
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Returns the enemy current HP.
     *
     * @return current HP
     */
    public int getCurrentHp() {
        return currentHp;
    }

    /**
     * Returns the armor class used for hit checks.
     *
     * @return armor class
     */
    public int getArmorClass() {
        return armorClass;
    }

    /**
     * Returns the enemy attack bonus.
     *
     * @return attack bonus
     */
    public int getAttackBonus() {
        return attackBonus;
    }

    /**
     * Reduces enemy HP and prevents it from going below zero.
     *
     * @param damage amount of damage taken
     */

    public void takeDamage(int damage) {
        currentHp -= damage;

        if (currentHp < 0) {
            currentHp = 0;
        }
    }

    /**
     * Checks whether the enemy has no remaining HP.
     *
     * @return true if the enemy is defeated
     */

    public boolean isDefeated() {
        return currentHp <= 0;
    }
}
