package ashen.service;

/**
 * Result of a short rest action.
 */
public final class ShortRestResult {

    private final int hpBefore;
    private final int hpAfter;

    /**
     * Creates a result object for a completed short rest.
     *
     * @param hpBefore HP before resting
     * @param hpAfter HP after resting
     */
    public ShortRestResult(int hpBefore, int hpAfter) {
        this.hpBefore = hpBefore;
        this.hpAfter = hpAfter;
    }

    /**
     * Returns HP before the rest.
     *
     * @return starting HP
     */
    public int getHpBefore() {
        return hpBefore;
    }

    /**
     * Returns HP after the rest.
     *
     * @return final HP
     */
    public int getHpAfter() {
        return hpAfter;
    }

    /**
     * Calculates HP recovered during the rest.
     *
     * @return recovered HP amount
     */
    public int getRecoveredHp() {
        return hpAfter - hpBefore;
    }

    /**
     * Formats the rest result for the campaign battle log.
     *
     * @return battle log entry describing the rest
     */
    public String toBattleLogEntry() {
        return "\nShort Rest\n"
                + "Recovered "
                + getRecoveredHp()
                + " HP ("
                + hpBefore
                + " -> "
                + hpAfter
                + ")\n\n";
    }
}
