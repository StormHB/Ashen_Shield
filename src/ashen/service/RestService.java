package ashen.service;

import ashen.model.GameCharacter;

/**
 * Utility class for rest and healing rules.
 */
public final class RestService {

    private RestService() {
    }

    /**
     * Restores part of the character's missing HP.
     *
     * @param character character that takes a short rest
     * @return result containing HP before and after the rest
     */
    public static ShortRestResult shortRest(GameCharacter character) {
        int hpBefore = character.getCurrentHp();
        int missingHp = character.getMaxHp() - character.getCurrentHp();
        int healing = (missingHp + 1) / 2;

        if (healing < 4 && missingHp > 0) {
            healing = 4;
        }

        character.heal(healing);

        return new ShortRestResult(hpBefore, character.getCurrentHp());
    }
}
