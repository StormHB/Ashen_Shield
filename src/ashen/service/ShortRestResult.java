package ashen.service;

/**
 * Result of a short rest action.
 */
public final class ShortRestResult {

    private final int hpBefore;
    private final int hpAfter;

    public ShortRestResult(int hpBefore, int hpAfter) {
        this.hpBefore = hpBefore;
        this.hpAfter = hpAfter;
    }

    public int getHpBefore() {
        return hpBefore;
    }

    public int getHpAfter() {
        return hpAfter;
    }

    public int getRecoveredHp() {
        return hpAfter - hpBefore;
    }

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
