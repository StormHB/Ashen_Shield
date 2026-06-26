package ashen.combat;

/**
 * Result of a player damage roll, including text used in the battle log.
 */
public final class DamageRoll {

    private final int damage;
    private final String formula;
    private final String description;

    public DamageRoll(int damage, String formula, String description) {
        this.damage = damage;
        this.formula = formula;
        this.description = description;
    }

    public int getDamage() {
        return damage;
    }

    public String getFormula() {
        return formula;
    }

    public String getDescription() {
        return description;
    }
}
