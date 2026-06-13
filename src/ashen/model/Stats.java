package ashen.model;

import java.io.Serializable;

public class Stats implements Serializable {

    private static final long serialVersionUID = 1L;

    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int luck;

    public Stats(int strength, int dexterity, int constitution,
                 int intelligence, int wisdom, int luck) {
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.luck = luck;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getConstitution() {
        return constitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public int getLuck() {
        return luck;
    }
}