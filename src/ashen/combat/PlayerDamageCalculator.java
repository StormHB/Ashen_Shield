package ashen.combat;

import ashen.model.GameCharacter;

/**
 * Calculates player weapon damage rolls.
 */
public final class PlayerDamageCalculator {

    private PlayerDamageCalculator() {
    }

    /**
     * Rolls player weapon damage and prepares battle log text for the roll.
     *
     * @param character attacking character
     * @param diceRoller dice roller used for random values
     * @return damage result with formula text
     */
    public static DamageRoll rollDamage(GameCharacter character, DiceRoller diceRoller) {
        int modifier = BattleRules.calculateAbilityModifierForAttack(character);
        String statName = BattleRules.getMainStatName(character);

        switch (character.getWeaponType()) {
            case SCIMITAR_DAGGER: {
                int mainHandRoll = diceRoller.roll(6);
                int offHandRoll = diceRoller.roll(4);
                int damage = mainHandRoll + modifier + offHandRoll;
                String formula = "Main Hand "
                        + mainHandRoll
                        + " + "
                        + statName
                        + " "
                        + modifier
                        + " + Off Hand "
                        + offHandRoll;

                return new DamageRoll(damage, formula, formula + " = " + damage);
            }

            case DUAL_DAGGERS: {
                int mainHandRoll = diceRoller.roll(4);
                int offHandRoll = diceRoller.roll(4);
                int damage = mainHandRoll + modifier + offHandRoll;
                String formula = "Main Hand "
                        + mainHandRoll
                        + " + "
                        + statName
                        + " "
                        + modifier
                        + " + Off Hand "
                        + offHandRoll;

                return new DamageRoll(damage, formula, formula + " = " + damage);
            }

            case ROD_SPELLBOOK: {
                int spellRoll = diceRoller.roll(BattleRules.getWeaponDamageDice(character));
                int damage = spellRoll + modifier;
                String formula = "Fireball "
                        + spellRoll
                        + " + "
                        + statName
                        + " "
                        + modifier;

                return new DamageRoll(damage, formula, formula + " = " + damage);
            }

            default: {
                int weaponRoll = diceRoller.roll(BattleRules.getWeaponDamageDice(character));
                int damage = weaponRoll + modifier;
                String formula = "Weapon "
                        + weaponRoll
                        + " + "
                        + statName
                        + " "
                        + modifier;

                return new DamageRoll(damage, formula, formula + " = " + damage);
            }
        }
    }
}
