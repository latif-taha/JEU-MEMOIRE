package io.github.slavmetal;

import org.pmw.tinylog.Logger;

import javax.swing.*;
import java.awt.*;
import java.net.URISyntaxException;

/**
 * Game's main menu.
 */
class MenuState implements GameState {
    private JButton gameButton = new JButton("Start New Game");
    private JButton scoresButton = new JButton("Show High Scores");
    private JButton exitButton = new JButton("Exit");
    private JButton aboutButton = new JButton("About");

    MenuState(GameStateManager gsm, JPanel gamePanel) {
        // Switch from menu to the game board
        gameButton.addActionListener(actionEvent -> gsm.setCurrentState(GameStateManager.PLAYSTATE, gamePanel));
        // Switch from menu to the highscores
        scoresButton.addActionListener(actionEvent -> gsm.setCurrentState(GameStateManager.SCORESTATE, gamePanel));
        // Show about dialog
        aboutButton.addActionListener(actionEvent -> {
            try {
                new AboutDialog();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
        // Exit the app
        exitButton.addActionListener(actionEvent -> System.exit(0));

        // Set buttons' preferred size
        Dimension buttDimension = new Dimension(300, 50);
        gameButton.setPreferredSize(buttDimension);
        scoresButton.setPreferredSize(buttDimension);
        exitButton.setPreferredSize(buttDimension);
        aboutButton.setPreferredSize(buttDimension);
    }

    /**
     * Updates the content of current panel.
     * @param gsm       GameStateManager
     * @param gamePanel Panel to update
     */
    @Override
    public void update(GameStateManager gsm, JPanel gamePanel) {
        gamePanel.removeAll();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));

        // Create 'padding' from the top
        gamePanel.add(Box.createRigidArea(new Dimension(0, 50)));

        // Use temp panel to use elements' preferred size on the gamePanel
        JPanel tempPanel = new JPanel();
        tempPanel.add(gameButton);
        gamePanel.add(tempPanel);

        tempPanel = new JPanel();
        tempPanel.add(scoresButton);
        gamePanel.add(tempPanel);

        tempPanel = new JPanel();
        tempPanel.add(aboutButton);
        gamePanel.add(tempPanel);

        tempPanel = new JPanel();
        tempPanel.add(exitButton);
        gamePanel.add(tempPanel);

        gamePanel.updateUI();

        Logger.info("Menu State updated");
    }
}
