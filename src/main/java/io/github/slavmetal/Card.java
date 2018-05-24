package io.github.slavmetal;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Card object to show on the board.
 */
class Card extends JButton {
    private static AtomicInteger nextId = new AtomicInteger();  // Thread-safe integer
    private final int id;                                       // Unique id of the card
    private final ImageIcon icon;                               // Image to show on the button
    private CardButtonState state;                              // Current card's state (e.g. current side of card)

    /**
     * Default constructor
     * @param file
     * @param al
     * @param name
     */
    public Card(File file, ActionListener al, String name) {
        id = nextId.incrementAndGet();              // Set incremented ID
        state = CardButtonState.REAR;               // Default state is REAR
        icon = new ImageIcon(String.valueOf(file));
        this.addActionListener(al);
        this.setName(name);

        // Some fancy staff
        this.setContentAreaFilled(false);
    }

    /**
     * Flips the card depending on it's state.
     */
    public void turn(){
        if(state == CardButtonState.REAR){
            // If state is REAR, set FRONT state and show the image
            state = CardButtonState.FRONT;
            this.setIcon(icon);
        } else if (state == CardButtonState.FRONT){
            CardWorker cw = new CardWorker(this);
            cw.execute();

            // Otherwise set REAR state and remove image
            state = CardButtonState.REAR;
//            this.setIcon(null);
        }
    }

    /**
     * Makes interaction with button impossible.
     */
    public void deactivate() {
        this.state = CardButtonState.NOTACTIVE;
        this.setEnabled(false);
    }

    /**
     * @return Card's unique ID
     */
    public int getId() {
        return id;
    }
}
