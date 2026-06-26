package ashen.combat;

import ashen.combat.event.BattleEventListener;
import ashen.model.Enemy;
import ashen.model.CharacterClass;
import ashen.model.GameCharacter;

/**
 * Controls one battle by applying combat rules and notifying battle event listeners.
 */
public class BattleController {

    private final GameCharacter character;
    private final Enemy[] enemies;
    private final int currentEnemyIndex;
    private final Enemy enemy;
    private final BattleEventListener listener;
    private final DiceRoller diceRoller;

    private boolean playerDefeated;
    private boolean rogueSneakAttackUsed;
    private int rangerPoisonDamage;

    public BattleController(GameCharacter character, int enemyIndex, BattleEventListener listener) {
        this(character, enemyIndex, listener, new DiceRoller());
    }

    BattleController(GameCharacter character, int enemyIndex, BattleEventListener listener, DiceRoller diceRoller) {
        this.character = character;
        this.enemies = CampaignEnemyFactory.createEnemies(character);
        this.currentEnemyIndex = enemyIndex;
        this.listener = listener;
        this.diceRoller = diceRoller;

        if (enemyIndex < 0 || enemyIndex >= enemies.length) {
            throw new IllegalArgumentException("Unknown enemy index: " + enemyIndex);
        }

        this.enemy = enemies[currentEnemyIndex];
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public int getCurrentEnemyIndex() {
        return currentEnemyIndex;
    }

    public int getEnemyCount() {
        return enemies.length;
    }

    public boolean isPlayerDefeated() {
        return playerDefeated;
    }

    public void playerAttack(boolean enemyResponds) {
        if (character.getCharacterClassType() == CharacterClass.ROGUE && !rogueSneakAttackUsed) {
            rogueSneakAttackUsed = true;

            DamageRoll baseDamage = PlayerDamageCalculator.rollDamage(character, diceRoller);
            int sneakRoll = diceRoller.roll(8);
            int damage = Math.max(1, baseDamage.getDamage() + sneakRoll);

            enemy.takeDamage(damage);
            updateEnemyHpLabel();

            listener.appendBattleLog(character.getName() + " uses Sneak Attack!\n");
            listener.appendBattleLog("Automatic Hit!\n");
            listener.appendBattleLog(
                    "Damage Roll: "
                            + baseDamage.getFormula()
                            + " + Sneak Attack "
                            + sneakRoll
                            + " = "
                            + damage
                            + "\n"
            );

            listener.appendBattleLog(enemy.getName() + " HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + "\n");

            checkEnemyDefeated();

            if (!enemy.isDefeated() && enemyResponds) {
                listener.appendBattleLog("\n");
                enemyTurn();
            } else {
                listener.appendBattleLog("\n");
            }

            return;
        }

        int d20Roll = diceRoller.roll(20);

        if (character.getCharacterClassType() == CharacterClass.WIZARD && d20Roll <= 5) {
            int oldRoll = d20Roll;
            d20Roll = diceRoller.roll(20);

            listener.appendBattleLog(
                    "Arcane Precision: rerolled "
                            + oldRoll
                            + " into "
                            + d20Roll
                            + ".\n"
            );
        }

        int attackBonus = BattleRules.calculateAttackBonus(character);
        int totalAttack = d20Roll + attackBonus;

        if (character.getCharacterClassType() == CharacterClass.WIZARD) {
            listener.appendBattleLog(character.getName() + " casts Fireball at " + enemy.getName() + ".\n");
        } else {
            listener.appendBattleLog(character.getName() + " attacks " + enemy.getName() + ".\n");
        }

        if (d20Roll == 1) {
            int selfDamage = diceRoller.roll(4);

            damagePlayer(selfDamage);

            listener.appendBattleLog("Natural 1! Critical Miss!\n");
            listener.appendBattleLog(character.getName() + " takes " + selfDamage + " self-damage.\n");
            listener.appendBattleLog(character.getName() + " HP: " + character.getCurrentHp() + "/" + character.getMaxHp() + "\n\n");

            checkPlayerDefeated();

            if (!character.isDefeated() && enemyResponds) {
                enemyTurn();
            }

            return;
        }

        if (d20Roll == 20) {
            int weaponDice = BattleRules.getWeaponDamageDice(character);
            int firstRoll = diceRoller.roll(weaponDice);
            int secondRoll = diceRoller.roll(weaponDice);
            int damageModifier = BattleRules.calculateAbilityModifierForAttack(character);
            int damage = Math.max(1, firstRoll + secondRoll + damageModifier);

            enemy.takeDamage(damage);
            updateEnemyHpLabel();

            addRangerPoisonOnHit();

            listener.appendBattleLog("Natural 20! Critical Hit!\n");
            listener.appendBattleLog("Critical Damage Roll: " + firstRoll + " + " + secondRoll + " + " + damageModifier + " = " + damage + "\n");
            listener.appendBattleLog(enemy.getName() + " HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + "\n");

            checkEnemyDefeated();

            if (!enemy.isDefeated()) {
                applyRangerPoison();
            }

            if (!enemy.isDefeated() && enemyResponds) {
                listener.appendBattleLog("\n");
                enemyTurn();
            } else {
                listener.appendBattleLog("\n");
            }

            return;
        }

        listener.appendBattleLog("Attack Roll: " + d20Roll + " + " + attackBonus + " = " + totalAttack + "\n");

        if (totalAttack >= enemy.getArmorClass()) {
            DamageRoll damageRoll = PlayerDamageCalculator.rollDamage(character, diceRoller);
            int damage = Math.max(1, damageRoll.getDamage());

            listener.appendBattleLog("Hit!\n");
            listener.appendBattleLog("Damage Roll: " + damageRoll.getDescription() + "\n");

            enemy.takeDamage(damage);
            updateEnemyHpLabel();

            addRangerPoisonOnHit();

            listener.appendBattleLog(enemy.getName() + " HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + "\n");

            checkEnemyDefeated();

            if (!enemy.isDefeated()) {
                applyRangerPoison();
            }

            if (!enemy.isDefeated() && enemyResponds) {
                listener.appendBattleLog("\n");
                enemyTurn();
            } else {
                listener.appendBattleLog("\n");
            }
        } else {
            listener.appendBattleLog("Miss!\n");

            applyRangerPoison();

            if (!enemy.isDefeated() && enemyResponds) {
                listener.appendBattleLog("\n");
                enemyTurn();
            } else {
                listener.appendBattleLog("\n");
            }
        }
    }

    private void addRangerPoisonOnHit() {
        if (character.getCharacterClassType() == CharacterClass.RANGER) {
            int poisonRoll = diceRoller.roll(2);
            rangerPoisonDamage += poisonRoll;

            listener.appendBattleLog(
                    "Poison Arrow: +"
                            + poisonRoll
                            + " poison damage. Total poison: "
                            + rangerPoisonDamage
                            + "\n"
            );
        }
    }

    private void applyRangerPoison() {
        if (character.getCharacterClassType() != CharacterClass.RANGER) {
            return;
        }

        if (rangerPoisonDamage <= 0) {
            return;
        }

        enemy.takeDamage(rangerPoisonDamage);
        updateEnemyHpLabel();

        listener.appendBattleLog(
                "Poison Arrows deal "
                        + rangerPoisonDamage
                        + " poison damage.\n"
        );

        listener.appendBattleLog(
                enemy.getName()
                        + " HP: "
                        + enemy.getCurrentHp()
                        + "/"
                        + enemy.getMaxHp()
                        + "\n"
        );

        checkEnemyDefeated();
    }

    private void enemyTurn() {
        int d20Roll = diceRoller.roll(20);
        int attackBonus = enemy.getAttackBonus();
        int totalAttack = d20Roll + attackBonus;

        listener.appendBattleLog(enemy.getName() + " attacks " + character.getName() + ".\n");
        listener.appendBattleLog("Attack Roll: " + d20Roll + " + " + attackBonus + " = " + totalAttack + "\n");

        if (d20Roll == 1) {
            int selfDamage = diceRoller.roll(4);

            enemy.takeDamage(selfDamage);
            updateEnemyHpLabel();

            listener.appendBattleLog("Natural 1! Critical Miss!\n");
            listener.appendBattleLog(enemy.getName() + " takes " + selfDamage + " self-damage.\n");
            listener.appendBattleLog(enemy.getName() + " HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + "\n");

            checkEnemyDefeated();

            listener.appendBattleLog("\n");
            return;
        }

        if (d20Roll == 20) {
            int firstRoll = diceRoller.roll(6);
            int secondRoll = diceRoller.roll(6);
            int damageModifier = enemy.getAttackBonus();
            int baseDamage = firstRoll + secondRoll + damageModifier;
            int damage = BattleRules.applyDifficultyDamage(character, baseDamage);

            damagePlayer(damage);

            listener.appendBattleLog("Natural 20! Critical Hit!\n");
            if (character.hasHardcoreDamageBonus()) {
                listener.appendBattleLog(
                        "Critical Damage Roll: "
                                + firstRoll
                                + " + "
                                + secondRoll
                                + " + "
                                + damageModifier
                                + " = "
                                + baseDamage
                                + "\n"
                );

                listener.appendBattleLog(
                        "Total Damage (Hardcore bonus): "
                                + damage
                                + "\n"
                );
            } else {
                listener.appendBattleLog(
                        "Critical Damage Roll: "
                                + firstRoll
                                + " + "
                                + secondRoll
                                + " + "
                                + damageModifier
                                + " = "
                                + damage
                                + "\n"
                );
            }

            listener.appendBattleLog(character.getName() + " HP: " + character.getCurrentHp() + "/" + character.getMaxHp() + "\n");

            checkPlayerDefeated();
            listener.appendBattleLog("\n");
            return;
        }

        if (totalAttack >= BattleRules.calculateArmorClass(character)) {
            int damageRoll = diceRoller.roll(6);
            int damageModifier = BattleRules.calculateEnemyDamageModifier(enemy);
            int baseDamage = damageRoll + damageModifier;
            int damage = BattleRules.applyDifficultyDamage(character, baseDamage);

            damagePlayer(damage);

            listener.appendBattleLog("Hit!\n");

            if (character.hasHardcoreDamageBonus()) {
                listener.appendBattleLog(
                        "Damage Roll: "
                                + damageRoll
                                + " + "
                                + damageModifier
                                + " = "
                                + baseDamage
                                + "\n"
                );

                listener.appendBattleLog(
                        "Total Damage (Hardcore bonus): "
                                + damage
                                + "\n"
                );
            } else {
                listener.appendBattleLog(
                        "Damage Roll: "
                                + damageRoll
                                + " + "
                                + damageModifier
                                + " = "
                                + damage
                                + "\n"
                );
            }

            listener.appendBattleLog(character.getName() + " HP: " + character.getCurrentHp() + "/" + character.getMaxHp() + "\n");

            checkPlayerDefeated();
            listener.appendBattleLog("\n");
        } else {
            listener.appendBattleLog("Miss!\n\n");
        }
    }

    private void damagePlayer(int damage) {
        character.takeDamage(damage);
        updatePlayerHpLabel();
    }

    private void updatePlayerHpLabel() {
        listener.updatePlayerHp(character.getCurrentHp(), character.getMaxHp());
    }

    private void updateEnemyHpLabel() {
        listener.updateEnemyHp(enemy.getCurrentHp(), enemy.getMaxHp());
    }

    private void checkEnemyDefeated() {
        if (enemy.isDefeated()) {
            listener.appendBattleLog(enemy.getName() + " has been defeated!\n");
            listener.enemyDefeated();
        }
    }

    private void checkPlayerDefeated() {
        if (character.isDefeated()) {
            listener.appendBattleLog(
                    character.getName()
                            + " has been defeated!\n"
            );

            playerDefeated = true;
            listener.playerDefeated();
        }
    }
}
