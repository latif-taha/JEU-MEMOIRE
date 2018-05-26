package io.github.slavmetal;

import javax.swing.*;

/**
 * Interface which defines all common methods for states.
 */
interface GameState {
    void update(GameStateManager gsm, JPanel gamePanel);
}
