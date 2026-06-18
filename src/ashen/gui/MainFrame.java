package ashen.gui;

import ashen.model.GameCharacter;
import ashen.service.SaveLoadService;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private Container contentPane;

    private StringBuilder campaignBattleLog = new StringBuilder();

    public MainFrame() {
        super("Ashen Shield");

        initFrame();
        initLayout();
        showMainMenu();
    }

    private void initFrame() {
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initLayout() {
        cardLayout = new CardLayout();
        contentPane = getContentPane();
        contentPane.setLayout(cardLayout);

        contentPane.add(new MainMenuPanel(this), "mainMenu");
        contentPane.add(new CharacterCreationPanel(this), "characterCreation");
    }

    public void showDefeatPanel(GameCharacter character) {
        clearMenuBar();
        setContentPane(new DefeatPanel(this, character));
        revalidate();
        repaint();
    }

    public void showMainMenu() {
        clearMenuBar();
        setContentPane(new MainMenuPanel(this));
        revalidate();
        repaint();
    }

    public void showCharacterCreation() {
        clearMenuBar();
        setContentPane(new CharacterCreationPanel(this));
        revalidate();
        repaint();
    }

    public void showBattle(GameCharacter character) {
        clearCampaignBattleLog();
        showBattle(character, 0);
    }

    public void showBattle(GameCharacter character, int enemyIndex) {
        setContentPane(new BattlePanel(this, character, enemyIndex));
        revalidate();
        repaint();
    }

    public void loadCharacter() {
        JFileChooser fileChooser = new JFileChooser("DATA");
        fileChooser.setAcceptAllFileFilterUsed(true);

        fileChooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "Character Saves (*.ser)",
                        "ser"
                )
        );

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                SaveLoadService saveLoadService = new SaveLoadService();
                GameCharacter character = saveLoadService.loadCharacter(selectedFile.getPath());

                character.restoreFullHp();

                showBattle(character);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to load character.",
                        "Load Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    public void showVictoryPanel(GameCharacter character, int defeatedEnemyIndex, String defeatedEnemyName) {
        clearMenuBar();
        setContentPane(new VictoryPanel(this, character, defeatedEnemyIndex, defeatedEnemyName));
        revalidate();
        repaint();
    }

    public void appendToCampaignBattleLog(String text) {
        campaignBattleLog.append(text);
    }

    public String getCampaignBattleLog() {
        return campaignBattleLog.toString();
    }

    public void clearCampaignBattleLog() {
        campaignBattleLog.setLength(0);
    }

    private void clearMenuBar() {
        setJMenuBar(null);
    }
}
