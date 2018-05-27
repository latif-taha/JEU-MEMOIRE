package io.github.slavmetal;

import java.sql.Time;

/**
 * Structure for every row in the scores table.
 */
class ScoresList {
    private String nickname;
    private Time time;
    private int boardSize;
    private int score;

    ScoresList(String nickname, Time time, int boardSize, int score) {
        this.nickname = nickname;
        this.time = time;
        this.boardSize = boardSize;
        this.score = score;
    }

    /**
     * @return Player's nickname
     */
    String getNickname() {
        return nickname;
    }

    /**
     * @return Player's time
     */
    Time getTime() {
        return time;
    }

    /**
     * @return Player's board size
     */
    int getBoardSize() {
        return boardSize;
    }

    /**
     * @return Player's score
     */
    int getScore() {
        return score;
    }

}
