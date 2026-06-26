package ashen.combat.event;

/**
 * Listener used by the battle controller to publish battle events.
 */
public interface BattleEventListener {

    void appendBattleLog(String text);

    void updatePlayerHp(int currentHp, int maxHp);

    void updateEnemyHp(int currentHp, int maxHp);

    void enemyDefeated();

    void playerDefeated();
}
