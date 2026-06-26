package ashen.model;

/**
 * Ability scores used by characters.
 */
public enum Ability {
    STRENGTH("STR"),
    DEXTERITY("DEX"),
    CONSTITUTION("CON"),
    INTELLIGENCE("INT"),
    WISDOM("WIS"),
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
