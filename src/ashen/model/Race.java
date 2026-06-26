package ashen.model;

/**
 * Playable character races.
 */
public enum Race {
    HUMAN("Human", 1, 1, 1, 0, 0, 1, "+1 STR, +1 DEX, +1 CON, +1 LCK"),
    ELF("Elf", 0, 2, 1, 0, 0, 1, "+2 DEX, +1 CON, +1 LCK"),
    DWARF("Dwarf", 1, 0, 1, 0, 2, 0, "+1 STR, +1 CON, +2 WIS"),
    TIEFLING("Tiefling", 0, 0, 0, 2, 0, 2, "+2 INT, +2 LCK"),
    DRAGONBORN("Dragonborn", 2, 0, 1, 0, 0, 1, "+2 STR, +1 CON, +1 LCK"),
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

    public static Race[] playableValues() {
        return PLAYABLE_VALUES.clone();
    }

    public static Race fromDisplayName(String displayName) {
        for (Race race : values()) {
            if (race.displayName.equals(displayName)) {
                return race;
            }
        }

        return UNKNOWN;
    }

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

    public String getBonusDescription() {
        return bonusDescription;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
