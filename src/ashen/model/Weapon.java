package ashen.model;

/**
 * Player weapon choices.
 */
public enum Weapon {
    /**
     * Fighter weapon set with shield armor class bonus.
     */
    LONGSWORD_SHIELD("Longsword + Shield", 8, "1d8 + STR, +2 AC", "Damage: 1d8 + STR | Shield: +2 AC"),

    /**
     * Heavy fighter weapon with a large strength damage die.
     */
    GREATSWORD("Greatsword", 12, "1d12 + STR", "Damage: 1d12 + STR | Two-handed | No shield"),

    /**
     * Rogue weapon set with an off-hand dagger.
     */
    SCIMITAR_DAGGER("Scimitar + Dagger", 6, "1d6 + DEX + 1d4", "Damage: 1d6 + DEX + 1d4 off-hand"),

    /**
     * Rogue weapon set that trades smaller dice for attack accuracy.
     */
    DUAL_DAGGERS("Dual Daggers", 4, "1d4 + DEX + 1d4, +2 Attack Bonus", "Damage: 1d4 + DEX + 1d4 off-hand | +2 Attack Bonus"),

    /**
     * Wizard weapon set that improves intelligence.
     */
    ROD_SPELLBOOK("Rod + Spellbook", 12, "Fireball 1d12 + INT, +2 INT", "Fireball: 1d12 + INT | Spellbook: +2 INT"),

    /**
     * Druid weapon that uses wisdom for damage.
     */
    QUARTERSTAFF("Quarterstaff", 10, "1d10 + WIS", "Damage: 1d10 + WIS | Two-handed"),

    /**
     * Ranger weapon that uses dexterity for ranged damage.
     */
    LONGBOW("Longbow", 10, "1d10 + DEX", "Damage: 1d10 + DEX | Poison Arrows"),

    /**
     * Fallback weapon value used for unknown saved data.
     */
    UNKNOWN("Unknown", 4, "?", null);

    private final String displayName;
    private final int damageDice;
    private final String damageDescription;
    private final String tooltip;

    Weapon(String displayName, int damageDice, String damageDescription, String tooltip) {
        this.displayName = displayName;
        this.damageDice = damageDice;
        this.damageDescription = damageDescription;
        this.tooltip = tooltip;
    }

    /**
     * Finds a weapon by the text stored in save files or shown in the GUI.
     *
     * @param displayName display name to search for
     * @return matching weapon, or {@link #UNKNOWN} when no match exists
     */
    public static Weapon fromDisplayName(String displayName) {
        for (Weapon weapon : values()) {
            if (weapon.displayName.equals(displayName)) {
                return weapon;
            }
        }

        return UNKNOWN;
    }

    /**
     * Returns the die size used for this weapon's base damage roll.
     *
     * @return damage die size
     */
    public int getDamageDice() {
        return damageDice;
    }

    /**
     * Returns a compact description of this weapon's damage formula.
     *
     * @return damage description shown on character sheets
     */
    public String getDamageDescription() {
        return damageDescription;
    }

    /**
     * Returns text shown as a tooltip in the weapon combo box.
     *
     * @return weapon tooltip text, or null when no tooltip is available
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * Returns the user-facing weapon name.
     *
     * @return display name used in GUI and save files
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the display name so combo boxes show readable weapon names.
     *
     * @return user-facing weapon name
     */
    @Override
    public String toString() {
        return displayName;
    }
}
