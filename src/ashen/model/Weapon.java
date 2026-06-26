package ashen.model;

/**
 * Player weapon choices.
 */
public enum Weapon {
    LONGSWORD_SHIELD("Longsword + Shield", 8, "1d8 + STR, +2 AC", "Damage: 1d8 + STR | Shield: +2 AC"),
    GREATSWORD("Greatsword", 12, "1d12 + STR", "Damage: 1d12 + STR | Two-handed | No shield"),
    SCIMITAR_DAGGER("Scimitar + Dagger", 6, "1d6 + DEX + 1d4", "Damage: 1d6 + DEX + 1d4 off-hand"),
    DUAL_DAGGERS("Dual Daggers", 4, "1d4 + DEX + 1d4, +2 Attack Bonus", "Damage: 1d4 + DEX + 1d4 off-hand | +2 Attack Bonus"),
    ROD_SPELLBOOK("Rod + Spellbook", 12, "Fireball 1d12 + INT, +2 INT", "Fireball: 1d12 + INT | Spellbook: +2 INT"),
    QUARTERSTAFF("Quarterstaff", 10, "1d10 + WIS", "Damage: 1d10 + WIS | Two-handed"),
    LONGBOW("Longbow", 10, "1d10 + DEX", "Damage: 1d10 + DEX | Poison Arrows"),
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

    public static Weapon fromDisplayName(String displayName) {
        for (Weapon weapon : values()) {
            if (weapon.displayName.equals(displayName)) {
                return weapon;
            }
        }

        return UNKNOWN;
    }

    public int getDamageDice() {
        return damageDice;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public String getTooltip() {
        return tooltip;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
