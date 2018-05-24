package io.github.slavmetal;

import javax.swing.*;

interface GameState {
    public void update(GameStateManager gsm, JPanel gamePanel);
}
