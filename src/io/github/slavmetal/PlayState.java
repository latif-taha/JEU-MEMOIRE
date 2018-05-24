package io.github.slavmetal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.ParseException;
import java.util.*;

/**
 * State when playing the game.
 */
class PlayState extends JPanel implements GameState, ActionListener {
    private final int DEFAULTSIZE = 4;                  // Default size of square board (one of the sides)
    private int size = DEFAULTSIZE;                     // Set size of the board
    private ArrayList<File> imageFiles;                 // Store all found images for cards
    private ArrayList<Card> cards = new ArrayList<>();  // Store all cards for the board
    private byte currClick = 1;                         // Number of current click on card
    private Card prevCard;                              // Store previous card to compare it with latest pressed one
    private byte currPairsNum = 0;                      // Number of guessed pairs

    private GameStateManager gsm;                       // GameStateManager to set new game's state in Action Listener
    private JPanel panel;                               // Panel to pass when setting new game's state in Action Listener
    private TimerLabel timerLabel = new TimerLabel();   // Timer label to show current game time

    /**
     * Default empty constructor
     */
    public PlayState() {
        // Get all jpg/jpeg/png files from 'img' directory
        File dir = new File("./img");
        imageFiles = new ArrayList<>(Arrays.asList(Objects.requireNonNull(dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return (s.toLowerCase().endsWith("jpg") ||
                        s.toLowerCase().endsWith("jpeg") ||
                        s.toLowerCase().endsWith("png"));
            }
        }))));

        // Shuffle so that each game we get different images on the board
        Collections.shuffle(imageFiles);
    }

    @Override
    public void update(GameStateManager gsm, JPanel gamePanel) {
        System.out.println("PLAY STATE");
        this.gsm = gsm;
        this.panel = gamePanel;

        // Modal dialog to choose size of the board
        sizeInput();

        gamePanel.removeAll();
        gamePanel.setLayout(new BorderLayout());

        JPanel cardsPanel = new JPanel(new GridLayout(size, size)); // Panel for cards
        JPanel menuPanel = new JPanel();                            // Panel for JMenuBar where we show timer

        JMenuBar menuBar = new JMenuBar();                          // JMenuBar where we show timer
        menuBar.add(timerLabel);                                    // Add timer to it

        menuPanel.add(menuBar);
        menuPanel.setPreferredSize(new Dimension(400, 30));

        // Every time add 2 same cards to the array
        for (int i = 0; i < (size * size) / 2; i++) {
            cards.add(new Card(imageFiles.get(i), this, Integer.toString(i)));
            cards.add(new Card(imageFiles.get(i), this, Integer.toString(i)));
        }

        // Shuffle cards in the array
//        Collections.shuffle(cards);

        // And finally add all cards to the board
        for (int i = 0; i < size * size; i++) {
            cardsPanel.add(cards.get(i));
        }

        gamePanel.add(menuPanel, BorderLayout.SOUTH);   // Show menu panel with timer on the bottom
        gamePanel.add(cardsPanel, BorderLayout.CENTER); // Show board at the center
        gamePanel.updateUI();                           // Update UI
        timerLabel.startTimer();                        // Start timer just after showing the new UI
    }

    /**
     * Shows modal dialog to input size of the board.
     */
    private void sizeInput(){
        // Components of the dialog
        JDialog sizeDialog = new JDialog();
        JPanel sizePanel = new JPanel();
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(/*DEFAULTSIZE*/2, 2, 20, 2));
        JButton button = new JButton("OK");

        // TODO Focus on OK button
        // TODO Check if we have enough pictures

        button.addActionListener(actionEvent -> {
            try{
                // Check if current value is OK
                // If not, last correct value will be used
                spinner.commitEdit();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            size = (Integer) spinner.getValue();    // Set new size of the board
            sizeDialog.dispose();                   // Close the dialog
        });

        // Dialog itself
        sizePanel.add(new JLabel("Select grid size:"));
        sizePanel.add(spinner);
        sizePanel.add(button);

        // Some parameters
        sizeDialog.setContentPane(sizePanel);
        sizeDialog.setSize(new Dimension(500, 190));
        sizeDialog.setLocationRelativeTo(null);
        sizeDialog.setModal(true);
        sizeDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        sizeDialog.setVisible(true);
    }

    /**
     * Processes clicks on the cards.
     * @param actionEvent
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

                // And if it's not the last guessed pair, increment our value
                if(currPairsNum != ((size * size) / 2) - 1) {
                    currPairsNum++;
                } else {
                    // Otherwise stop timer and show scores state
                    System.out.println("WIN");
                    timerLabel.stopTimer();
                    nickInput();
                    gsm.setCurrentState(GameStateManager.SCORESTATE, panel, Context.GAME);
                }
            } else {
                // If cards are not identical, turn back both of them
                prevCard.turn();
                pressedCard.turn();
            }
        }

        prevCard = pressedCard;
    }

    private void nickInput(){
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
        } catch (IOException e){
            e.printStackTrace();
        }

        // Components of the dialog
        JDialog nickDialog = new JDialog();
        JPanel nickPanel = new JPanel();
        nickPanel.setLayout(new BoxLayout(nickPanel, BoxLayout.Y_AXIS));
        JTextField nickField = new JTextField();
        nickField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if (nickField.getText().length() >= Integer.parseInt(prop.getProperty("maxnicklength")))
                    // Just say we got this request but do nothing
                    keyEvent.consume();
            }
        });
        JButton button = new JButton("OK");

        // TODO Focus on OK button

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Class.forName(prop.getProperty("dbdriver"));
                    Connection connection = DriverManager.getConnection(
                            prop.getProperty("dbconnection"),
                            prop.getProperty("dbuser"),
                            prop.getProperty("dbpassword"));
                    Statement statement = connection.createStatement();

                    statement.executeUpdate("INSERT INTO SCORES (NICKNAME, PLAYTIME, BOARDSIZE, SCORE) VALUES ('"
                            +nickField.getText().replaceAll("'", "").trim()+"', " +
                            "'12:00:00'," +
                            " '4'," +
                            " '5000')");

                    statement.close();
                    connection.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
                // TODO LOG USER HERE OR IN UPDATE METHOD
                nickDialog.dispose();
            }
        });

        // Dialog itself
        JPanel tempPanel = new JPanel();
        tempPanel.add(new JLabel("Input your nickname:"));
        nickPanel.add(tempPanel);

        tempPanel = new JPanel();
        nickField.setPreferredSize(new Dimension(250, 30));
        tempPanel.add(nickField);
        nickPanel.add(tempPanel);

        tempPanel = new JPanel();
        tempPanel.add(button);
        nickPanel.add(tempPanel);

        // Some parameters
        nickDialog.setContentPane(nickPanel);
        nickDialog.setSize(new Dimension(300, 190));
        nickDialog.setLocationRelativeTo(null);
        nickDialog.setModal(true);
        nickDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        nickDialog.setVisible(true);
    }
}
