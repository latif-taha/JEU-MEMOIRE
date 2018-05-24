package io.github.slavmetal;

import javax.swing.*;

class CardWorker extends SwingWorker {
    private JButton button;

    public CardWorker(JButton button) {
        this.button = button;
    }

    @Override
    protected Object doInBackground() throws Exception {
        Thread.sleep(2000);
        button.setIcon(null);
        return null;
    }
}
