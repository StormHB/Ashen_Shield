package ashen.model;

/**
 * Playable character classes.
 */
public enum CharacterClass {
    /**
     * Strength-based front-line class.
     */
    FIGHTER("Fighter"),

    /**
     * Dexterity-based agile class.
     */
    ROGUE("Rogue"),

    /**
     * Intelligence-based spellcasting class.
     */
    WIZARD("Wizard"),

    /**
     * Wisdom-based nature magic class.
     */
    DRUID("Druid"),

    /**
     * Dexterity-based ranged combat class.
     */
    RANGER("Ranger"),

    /**
     * Fallback class value used for unknown saved data.
     */
    UNKNOWN("Unknown");

    private static final CharacterClass[] PLAYABLE_VALUES = {
            FIGHTER,
            ROGUE,
            WIZARD,
            DRUID,
            RANGER
    };

    private final String displayName;

    CharacterClass(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns only classes that can be selected during character creation.
     *
     * @return copy of the playable class array
     */
    public static CharacterClass[] playableValues() {
        return PLAYABLE_VALUES.clone();
    }

    /**
     * Finds a character class by the text stored in save files or shown in the GUI.
     *
     * @param displayName display name to search for
     * @return matching character class, or {@link #UNKNOWN} when no match exists
     */
    public static CharacterClass fromDisplayName(String displayName) {
        for (CharacterClass characterClass : values()) {
            if (characterClass.displayName.equals(displayName)) {
                return characterClass;
            }
        }

        return UNKNOWN;
    }

    /**
     * Returns the user-facing class name.
     *
     * @return display name used in GUI and save files
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the display name so combo boxes show readable class names.
     *
     * @return user-facing class name
     */
    @Override
    public String toString() {
        return displayName;
    }
}
