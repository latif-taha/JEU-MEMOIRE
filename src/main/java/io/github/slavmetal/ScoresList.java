package io.github.slavmetal;

import java.sql.Time;

class ScoresList {
    private String nickname;
    private Time time;
    private int boardSize;
    private int score;

    public ScoresList(String nickname, Time time, int boardSize, int score) {
        this.nickname = nickname;
        this.time = time;
        this.boardSize = boardSize;
        this.score = score;
    }

    public String getNickname() {
        return nickname;
    }

    public Time getTime() {
        return time;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getScore() {
        return score;
    }

}
