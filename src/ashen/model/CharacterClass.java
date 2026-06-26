package ashen.model;

/**
 * Playable character classes.
 */
public enum CharacterClass {
    FIGHTER("Fighter"),
    ROGUE("Rogue"),
    WIZARD("Wizard"),
    DRUID("Druid"),
    RANGER("Ranger"),
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

    public static CharacterClass[] playableValues() {
        return PLAYABLE_VALUES.clone();
    }

    public static CharacterClass fromDisplayName(String displayName) {
        for (CharacterClass characterClass : values()) {
            if (characterClass.displayName.equals(displayName)) {
                return characterClass;
            }
        }

        return UNKNOWN;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
