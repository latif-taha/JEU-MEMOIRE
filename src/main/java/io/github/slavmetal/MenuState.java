package io.github.slavmetal;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Game's main menu.
 */
class MenuState extends JPanel implements GameState {
    private JButton gameButton = new JButton("Start New Game");
    private JButton scoresButton = new JButton("Show High Scores");
    private JButton exitButton = new JButton("Exit");

    public MenuState() {
    }

    /**
     * Updates the content of current panel.
     * @param gsm       GameStateManager
     * @param gamePanel Panel to update
     */
    @Override
    public void update(GameStateManager gsm, JPanel gamePanel) {
        System.out.println("MENU STATE");

        gameButton.addActionListener(actionEvent -> gsm.setCurrentState(GameStateManager.PLAYSTATE, gamePanel));
        scoresButton.addActionListener(actionEvent -> gsm.setCurrentState(GameStateManager.SCORESTATE, gamePanel));
        exitButton.addActionListener(actionEvent -> System.exit(0));

        gamePanel.removeAll();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));

        JPanel tempPanel = new JPanel();
        tempPanel.add(gameButton);
        gamePanel.add(tempPanel);

        tempPanel = new JPanel();
        tempPanel.add(scoresButton);
        gamePanel.add(tempPanel);

        tempPanel = new JPanel();
        tempPanel.add(exitButton);
        gamePanel.add(tempPanel);

        gamePanel.updateUI();
    }
}
