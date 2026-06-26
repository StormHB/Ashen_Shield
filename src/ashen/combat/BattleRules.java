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

    public static int calculateAttackBonus(GameCharacter character) {
        int proficiencyBonus = 2;
        int attackBonus = proficiencyBonus + calculateAbilityModifierForAttack(character);

        if (character.getWeaponType() == Weapon.DUAL_DAGGERS) {
            attackBonus += 2;
        }

        return attackBonus;
    }

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

    public static int getWeaponDamageDice(GameCharacter character) {
        return character.getWeaponType().getDamageDice();
    }

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

    public static int applyDifficultyHp(GameCharacter character, int hp) {
        if (character.hasHardcoreHpBonus()) {
            return (int) Math.round(hp * 1.25);
        }

        return hp;
    }

    public static int applyDifficultyDamage(GameCharacter character, int damage) {
        if (character.hasHardcoreDamageBonus()) {
            return (int) Math.round(damage * 1.25);
        }

        return damage;
    }

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

    public static String getRaceBonusDescription(GameCharacter character) {
        return character.getRaceType().getBonusDescription();
    }

    public static String getWeaponDamageDescription(GameCharacter character) {
        return character.getWeaponType().getDamageDescription();
    }

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
