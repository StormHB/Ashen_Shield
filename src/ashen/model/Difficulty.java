package ashen.model;

/**
 * Campaign difficulty mode.
 */
public enum Difficulty {
    /**
     * Standard campaign difficulty.
     */
    NORMAL("Normal"),

    /**
     * Harder campaign difficulty with optional enemy bonuses.
     */
    HARDCORE("Hardcore");

    private final String displayName;

    Difficulty(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Finds a difficulty value by the text stored in save files or shown in the GUI.
     *
     * @param displayName display name to search for
     * @return matching difficulty, or {@link #NORMAL} when no match exists
     */
    public static Difficulty fromDisplayName(String displayName) {
        for (Difficulty difficulty : values()) {
            if (difficulty.displayName.equals(displayName)) {
                return difficulty;
            }
        }

        return NORMAL;
    }

    /**
     * Returns the user-facing difficulty name.
     *
     * @return display name used in GUI and save files
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the display name so controls show readable difficulty names.
     *
     * @return user-facing difficulty name
     */
    @Override
    public String toString() {
        return displayName;
    }
}
