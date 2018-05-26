package io.github.slavmetal;

import javax.swing.*;

/**
 * Removes button's icon with a certain delay.
 */
class CardWorker extends SwingWorker {
    private Card button; // Button to remove icon of.

    CardWorker(Card button) {
        this.button = button;
    }

    @Override
    protected Object doInBackground() throws Exception {
        Thread.sleep(2000);  // 2s delay
        button.setIcon(null);   // Remove icon
        return null;
    }
}
