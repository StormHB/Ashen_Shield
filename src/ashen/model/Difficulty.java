package ashen.model;

/**
 * Campaign difficulty mode.
 */
public enum Difficulty {
    NORMAL("Normal"),
    HARDCORE("Hardcore");

    private final String displayName;

    Difficulty(String displayName) {
        this.displayName = displayName;
    }

    public static Difficulty fromDisplayName(String displayName) {
        for (Difficulty difficulty : values()) {
            if (difficulty.displayName.equals(displayName)) {
                return difficulty;
            }
        }

        return NORMAL;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
