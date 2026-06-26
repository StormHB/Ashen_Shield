package ashen.combat;

import ashen.model.Enemy;
import ashen.model.GameCharacter;
import ashen.model.CharacterClass;
import ashen.model.Difficulty;
import ashen.model.Stats;
import ashen.model.Weapon;

/**
 * Shared combat calculations and text descriptions.
 */
public final class BattleRules {

    private BattleRules() {
    }

    /**
     * Calculates the ability modifier used for player attacks.
     *
     * @param character attacking character
     * @return relevant ability modifier for the character class
     */
    public static int calculateAbilityModifierForAttack(GameCharacter character) {
        CharacterClass characterClass = character.getCharacterClassType();

        if (characterClass == CharacterClass.FIGHTER) {
            return Stats.calculateModifier(character.getStats().getStrength());
        }

        if (characterClass == CharacterClass.ROGUE || characterClass == CharacterClass.RANGER) {
            return Stats.calculateModifier(character.getStats().getDexterity());
        }

        if (characterClass == CharacterClass.WIZARD) {
            return Stats.calculateModifier(character.getStats().getIntelligence());
        }

        if (characterClass == CharacterClass.DRUID) {
            return Stats.calculateModifier(character.getStats().getWisdom());
        }

        return 0;
    }

    /**
     * Calculates the player's full attack bonus.
     *
     * @param character attacking character
     * @return proficiency, ability and weapon attack bonus
     */
    public static int calculateAttackBonus(GameCharacter character) {
        int proficiencyBonus = 2;
        int attackBonus = proficiencyBonus + calculateAbilityModifierForAttack(character);

        if (character.getWeaponType() == Weapon.DUAL_DAGGERS) {
            attackBonus += 2;
        }

        return attackBonus;
    }

    /**
     * Calculates the player's armor class.
     *
     * @param character character whose armor class is calculated
     * @return final armor class including equipment and class bonuses
     */
    public static int calculateArmorClass(GameCharacter character) {
        int ac = character.getArmorType().getBaseArmorClass();

        if (character.getCharacterClassType() == CharacterClass.DRUID) {
            ac += 2;
        }

        if (character.getWeaponType() == Weapon.LONGSWORD_SHIELD) {
            ac += 2;
        }

        return ac;
    }

    /**
     * Returns the die size used by the equipped weapon.
     *
     * @param character character carrying the weapon
     * @return damage die size
     */
    public static int getWeaponDamageDice(GameCharacter character) {
        return character.getWeaponType().getDamageDice();
    }

    /**
     * Returns the abbreviation of the main attack stat.
     *
     * @param character character whose main stat is requested
     * @return stat abbreviation used in battle log formulas
     */
    public static String getMainStatName(GameCharacter character) {
        switch (character.getCharacterClassType()) {
            case FIGHTER:
                return "STR";
            case ROGUE:
            case RANGER:
                return "DEX";
            case WIZARD:
                return "INT";
            case DRUID:
                return "WIS";
            default:
                return "Modifier";
        }
    }

    /**
     * Applies hardcore HP scaling to enemy HP when enabled.
     *
     * @param character player character with difficulty settings
     * @param hp base enemy HP
     * @return adjusted enemy HP
     */
    public static int applyDifficultyHp(GameCharacter character, int hp) {
        if (character.hasHardcoreHpBonus()) {
            return (int) Math.round(hp * 1.25);
        }

        return hp;
    }

    /**
     * Applies hardcore damage scaling to enemy damage when enabled.
     *
     * @param character player character with difficulty settings
     * @param damage base enemy damage
     * @return adjusted enemy damage
     */
    public static int applyDifficultyDamage(GameCharacter character, int damage) {
        if (character.hasHardcoreDamageBonus()) {
            return (int) Math.round(damage * 1.25);
        }

        return damage;
    }

    /**
     * Calculates the flat damage modifier used by an enemy.
     *
     * @param enemy enemy whose modifier is calculated
     * @return enemy damage modifier
     */
    public static int calculateEnemyDamageModifier(Enemy enemy) {
        if ("Goblin".equals(enemy.getName())) {
            return 1;
        }

        if ("Skeleton".equals(enemy.getName())) {
            return 1;
        }

        if ("Orc".equals(enemy.getName())) {
            return 2;
        }

        if ("Hobgoblin".equals(enemy.getName())) {
            return 2;
        }

        if ("Young Dragon".equals(enemy.getName())) {
            return 4;
        }

        return 1;
    }

    /**
     * Returns the race bonus description for a character.
     *
     * @param character character whose race bonus is described
     * @return race bonus description
     */
    public static String getRaceBonusDescription(GameCharacter character) {
        return character.getRaceType().getBonusDescription();
    }

    /**
     * Returns the weapon damage description for a character.
     *
     * @param character character whose weapon is described
     * @return weapon damage description
     */
    public static String getWeaponDamageDescription(GameCharacter character) {
        return character.getWeaponType().getDamageDescription();
    }

    /**
     * Returns a readable difficulty description including hardcore modifiers.
     *
     * @param character character whose difficulty settings are described
     * @return display text for difficulty settings
     */
    public static String getDifficultyDescription(GameCharacter character) {
        if (character.getDifficultyType() != Difficulty.HARDCORE) {
            return Difficulty.NORMAL.getDisplayName();
        }

        String description = Difficulty.HARDCORE.getDisplayName();

        if (character.hasHardcoreHpBonus()) {
            description += " (+25% enemy HP)";
        }

        if (character.hasHardcoreDamageBonus()) {
            description += " (+25% enemy damage)";
        }

        return description;
    }
}
