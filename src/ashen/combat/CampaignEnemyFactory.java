package ashen.combat;

import ashen.model.Enemy;
import ashen.model.GameCharacter;

/**
 * Creates the ordered enemy list for a campaign run.
 */
public final class CampaignEnemyFactory {

    private CampaignEnemyFactory() {
    }

    public static Enemy[] createEnemies(GameCharacter character) {
        return new Enemy[]{
                new Enemy("Goblin", BattleRules.applyDifficultyHp(character, 10), 12, 2),
                new Enemy("Skeleton", BattleRules.applyDifficultyHp(character, 15), 13, 3),
                new Enemy("Orc", BattleRules.applyDifficultyHp(character, 22), 14, 3),
                new Enemy("Hobgoblin", BattleRules.applyDifficultyHp(character, 30), 15, 4),
                new Enemy("Young Dragon", BattleRules.applyDifficultyHp(character, 40), 16, 5)
        };
    }
}
