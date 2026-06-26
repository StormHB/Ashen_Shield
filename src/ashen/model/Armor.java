package ashen.model;

/**
 * Player armor choices.
 */
public enum Armor {
    CLOTH_ROBE("Cloth Robe", 10, "AC: 10 | +2 INT"),
    LEATHER_TUNIC("Leather Tunic", 12, "AC: 12 | +2 main stat"),
    LEATHER_ARMOR("Leather Armor", 14, "AC: 14 | +1 main stat"),
    HIDE_ARMOR("Hide Armor", 16, "AC: 16"),
    CHAIN_MAIL("Chain Mail", 15, "AC: 15 | +2 STR"),
    PLATE_ARMOR("Plate Armor", 17, "AC: 17"),
    UNKNOWN("Unknown", 10, null);

    private final String displayName;
    private final int baseArmorClass;
    private final String tooltip;

    Armor(String displayName, int baseArmorClass, String tooltip) {
        this.displayName = displayName;
        this.baseArmorClass = baseArmorClass;
        this.tooltip = tooltip;
    }

    /**
     * Finds an armor value by the text stored in save files or shown in the GUI.
     *
     * @param displayName display name to search for
     * @return matching armor, or {@link #UNKNOWN} when no match exists
     */
    public static Armor fromDisplayName(String displayName) {
        for (Armor armor : values()) {
            if (armor.displayName.equals(displayName)) {
                return armor;
            }
        }

        return UNKNOWN;
    }

    /**
     * Returns the armor class provided before class-specific bonuses.
     *
     * @return base armor class value
     */
    public int getBaseArmorClass() {
        return baseArmorClass;
    }

    /**
     * Returns text shown as a tooltip in the equipment combo box.
     *
     * @return armor tooltip text, or null when no tooltip is available
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * Returns the user-facing armor name.
     *
     * @return display name used in GUI and save files
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the display name so combo boxes show readable armor names.
     *
     * @return user-facing armor name
     */
    @Override
    public String toString() {
        return displayName;
    }
}
