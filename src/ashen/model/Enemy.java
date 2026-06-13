package ashen.model;

import java.io.Serializable;

public class Enemy implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private int maxHp;
    private int currentHp;
    private int armorClass;
    private int attackBonus;

    public Enemy(String name, int maxHp, int armorClass, int attackBonus) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.armorClass = armorClass;
        this.attackBonus = attackBonus;
    }

    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getArmorClass() {
        return armorClass;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public void takeDamage(int damage) {
        currentHp -= damage;

        if (currentHp < 0) {
            currentHp = 0;
        }
    }

    public boolean isDefeated() {
        return currentHp <= 0;
    }
}