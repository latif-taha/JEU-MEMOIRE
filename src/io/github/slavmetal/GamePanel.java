package io.github.slavmetal;

import javax.swing.JPanel;

import java.awt.*;

class GamePanel extends JPanel {
    private GameStateManager gsm;

    public GamePanel() {
        this.setPreferredSize(new Dimension(400, 400));
        gsm = new GameStateManager();
        update();
    }

    private void update() {
        gsm.update(this);
    }

}