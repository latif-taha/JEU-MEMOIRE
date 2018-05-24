package io.github.slavmetal;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MenuState extends JPanel implements GameState {
    private JButton gameButton = new JButton("Start New Game");
    private JButton scoresButton = new JButton("Show High Scores");
    private JButton exitButton = new JButton("Exit");

    public MenuState() {
    }

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

//        // Components of the dialog
//        JDialog nickDialog = new JDialog();
//        JPanel nickPanel = new JPanel();
//        nickPanel.setLayout(new BoxLayout(nickPanel, BoxLayout.Y_AXIS));
//        JTextField nickField = new JTextField();
//        JButton button = new JButton("OK");
//
//        // Dialog itself
//        JPanel tempPanel = new JPanel();
//        tempPanel.add(new JLabel("Input your nickname:"));
//        nickPanel.add(tempPanel);
//
//        tempPanel = new JPanel();
//        nickField.setPreferredSize(new Dimension(250, 30));
//        tempPanel.add(nickField);
//        nickPanel.add(tempPanel);
//
//        tempPanel = new JPanel();
//        tempPanel.add(button);
//        nickPanel.add(tempPanel);
    }
}
