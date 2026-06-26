package ashen.creation;

import ashen.model.Armor;
import ashen.model.CharacterClass;
import ashen.model.Race;
import ashen.model.Stats;
import ashen.model.Weapon;

/**
 * Character creation catalog and bonus rules.
 */
public final class CharacterCreationRules {

    private CharacterCreationRules() {
    }

    /**
     * Returns races available to the player.
     *
     * @return selectable race values
     */
    public static Race[] getRaces() {
        return Race.playableValues();
    }

    /**
     * Returns character classes available to the player.
     *
     * @return selectable class values
     */
    public static CharacterClass[] getCharacterClasses() {
        return CharacterClass.playableValues();
    }

    /**
     * Returns stat bonuses granted by the selected race.
     *
     * @param race selected race
     * @return stat object containing race bonus values
     */
    public static Stats getRaceBonuses(Race race) {
        if (race == null) {
            return Race.UNKNOWN.createBonusStats();
        }

        return race.createBonusStats();
    }

    /**
     * Returns text describing the selected race bonuses.
     *
     * @param race selected race
     * @return display text for race bonuses
     */
    public static String getRaceBonusDescription(Race race) {
        if (race == null) {
            return Race.UNKNOWN.getBonusDescription();
        }

        return race.getBonusDescription();
    }

    /**
     * Returns weapons available to a selected class.
     *
     * @param characterClass selected class
     * @return weapon choices for that class
     */
    public static Weapon[] getWeaponsForClass(CharacterClass characterClass) {
        if (characterClass == CharacterClass.FIGHTER) {
            return new Weapon[]{Weapon.LONGSWORD_SHIELD, Weapon.GREATSWORD};
        }

        if (characterClass == CharacterClass.ROGUE) {
            return new Weapon[]{Weapon.SCIMITAR_DAGGER, Weapon.DUAL_DAGGERS};
        }

        if (characterClass == CharacterClass.WIZARD) {
            return new Weapon[]{Weapon.ROD_SPELLBOOK};
        }

        if (characterClass == CharacterClass.DRUID) {
            return new Weapon[]{Weapon.QUARTERSTAFF};
        }

        if (characterClass == CharacterClass.RANGER) {
            return new Weapon[]{Weapon.LONGBOW};
        }

        return new Weapon[0];
    }

    /**
     * Returns armor available to a selected class.
     *
     * @param characterClass selected class
     * @return armor choices for that class
     */
    public static Armor[] getArmorForClass(CharacterClass characterClass) {
        if (characterClass == CharacterClass.FIGHTER) {
            return new Armor[]{Armor.CHAIN_MAIL, Armor.PLATE_ARMOR};
        }

        if (characterClass == CharacterClass.ROGUE) {
            return new Armor[]{Armor.LEATHER_ARMOR, Armor.LEATHER_TUNIC};
        }

        if (characterClass == CharacterClass.WIZARD) {
            return new Armor[]{Armor.CLOTH_ROBE};
        }

        if (characterClass == CharacterClass.DRUID) {
            return new Armor[]{Armor.LEATHER_ARMOR, Armor.HIDE_ARMOR};
        }

        if (characterClass == CharacterClass.RANGER) {
            return new Armor[]{Armor.LEATHER_ARMOR, Armor.LEATHER_TUNIC};
        }

        return new Armor[0];
    }

    /**
     * Applies class and equipment stat bonuses to finalized base stats.
     *
     * @param characterClass selected class
     * @param armor selected armor
     * @param stats stats after point allocation and race bonuses
     * @return new stats object with equipment bonuses applied
     */
    public static Stats applyEquipmentBonuses(CharacterClass characterClass, Armor armor, Stats stats) {
        int strength = stats.getStrength();
        int dexterity = stats.getDexterity();
        int constitution = stats.getConstitution();
        int intelligence = stats.getIntelligence();
        int wisdom = stats.getWisdom();
        int luck = stats.getLuck();

        if (characterClass == CharacterClass.WIZARD) {
            intelligence += 2;
        }

        if (characterClass == CharacterClass.FIGHTER && armor == Armor.CHAIN_MAIL) {
            strength += 2;
        }

        if ((characterClass == CharacterClass.ROGUE || characterClass == CharacterClass.RANGER)
                && armor == Armor.LEATHER_ARMOR) {
            dexterity += 1;
        }

        if ((characterClass == CharacterClass.ROGUE || characterClass == CharacterClass.RANGER)
                && armor == Armor.LEATHER_TUNIC) {
            dexterity += 2;
        }

        if (characterClass == CharacterClass.DRUID && armor == Armor.LEATHER_ARMOR) {
            wisdom += 1;
        }

        return new Stats(strength, dexterity, constitution, intelligence, wisdom, luck);
    }

    /**
     * Returns the class description shown in the character creation panel.
     *
     * @param characterClass selected class
     * @return description text for the selected class
     */
    public static String getClassDescription(CharacterClass characterClass) {
        if (characterClass == CharacterClass.FIGHTER) {
            return "Primary Stat: Strength\n" +
                    "Recommended Stats: STR, CON\n\n" +
                    "Class Ability: Weapon Mastery\n" +
                    "Effect: Can choose between defensive and offensive weapon setups.";
        }

        if (characterClass == CharacterClass.ROGUE) {
            return "Primary Stat: Dexterity\n" +
                    "Recommended Stats: DEX, CON\n\n" +
                    "Class Ability: Sneak Attack\n" +
                    "Effect: First attack each battle gains +1d8 damage.";
        }

        if (characterClass == CharacterClass.WIZARD) {
            return "Primary Stat: Intelligence\n" +
                    "Recommended Stats: INT, CON\n\n" +
                    "Class Ability: Arcane Precision\n" +
                    "Effect: Reroll attack rolls of 5 or lower.\n\n" +
                    "Equipment Bonus: +2 INT from Spellbook.";
        }

        if (characterClass == CharacterClass.DRUID) {
            return "Primary Stat: Wisdom\n" +
                    "Recommended Stats: WIS, CON\n\n" +
                    "Class Ability: Mark of the Wild\n" +
                    "Effect: Gain +2 Armor Class.";
        }

        if (characterClass == CharacterClass.RANGER) {
            return "Primary Stat: Dexterity\n" +
                    "Recommended Stats: DEX, CON\n\n" +
                    "Class Ability: Poison Arrows\n" +
                    "Effect: Successful hits add stacking poison damage.\n" +
                    "Poison triggers even if future attacks miss.";
        }

        return "";
    }

    /**
     * Returns tooltip text for a selected weapon.
     *
     * @param weapon selected weapon
     * @return weapon tooltip text, or null if no weapon is selected
     */
    public static String getWeaponTooltip(Weapon weapon) {
        if (weapon == null) {
            return null;
        }

        return weapon.getTooltip();
    }

    /**
     * Returns tooltip text for a selected armor item.
     *
     * @param armor selected armor
     * @return armor tooltip text, or null if no armor is selected
     */
    public static String getArmorTooltip(Armor armor) {
        if (armor == null) {
            return null;
        }

        return armor.getTooltip();
    }
}
