package io.github.slavmetal;

import javax.swing.*;
import java.util.ArrayList;

class GameStateManager {
    private ArrayList<GameState> gameStates = new ArrayList<>();
    private int currentState;

    public static final int MENUSTATE = 0;
    public static final int PLAYSTATE = 1;
    public static final int SCORESTATE = 2;

    public GameStateManager() {
        gameStates.add(new MenuState());
        gameStates.add(new PlayState());
        gameStates.add(new ScoreState());
        currentState = MENUSTATE;
    }

    public void setCurrentState(int i, JPanel gamePanel){
        currentState = i;
        update(gamePanel);
    }

    public void update(JPanel gamePanel){
        gameStates.get(currentState).update(this, gamePanel);
    }
}
