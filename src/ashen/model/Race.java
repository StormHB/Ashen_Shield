package ashen.model;

/**
 * Playable character races.
 */
public enum Race {
    /**
     * Balanced race with bonuses across several abilities.
     */
    HUMAN("Human", 1, 1, 1, 0, 0, 1, "+1 STR, +1 DEX, +1 CON, +1 LCK"),

    /**
     * Agile race focused on dexterity.
     */
    ELF("Elf", 0, 2, 1, 0, 0, 1, "+2 DEX, +1 CON, +1 LCK"),

    /**
     * Hardy race focused on strength, constitution and wisdom.
     */
    DWARF("Dwarf", 1, 0, 1, 0, 2, 0, "+1 STR, +1 CON, +2 WIS"),

    /**
     * Arcane race focused on intelligence and luck.
     */
    TIEFLING("Tiefling", 0, 0, 0, 2, 0, 2, "+2 INT, +2 LCK"),

    /**
     * Strong race focused on physical combat.
     */
    DRAGONBORN("Dragonborn", 2, 0, 1, 0, 0, 1, "+2 STR, +1 CON, +1 LCK"),

    /**
     * Fallback race value used for unknown saved data.
     */
    UNKNOWN("Unknown", 0, 0, 0, 0, 0, 0, "None");

    private static final Race[] PLAYABLE_VALUES = {
            HUMAN,
            ELF,
            DWARF,
            TIEFLING,
            DRAGONBORN
    };

    private final String displayName;
    private final int strengthBonus;
    private final int dexterityBonus;
    private final int constitutionBonus;
    private final int intelligenceBonus;
    private final int wisdomBonus;
    private final int luckBonus;
    private final String bonusDescription;

    Race(
            String displayName,
            int strengthBonus,
            int dexterityBonus,
            int constitutionBonus,
            int intelligenceBonus,
            int wisdomBonus,
            int luckBonus,
            String bonusDescription
    ) {
        this.displayName = displayName;
        this.strengthBonus = strengthBonus;
        this.dexterityBonus = dexterityBonus;
        this.constitutionBonus = constitutionBonus;
        this.intelligenceBonus = intelligenceBonus;
        this.wisdomBonus = wisdomBonus;
        this.luckBonus = luckBonus;
        this.bonusDescription = bonusDescription;
    }

    /**
     * Returns only races that can be selected during character creation.
     *
     * @return copy of the playable race array
     */
    public static Race[] playableValues() {
        return PLAYABLE_VALUES.clone();
    }

    /**
     * Finds a race by the text stored in save files or shown in the GUI.
     *
     * @param displayName display name to search for
     * @return matching race, or {@link #UNKNOWN} when no match exists
     */
    public static Race fromDisplayName(String displayName) {
        for (Race race : values()) {
            if (race.displayName.equals(displayName)) {
                return race;
            }
        }

        return UNKNOWN;
    }

    /**
     * Creates the ability score bonuses granted by this race.
     *
     * @return stat object containing only race bonus values
     */
    public Stats createBonusStats() {
        return new Stats(
                strengthBonus,
                dexterityBonus,
                constitutionBonus,
                intelligenceBonus,
                wisdomBonus,
                luckBonus
        );
    }

    /**
     * Returns a compact user-facing description of race bonuses.
     *
     * @return race bonus description
     */
    public String getBonusDescription() {
        return bonusDescription;
    }

    /**
     * Returns the user-facing race name.
     *
     * @return display name used in GUI and save files
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the display name so combo boxes show readable race names.
     *
     * @return user-facing race name
     */
    @Override
    public String toString() {
        return displayName;
    }
}
