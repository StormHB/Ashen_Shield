package ashen.model;

import java.io.Serializable;

/**
 * Represents the playable character created by the user.
 * Stores identity, selected equipment, ability scores, difficulty options
 * and current combat health.
 */

public class GameCharacter implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String race;
    private String characterClass;
    private Stats stats;
    private String weapon;
    private String armor;
    private String difficulty;

    private int maxHp;
    private int currentHp;

    private boolean hardcoreHpBonus;
    private boolean hardcoreDamageBonus;

    /**
     * Creates a new playable character and calculates maximum HP from constitution.
     *
     * @param name character name
     * @param race selected race
     * @param characterClass selected class
     * @param stats ability scores
     * @param weapon selected weapon
     * @param armor selected armor
     * @param difficulty selected difficulty mode
     * @param hardcoreHpBonus true if hardcore HP modifier is enabled
     * @param hardcoreDamageBonus true if hardcore damage modifier is enabled
     */

    public GameCharacter(String name, String race, String characterClass,
                         Stats stats, String weapon, String armor, String difficulty, boolean hardcoreHpBonus, boolean hardcoreDamageBonus) {
        this.name = name;
        this.race = race;
        this.characterClass = characterClass;
        this.stats = stats;
        this.weapon = weapon;
        this.armor = armor;
        this.difficulty = difficulty;
        this.hardcoreHpBonus = hardcoreHpBonus;
        this.hardcoreDamageBonus = hardcoreDamageBonus;

        this.maxHp = 10 + stats.getConstitution();
        this.currentHp = this.maxHp;
    }

    public String getName() {
        return name;
    }

    public String getRace() {
        return race;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public Stats getStats() {
        return stats;
    }

    public String getWeapon() {
        return weapon;
    }

    public String getArmor() {
        return armor;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Reduces character HP and prevents it from going below zero.
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
     * Restores HP up to the character maximum HP value.
     *
     * @param amount amount of HP to restore
     */

    public void heal(int amount) {
        currentHp += amount;

        if (currentHp > maxHp) {
            currentHp = maxHp;
        }
    }

    /**
     * Checks whether the character has no remaining HP.
     *
     * @return true if the character is defeated
     */

    public boolean isDefeated() {
        return currentHp <= 0;
    }

    /**
     * Restores the character to full HP.
     */

    public void restoreFullHp() {
        currentHp = maxHp;
    }

    public boolean hasHardcoreHpBonus() {
        return hardcoreHpBonus;
    }

    public boolean hasHardcoreDamageBonus() {
        return hardcoreDamageBonus;
    }
}