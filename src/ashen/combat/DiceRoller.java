package ashen.combat;

import java.util.Random;

/**
 * Rolls dice used by combat rules.
 */
public final class DiceRoller {

    private final Random random = new Random();

    /**
     * Rolls one die with the requested number of sides.
     *
     * @param sides number of die sides
     * @return random value from 1 to sides
     * @throws IllegalArgumentException if sides is less than one
     */
    public int roll(int sides) {
        if (sides <= 0) {
            throw new IllegalArgumentException("Die must have at least one side.");
        }

        return random.nextInt(sides) + 1;
    }
}
