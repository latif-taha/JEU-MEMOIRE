package io.github.slavmetal;

import org.pmw.tinylog.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Time;
import java.util.*;

/**
 * Game board with dialogs for choosing size and nickname.
 */
class PlayState implements GameState, ActionListener {
    private final int DEFAULTSIZE = 4;                  // Default size of square board (one of the sides)
    private int size = DEFAULTSIZE;                     // Set size of the board
    private ArrayList<File> imageFiles;                 // Store all found images for cards
    private ArrayList<Card> cards;                      // Store all cards for the board
    private byte currClick;                             // Number of current click on card
    private Card prevCard;                              // Store previous card to compare it with latest pressed one
    private byte currPairsNum;                          // Number of guessed pairs
    private int score;                                  // Total score

    private GameStateManager gsm;                       // GameStateManager to set new game's state in Action Listener
    private JPanel panel;                               // Panel to pass when setting new game's state in Action Listener
    private TimerLabel timerLabel;                      // Timer label to show current game time

    PlayState() {
        loadImages();
    }

    /**
     * Updates the content of current panel.
     * @param gsm       GameStateManager
     * @param gamePanel Panel to update
     */
    @Override
    public void update(GameStateManager gsm, JPanel gamePanel) {
        // Objects that will be used in ActionListener
        this.gsm = gsm;
        this.panel = gamePanel;
        resetValues();

        // Modal dialog to choose size of the board
        FieldSizeDialog fsd = new FieldSizeDialog(DEFAULTSIZE);
        size = fsd.getChosenSize();

        gamePanel.removeAll();
        gamePanel.setLayout(new BorderLayout());

        JPanel cardsPanel = new JPanel(new GridLayout(size, size)); // Panel for cards
        JPanel menuPanel = new JPanel();                            // Panel for JMenuBar where we show timer

        JMenuBar menuBar = new JMenuBar();                          // JMenuBar where we show timer
        menuBar.add(timerLabel);                                    // Add timer to it

        menuPanel.add(menuBar);
        menuPanel.setPreferredSize(new Dimension(500, 30));

        // Every time add 2 same cards to the array
        for (int i = 0; i < (size * size) / 2; i++) {
            cards.add(new Card(imageFiles.get(i), this, Integer.toString(i)));
            cards.add(new Card(imageFiles.get(i), this, Integer.toString(i)));
        }

        // Shuffle cards in the array
        Collections.shuffle(cards);

        // And finally add all cards to the board
        for (int i = 0; i < size * size; i++) {
            cardsPanel.add(cards.get(i));
        }

        gamePanel.add(menuPanel, BorderLayout.SOUTH);   // Show menu panel with timer on the bottom
        gamePanel.add(cardsPanel, BorderLayout.CENTER); // Show board at the center
        gamePanel.updateUI();                           // Update UI
        timerLabel.startTimer();                        // Start timer just after showing the new UI

        Logger.info("Play State updated");
    }

    /**
     *  Resets game values before every game.
     */
    private void resetValues(){
        // This code is even too ugly
        timerLabel = new TimerLabel();
        cards = new ArrayList<>();
        currClick = 1;
        currPairsNum = 0;
        score = 0;
    }

    /**
     * Loads all images we can use for cards.
     */
    private void loadImages(){
        // Load resources
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File dir = new File(Objects.requireNonNull(classLoader.getResource("board/img")).getFile());

        // Get all jpg/jpeg/png files from 'img' directory to the list
        imageFiles = new ArrayList<>(Arrays.asList(Objects.requireNonNull(dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return (s.toLowerCase().endsWith("jpg") ||
                        s.toLowerCase().endsWith("jpeg") ||
                        s.toLowerCase().endsWith("png"));
            }
        }))));

        Collections.shuffle(imageFiles);
    }

    /**
     * Processes clicks on the cards.
     * @param actionEvent   ActionEvent to get the pressed card
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // Anyway turn the pressed car
        Card pressedCard = (Card) actionEvent.getSource();
        pressedCard.turn();

        // If it's first click do nothing and wait for the second one
        if (currClick == 1){
            currClick = 2;
        } else if (currClick == 2){
            currClick = 1;

            // If previous and last pressed cards are identical (by name and id) make them disabled
            if ((pressedCard.getName().equals(prevCard.getName()) && (pressedCard.getId() != prevCard.getId()))){
                prevCard.deactivate();
                pressedCard.deactivate();

                score += size * size;

                // And if it's not the last guessed pair, increment our value
                if(currPairsNum != ((size * size) / 2) - 1) {
                    currPairsNum++;
                } else {
                    // Otherwise stop timer and show scores state
                    Logger.info("Game win");
                    timerLabel.stopTimer();
                    new NicknameDialog(new Time(0, timerLabel.getCountMinutes(), timerLabel.getCountSeconds()),
                            size, score); // Input nick and write it to the DB
                    gsm.setCurrentState(GameStateManager.SCORESTATE, panel);
                }
            } else {
                // If cards are not identical, turn back both of them
                prevCard.turn();
                pressedCard.turn();

                score -= size;
            }
        }

        prevCard = pressedCard;
    }
}
