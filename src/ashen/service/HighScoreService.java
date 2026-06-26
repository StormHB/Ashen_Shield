package ashen.service;

import ashen.model.GameCharacter;
import ashen.model.Difficulty;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service class for saving and loading high scores from a text file.
 * High scores are stored as simple semicolon-separated text records.
 */
public class HighScoreService implements HighScoreProvider {

    private static final String HIGH_SCORE_FILE = "DATA/highscores.txt";

    /**
     * Saves a completed campaign result to the high score file.
     * The score category is determined automatically from the
     * character difficulty settings.
     *
     * @param character completed character whose result is being saved
     * @throws IOException if the high score file cannot be written
     */

    @Override
    public void saveHighScore(GameCharacter character) throws IOException {
        File file = new File(HIGH_SCORE_FILE);
        File parentFolder = file.getParentFile();

        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            writer.println(
                    getScoreCategory(character)
                            + ";"
                            + character.getName()
                            + ";"
                            + character.getCharacterClass()
                            + ";"
                            + character.getCurrentHp()
            );
        }
    }

    /**
     * Loads all high scores belonging to the specified category,
     * sorts them by remaining HP and returns a formatted text
     * representation suitable for display.
     *
     * @param category high score category to load
     * @return formatted high score text
     * @throws IOException if the high score file cannot be read
     */

    @Override
    public String loadHighScoresForCategory(String category) throws IOException {
        File file = new File(HIGH_SCORE_FILE);

        if (!file.exists()) {
            return "No high scores saved yet.";
        }

        List<String[]> scores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                String scoreCategory;
                String name;
                String characterClass;
                String hpText;

                if (parts.length == 4) {
                    scoreCategory = parts[0];
                    name = parts[1];
                    characterClass = parts[2];
                    hpText = parts[3];
                } else if (parts.length == 3) {
                    scoreCategory = NORMAL;
                    name = parts[0];
                    characterClass = parts[1];
                    hpText = parts[2];
                } else {
                    continue;
                }

                if (!category.equals(scoreCategory)) {
                    continue;
                }

                try {
                    int hp = Integer.parseInt(hpText);

                    if (hp >= 0) {
                        scores.add(new String[]{name, characterClass, hpText});
                    }

                } catch (NumberFormatException ignored) {
                    // Skip invalid score entries
                }
            }
        }

        scores.sort(new Comparator<String[]>() {
            @Override
            public int compare(String[] firstScore, String[] secondScore) {
                int firstHp = Integer.parseInt(firstScore[2]);
                int secondHp = Integer.parseInt(secondScore[2]);

                return Integer.compare(secondHp, firstHp);
            }
        });

        if (scores.isEmpty()) {
            return "No high scores saved in this category yet.";
        }

        StringBuilder result = new StringBuilder(getCategoryDisplayName(category));
        result.append("\n\n");

        for (int i = 0; i < scores.size(); i++) {
            String[] score = scores.get(i);

            result.append(i + 1)
                    .append(". ")
                    .append(score[0])
                    .append(" - ")
                    .append(score[1])
                    .append(" - ")
                    .append(score[2])
                    .append(" HP remaining\n");
        }

        return result.toString();
    }

    /**
     * Converts an internal score category identifier into a
     * user-friendly display name.
     *
     * @param category internal category identifier
     * @return formatted category name
     */

    @Override
    public String getCategoryDisplayName(String category) {
        switch (category) {
            case HARDCORE_HP:
                return "Hardcore - Enemy HP";
            case HARDCORE_DAMAGE:
                return "Hardcore - Enemy Damage";
            case HARDCORE_FULL:
                return "Hardcore - Enemy HP + Damage";
            default:
                return "Normal";
        }
    }

    /**
     * Determines the score category based on the selected
     * difficulty and hardcore modifiers.
     *
     * @param character character whose category is being determined
     * @return internal category identifier
     */

    private String getScoreCategory(GameCharacter character) {
        if (character.getDifficultyType() != Difficulty.HARDCORE) {
            return NORMAL;
        }

        boolean hpBonus = character.hasHardcoreHpBonus();
        boolean damageBonus = character.hasHardcoreDamageBonus();

        if (hpBonus && damageBonus) {
            return HARDCORE_FULL;
        }

        if (hpBonus) {
            return HARDCORE_HP;
        }

        if (damageBonus) {
            return HARDCORE_DAMAGE;
        }

        return NORMAL;
    }
}
