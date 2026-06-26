package ashen.combat;

import java.util.Random;

/**
 * Rolls dice used by combat rules.
 */
public final class DiceRoller {

    private final Random random = new Random();

    public int roll(int sides) {
        if (sides <= 0) {
            throw new IllegalArgumentException("Die must have at least one side.");
        }

        return random.nextInt(sides) + 1;
    }
}
