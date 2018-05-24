package io.github.slavmetal;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Runs a Swing Timer in a separate thread and shows it's value on JLabel.
 */
class TimerLabel extends JLabel {
    private Timer timer;
    private int countMinutes = 0;
    private int countSeconds = 0;

    /**
     * Contains Swing Timer initialization.
     */
    public TimerLabel() {
        // Every second increase value of seconds (or minutes) and update label's value.
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (countSeconds == 59){
                    countSeconds = 0;
                    countMinutes++;
                } else {
                    countSeconds++;
                }
                setText(Integer.toString(countMinutes) + ":" + Integer.toString(countSeconds));
            }
        });
    }

    public void stopTimer(){
        timer.stop();
    }

    public void startTimer(){
        timer.start();
    }
}
