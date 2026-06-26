package ashen.service;

import ashen.model.GameCharacter;

import java.io.IOException;

/**
 * Abstraction for saving and loading high scores.
 */
public interface HighScoreProvider {

    /**
     * Score category for normal difficulty.
     */
    String NORMAL = "NORMAL";

    /**
     * Score category for hardcore mode with enemy HP bonus enabled.
     */
    String HARDCORE_HP = "HARDCORE_HP";

    /**
     * Score category for hardcore mode with enemy damage bonus enabled.
     */
    String HARDCORE_DAMAGE = "HARDCORE_DAMAGE";

    /**
     * Score category for hardcore mode with all enemy bonuses enabled.
     */
    String HARDCORE_FULL = "HARDCORE_FULL";

    /**
     * Saves a completed campaign score.
     *
     * @param character character whose score should be saved
     * @throws IOException if the score cannot be written
     */
    void saveHighScore(GameCharacter character) throws IOException;

    /**
     * Loads formatted scores for one high score category.
     *
     * @param category category identifier to load
     * @return formatted high score text
     * @throws IOException if scores cannot be read
     */
    String loadHighScoresForCategory(String category) throws IOException;

    /**
     * Converts a category identifier into display text.
     *
     * @param category category identifier
     * @return user-facing category name
     */
    String getCategoryDisplayName(String category);
}
