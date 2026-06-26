package ashen.combat.event;

/**
 * Listener used by the battle controller to publish battle events.
 */
public interface BattleEventListener {

    /**
     * Appends text to the battle log display.
     *
     * @param text log text to append
     */
    void appendBattleLog(String text);

    /**
     * Updates the displayed player HP.
     *
     * @param currentHp current player HP
     * @param maxHp maximum player HP
     */
    void updatePlayerHp(int currentHp, int maxHp);

    /**
     * Updates the displayed enemy HP.
     *
     * @param currentHp current enemy HP
     * @param maxHp maximum enemy HP
     */
    void updateEnemyHp(int currentHp, int maxHp);

    /**
     * Handles the current enemy being defeated.
     */
    void enemyDefeated();

    /**
     * Handles the player being defeated.
     */
    void playerDefeated();
}
