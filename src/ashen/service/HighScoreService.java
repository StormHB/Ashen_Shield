package ashen.service;

import ashen.model.GameCharacter;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service class for saving and loading high scores from a text file.
 * High scores are stored as simple semicolon-separated text records.
 */
public class HighScoreService {

    private static final String HIGH_SCORE_FILE = "DATA/highscores.txt";

    /**
     * Saves a completed campaign score to the high score text file.
     *
     * @param character character that completed the campaign
     * @throws IOException if the score cannot be written
     */
    public void saveHighScore(GameCharacter character) throws IOException {
        File file = new File(HIGH_SCORE_FILE);
        File parentFolder = file.getParentFile();

        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            writer.println(
                    character.getName()
                            + ";"
                            + character.getCharacterClass()
                            + ";"
                            + character.getCurrentHp()
            );
        }
    }

    /**
     * Loads all high scores from the text file and formats them for display.
     *
     * @return formatted high score text
     * @throws IOException if the score file cannot be read
     */
    public String loadHighScores() throws IOException {
        File file = new File(HIGH_SCORE_FILE);

        if (!file.exists()) {
            return "No high scores saved yet.";
        }

        List<String[]> scores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length == 3) {
                    try {
                        int hp = Integer.parseInt(parts[2]);

                        if (hp >= 0) {
                            scores.add(parts);
                        }

                    } catch (NumberFormatException ignored) {
                        // Skip invalid score entries
                    }
                }
            }
        }

        scores.sort(
                Comparator.comparingInt(
                        score -> -Integer.parseInt(score[2])
                )
        );

        StringBuilder result = new StringBuilder("High Scores\n\n");

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
}