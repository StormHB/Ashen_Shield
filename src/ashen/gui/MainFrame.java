package ashen.gui;

import ashen.model.GameCharacter;

import javax.smartcardio.Card;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private Container contentPane;

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

    public void showMainMenu() {
        cardLayout.show(contentPane, "mainMenu");
    }

    public void showCharacterCreation() {
        cardLayout.show(contentPane, "characterCreation");
    }

    public void showBattle(GameCharacter character) {

        setContentPane(new BattlePanel(character));

        revalidate();
        repaint();
    }
}
