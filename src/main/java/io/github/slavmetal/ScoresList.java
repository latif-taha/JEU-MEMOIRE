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

    String getNickname() {
        return nickname;
    }

    Time getTime() {
        return time;
    }

    int getBoardSize() {
        return boardSize;
    }

    int getScore() {
        return score;
    }

}
