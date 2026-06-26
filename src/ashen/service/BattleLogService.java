package ashen.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Utility class for exporting battle logs.
 */
public final class BattleLogService {

    private BattleLogService() {
    }

    public static void exportBattleLog(String battleLog, String filePath) throws IOException {
        File outputFile = new File(filePath);
        File parentFolder = outputFile.getParentFile();

        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(outputFile)) {
            writer.print(battleLog);
        }
    }
}
