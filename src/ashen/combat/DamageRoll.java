package ashen.combat;

/**
 * Result of a player damage roll, including text used in the battle log.
 */
public final class DamageRoll {

    private final int damage;
    private final String formula;
    private final String description;

    /**
     * Creates a completed damage roll result.
     *
     * @param damage numeric damage result
     * @param formula formula without the final result
     * @param description complete display text for the damage roll
     */
    public DamageRoll(int damage, String formula, String description) {
        this.damage = damage;
        this.formula = formula;
        this.description = description;
    }

    /**
     * Returns the numeric damage result.
     *
     * @return damage amount
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Returns the roll formula before the final result.
     *
     * @return damage formula text
     */
    public String getFormula() {
        return formula;
    }

    /**
     * Returns the full damage roll description.
     *
     * @return complete damage description
     */
    public String getDescription() {
        return description;
    }
}
