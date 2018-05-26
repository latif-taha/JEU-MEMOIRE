package io.github.slavmetal;

import javax.swing.*;
import java.util.ArrayList;

class GameStateManager {
    private ArrayList<GameState> gameStates = new ArrayList<>();
    private byte currentState;

    static final byte MENUSTATE = 0;
    static final byte PLAYSTATE = 1;
    static final byte SCORESTATE = 2;

    /**
     * Initializes all game's states and sets a default one.
     */
    GameStateManager(JPanel gamePanel) {
        gameStates.add(new MenuState(this, gamePanel));
        gameStates.add(new PlayState());
        gameStates.add(new ScoreState(this, gamePanel));
        currentState = MENUSTATE;
    }

    /**
     * Sets current state and updates it.
     * @param i         Number of the state
     * @param gamePanel Panel to change
     */
    void setCurrentState(byte i, JPanel gamePanel){
        currentState = i;
        update(gamePanel);
    }

    /**
     * Updates a panel (depending on the current state).
     * @param gamePanel Panel to change
     */
    void update(JPanel gamePanel){
        gameStates.get(currentState).update(this, gamePanel);
    }
}
