package ashen.service;

import ashen.model.GameCharacter;

import java.io.IOException;

/**
 * Abstraction for saving and loading high scores.
 */
public interface HighScoreProvider {

    String NORMAL = "NORMAL";
    String HARDCORE_HP = "HARDCORE_HP";
    String HARDCORE_DAMAGE = "HARDCORE_DAMAGE";
    String HARDCORE_FULL = "HARDCORE_FULL";

    void saveHighScore(GameCharacter character) throws IOException;

    String loadHighScoresForCategory(String category) throws IOException;

    String getCategoryDisplayName(String category);
}
