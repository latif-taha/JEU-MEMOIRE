package io.github.slavmetal;

import javax.swing.JPanel;

import java.awt.*;

/**
 * Default panel that will be shown and changed by game states.
 */
class GamePanel extends JPanel {
    GamePanel() {
        this.setPreferredSize(new Dimension(500, 500));
        // Initialize GameStateManager and update content of the panel
        new GameStateManager().update(this);
    }
}