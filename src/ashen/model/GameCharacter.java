package ashen.model;

import java.io.Serializable;

public class GameCharacter implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String race;
    private String characterClass;
    private Stats stats;
    private String weapon;
    private String armor;
    private boolean shield;

    private int maxHp;
    private int currentHp;

    public GameCharacter(String name, String race, String characterClass,
                         Stats stats, String weapon, String armor, boolean shield) {
        this.name = name;
        this.race = race;
        this.characterClass = characterClass;
        this.stats = stats;
        this.weapon = weapon;
        this.armor = armor;
        this.shield = shield;

        this.maxHp = 10 + stats.getConstitution();
        this.currentHp = this.maxHp;
    }

    public String getName() {
        return name;
    }

    public String getRace() {
        return race;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public Stats getStats() {
        return stats;
    }

    public String getWeapon() {
        return weapon;
    }

    public String getArmor() {
        return armor;
    }

    public boolean hasShield() {
        return shield;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void takeDamage(int damage) {
        currentHp -= damage;

        if (currentHp < 0) {
            currentHp = 0;
        }
    }

    public void heal(int amount) {
        currentHp += amount;

        if (currentHp > maxHp) {
            currentHp = maxHp;
        }
    }

    public boolean isDefeated() {
        return currentHp <= 0;
    }
}