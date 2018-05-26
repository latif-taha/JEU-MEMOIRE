package io.github.slavmetal;

import javax.swing.*;

interface GameState {
    void update(GameStateManager gsm, JPanel gamePanel);
}
