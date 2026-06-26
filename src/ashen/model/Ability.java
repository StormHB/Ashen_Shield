package ashen.model;

/**
 * Ability scores used by characters.
 */
public enum Ability {
    /**
     * Strength score used for physical power.
     */
    STRENGTH("STR"),

    /**
     * Dexterity score used for agility and finesse.
     */
    DEXTERITY("DEX"),

    /**
     * Constitution score used for endurance and hit points.
     */
    CONSTITUTION("CON"),

    /**
     * Intelligence score used for arcane knowledge.
     */
    INTELLIGENCE("INT"),

    /**
     * Wisdom score used for instinct and awareness.
     */
    WISDOM("WIS"),

    /**
     * Luck score used for chance-based bonuses.
     */
    LUCK("LCK");

    private final String abbreviation;

    Ability(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * Returns the short label displayed for this ability score.
     *
     * @return three-letter ability abbreviation
     */
    public String getAbbreviation() {
        return abbreviation;
    }
}
