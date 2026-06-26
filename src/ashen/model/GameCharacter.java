package ashen.model;

import java.io.Serializable;

/**
 * Represents the playable character created by the user.
 * Stores identity, selected equipment, ability scores, difficulty options
 * and current combat health.
 */

public class GameCharacter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Character display name.
     *
     * @serial character display name
     */
    private String name;

    /**
     * Race display name stored for save file compatibility.
     *
     * @serial selected race display name
     */
    private String race;

    /**
     * Class display name stored for save file compatibility.
     *
     * @serial selected class display name
     */
    private String characterClass;

    /**
     * Character ability scores.
     *
     * @serial character ability scores
     */
    private Stats stats;

    /**
     * Weapon display name stored for save file compatibility.
     *
     * @serial selected weapon display name
     */
    private String weapon;

    /**
     * Armor display name stored for save file compatibility.
     *
     * @serial selected armor display name
     */
    private String armor;

    /**
     * Difficulty display name stored for save file compatibility.
     *
     * @serial selected difficulty display name
     */
    private String difficulty;

    /**
     * Character maximum hit points.
     *
     * @serial maximum hit points
     */
    private int maxHp;

    /**
     * Character current hit points.
     *
     * @serial current hit points
     */
    private int currentHp;

    /**
     * Whether hardcore enemy HP bonus is enabled.
     *
     * @serial hardcore HP modifier flag
     */
    private boolean hardcoreHpBonus;

    /**
     * Whether hardcore enemy damage bonus is enabled.
     *
     * @serial hardcore damage modifier flag
     */
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

    /**
     * Creates a new playable character from enum selections.
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
    public GameCharacter(String name, Race race, CharacterClass characterClass,
                         Stats stats, Weapon weapon, Armor armor, Difficulty difficulty,
                         boolean hardcoreHpBonus, boolean hardcoreDamageBonus) {
        this(
                name,
                race == null ? Race.UNKNOWN.getDisplayName() : race.getDisplayName(),
                characterClass == null ? CharacterClass.UNKNOWN.getDisplayName() : characterClass.getDisplayName(),
                stats,
                weapon == null ? Weapon.UNKNOWN.getDisplayName() : weapon.getDisplayName(),
                armor == null ? Armor.UNKNOWN.getDisplayName() : armor.getDisplayName(),
                difficulty == null ? Difficulty.NORMAL.getDisplayName() : difficulty.getDisplayName(),
                hardcoreHpBonus,
                hardcoreDamageBonus
        );
    }

    /**
     * Returns the character name.
     *
     * @return character name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the race name as stored for serialization compatibility.
     *
     * @return race display name
     */
    public String getRace() {
        return race;
    }

    /**
     * Returns the race as an enum value.
     *
     * @return race enum matching the stored display name
     */
    public Race getRaceType() {
        return Race.fromDisplayName(race);
    }

    /**
     * Returns the class name as stored for serialization compatibility.
     *
     * @return character class display name
     */
    public String getCharacterClass() {
        return characterClass;
    }

    /**
     * Returns the character class as an enum value.
     *
     * @return class enum matching the stored display name
     */
    public CharacterClass getCharacterClassType() {
        return CharacterClass.fromDisplayName(characterClass);
    }

    /**
     * Returns the current ability scores.
     *
     * @return character stats
     */
    public Stats getStats() {
        return stats;
    }

    /**
     * Returns the weapon name as stored for serialization compatibility.
     *
     * @return weapon display name
     */
    public String getWeapon() {
        return weapon;
    }

    /**
     * Returns the equipped weapon as an enum value.
     *
     * @return weapon enum matching the stored display name
     */
    public Weapon getWeaponType() {
        return Weapon.fromDisplayName(weapon);
    }

    /**
     * Returns the armor name as stored for serialization compatibility.
     *
     * @return armor display name
     */
    public String getArmor() {
        return armor;
    }

    /**
     * Returns the equipped armor as an enum value.
     *
     * @return armor enum matching the stored display name
     */
    public Armor getArmorType() {
        return Armor.fromDisplayName(armor);
    }

    /**
     * Returns the maximum hit points.
     *
     * @return maximum HP
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Returns the current hit points.
     *
     * @return current HP
     */
    public int getCurrentHp() {
        return currentHp;
    }

    /**
     * Returns the difficulty name as stored for serialization compatibility.
     *
     * @return difficulty display name
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the difficulty as an enum value.
     *
     * @return difficulty enum matching the stored display name
     */
    public Difficulty getDifficultyType() {
        return Difficulty.fromDisplayName(difficulty);
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

    /**
     * Checks whether hardcore mode increases enemy HP.
     *
     * @return true if the enemy HP modifier is enabled
     */
    public boolean hasHardcoreHpBonus() {
        return hardcoreHpBonus;
    }

    /**
     * Checks whether hardcore mode increases enemy damage.
     *
     * @return true if the enemy damage modifier is enabled
     */
    public boolean hasHardcoreDamageBonus() {
        return hardcoreDamageBonus;
    }
}
