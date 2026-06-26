package ashen.creation;

import ashen.model.Ability;
import ashen.model.Stats;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/**
 * Handles point-buy stat allocation and race bonuses during character creation.
 */
public class StatAllocation {

    private static final int STARTING_POINTS = 12;
    private static final int STARTING_STAT_VALUE = 10;
    private static final int MIN_BASE_VALUE = 8;
    private static final int MAX_BASE_VALUE = 18;
    private static final int MAX_TOTAL_VALUE = 20;

    private final Map<Ability, Integer> baseValues = new EnumMap<>(Ability.class);
    private final Map<Ability, Integer> raceBonuses = new EnumMap<>(Ability.class);
    private final Random random = new Random();

    private int pointsRemaining;

    /**
     * Creates a new allocation model with default point-buy values.
     */
    public StatAllocation() {
        reset();
    }

    /**
     * Returns unspent point-buy points.
     *
     * @return remaining allocation points
     */
    public int getPointsRemaining() {
        return pointsRemaining;
    }

    /**
     * Returns the displayed stat value including race bonuses and the maximum cap.
     *
     * @param ability ability score to read
     * @return total stat value
     */
    public int getValue(Ability ability) {
        return Math.min(MAX_TOTAL_VALUE, getBaseValue(ability) + getRaceBonus(ability));
    }

    /**
     * Returns the race bonus applied to one ability score.
     *
     * @param ability ability score to read
     * @return race bonus for the ability
     */
    public int getRaceBonus(Ability ability) {
        return raceBonuses.get(ability);
    }

    /**
     * Increases one base ability score when points and limits allow it.
     *
     * @param ability ability score to increase
     * @return true if the score was increased
     */
    public boolean increase(Ability ability) {
        if (pointsRemaining <= 0) {
            return false;
        }

        int baseValue = getBaseValue(ability);

        if (baseValue >= MAX_BASE_VALUE) {
            return false;
        }

        baseValues.put(ability, baseValue + 1);
        pointsRemaining--;

        return true;
    }

    /**
     * Decreases one base ability score when the minimum value allows it.
     *
     * @param ability ability score to decrease
     * @return true if the score was decreased
     */
    public boolean decrease(Ability ability) {
        int baseValue = getBaseValue(ability);

        if (baseValue <= MIN_BASE_VALUE) {
            return false;
        }

        baseValues.put(ability, baseValue - 1);
        pointsRemaining++;

        return true;
    }

    /**
     * Randomly spends all available point-buy points.
     */
    public void randomize() {
        reset();

        while (pointsRemaining > 0) {
            Ability[] abilities = Ability.values();
            Ability randomAbility = abilities[random.nextInt(abilities.length)];

            increase(randomAbility);
        }
    }

    /**
     * Restores all base values, race bonuses and remaining points to defaults.
     */
    public void reset() {
        pointsRemaining = STARTING_POINTS;

        for (Ability ability : Ability.values()) {
            baseValues.put(ability, STARTING_STAT_VALUE);
            raceBonuses.put(ability, 0);
        }
    }

    /**
     * Applies race bonuses to the allocation model.
     *
     * @param bonuses race bonus values to apply
     */
    public void applyRaceBonuses(Stats bonuses) {
        raceBonuses.put(Ability.STRENGTH, bonuses.getStrength());
        raceBonuses.put(Ability.DEXTERITY, bonuses.getDexterity());
        raceBonuses.put(Ability.CONSTITUTION, bonuses.getConstitution());
        raceBonuses.put(Ability.INTELLIGENCE, bonuses.getIntelligence());
        raceBonuses.put(Ability.WISDOM, bonuses.getWisdom());
        raceBonuses.put(Ability.LUCK, bonuses.getLuck());
    }

    /**
     * Converts current allocation values into a stats object.
     *
     * @return stats containing current total ability values
     */
    public Stats toStats() {
        return new Stats(
                getValue(Ability.STRENGTH),
                getValue(Ability.DEXTERITY),
                getValue(Ability.CONSTITUTION),
                getValue(Ability.INTELLIGENCE),
                getValue(Ability.WISDOM),
                getValue(Ability.LUCK)
        );
    }

    private int getBaseValue(Ability ability) {
        return baseValues.get(ability);
    }
}
